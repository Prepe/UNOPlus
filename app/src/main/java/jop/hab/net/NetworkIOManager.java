package jop.hab.net;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by jopihabich on 15.04.18.
 */

public class NetworkIOManager {


    ObserverInterface observerInterface;

    ServerClass serverClass;
    ClientClass clientClass;
    SendReceive sendReceive;


    String hostAdress;

    boolean MODE_IS_SERVER;
    static final int MESSAGE_READ = 1;

    String testText;

    public NetworkIOManager(ObserverInterface observerInterface) {
        this.observerInterface = observerInterface;


    }

    public void setMode(String mode) {

        if (mode.equals("server")) {
            MODE_IS_SERVER = true;
        } else {
            MODE_IS_SERVER = false;
        }

    }

    public void setHostAdress(String hostAdress) {
        this.hostAdress = hostAdress;
    }

    public String getTestText(){
        return testText;
    }
    public void open() {

        if(MODE_IS_SERVER){

            serverClass = new ServerClass();
            serverClass.start();
            Log.d("@mode",MODE_IS_SERVER+"");
        }else{
            clientClass = new ClientClass(hostAdress);
            clientClass.start();
            Log.d("@mode",MODE_IS_SERVER+"");

        }


    }

    public void writeMsg(String msg){

        sendReceive.write(msg.getBytes());
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {

                case MESSAGE_READ:
                    byte[] readBuffer = (byte[]) msg.obj;
                    String tmpmsg = new String(readBuffer, 0, msg.arg1);
                    
                    testText = tmpmsg;

                    Log.d("@handler",testText);


                    observerInterface.dataChanged();



                    break;
            }

            return true;
        }
    });


    public class ServerClass extends Thread {

        Socket socket;
        ServerSocket serverSocket;

        @Override
        public void run() {


            Log.d("@serverclass","Serverclass running");

            try {
                serverSocket = new ServerSocket(8888);
                socket = serverSocket.accept();

                sendReceive = new SendReceive(socket);
                sendReceive.start();

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    private class SendReceive extends Thread {


        private Socket socket;
        private InputStream inputStream;
        private OutputStream outputStream;

        public SendReceive(Socket socket) {



            this.socket = socket;
            try {
                this.inputStream = socket.getInputStream();
                this.outputStream = socket.getOutputStream();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {

            Log.d("@sendreceive","sr running");


            byte[] buffer = new byte[1024];
            int  bytes;

            while(socket!=null){

                try {
                    bytes= inputStream.read(buffer);
                    if(bytes>0){

                        handler.obtainMessage(MESSAGE_READ,bytes,-1,buffer).sendToTarget();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }


        }


        public void write(byte[]bytes){

            try {

                Log.d("@write",bytes.toString());

                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public class ClientClass extends Thread {

        Socket socket;
        String hostAdd;

        public ClientClass(String hostAddress) {

        Log.d("@clientclass",hostAddress);
            hostAdd = hostAddress;
            socket = new Socket();
        }

        @Override
        public void run() {
            super.run();
            try {
                socket.connect(new InetSocketAddress(hostAdd, 8888), 500);
                sendReceive = new SendReceive(socket);
                sendReceive.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
