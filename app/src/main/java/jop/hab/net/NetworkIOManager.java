package jop.hab.net;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.marti.unoplus.GameActions;
import com.example.marti.unoplus.GameStatics;
import com.example.marti.unoplus.gameLogicImpl.GameController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by jopihabich on 15.04.18.
 */


//Erklärung des Network: einfach den Zahlen folgen!
//Start in der ConnectionScreen (is eigentlich unser Lobby Screen... könnte mal unbenannt werden...


public class NetworkIOManager {
    ObserverInterface observerInterface;

    public ServerClass serverClass;
    ClientClass clientClass;
    SendReceive sendReceive;


    String hostAdress;


    public boolean MODE_IS_SERVER = false;
    static final int MESSAGE_READ = 1;

    String testText;
    GameActions gameAction;
    LinkedList<GameActions> actions = new LinkedList<>();
    int countready = 0;
    public int numclients;
    boolean isReady = false;

    public NetworkIOManager(ObserverInterface observerInterface) {
        this.observerInterface = observerInterface;
    }

    public void setObserverInterface(ObserverInterface oInterface) {
        this.observerInterface = oInterface;
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

    public void setNumclients(int numclients) {
        this.numclients = numclients;
    }

    public String getTestText() {
        return testText;
    }

    public LinkedList<GameActions> getGameAction() {
        return actions;
    }

    public void open() {
        if (MODE_IS_SERVER) {
            serverClass = new ServerClass();
            serverClass.start();
            serverClass.ready = true;
            Log.d("@mode", MODE_IS_SERVER + "");
        } else {
            clientClass = new ClientClass(hostAdress);
            clientClass.start();
            Log.d("@mode", MODE_IS_SERVER + "");
        }
    }


    public void writeGameaction(GameActions gameAction) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        Gson gson = gsonBuilder.create();

        String GameActionString = gson.toJson(gameAction);

        Log.d("GSON Senden", GameActionString);
        sendReceive.write(GameActionString.getBytes());
    }

    public LinkedList<GameActions> receiveGameaction(String gameActionString) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        Gson gson = gsonBuilder.create();

        try {
            actions.add(gson.fromJson(gameActionString,GameActions.class));
            Log.d("GAMEACTION", gameAction.action.toString());
        } catch (Exception e) {
            Log.e("JSon error", "Retry");
            receiveGameactionRetry(gameActionString);
        }

        return actions;
    }

    void receiveGameactionRetry(String gameActionString) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        Gson gson = gsonBuilder.create();

        try {
            actions.add(gson.fromJson(gameActionString,GameActions.class));
        } catch (Exception e) {
            Log.e("JSon error", "Splitting");
            receiveGameactionSplitting(gameActionString);
        }
    }

    void receiveGameactionSplitting(String gameActionString) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        Gson gson = gsonBuilder.create();
        LinkedList<String> gameactions = new LinkedList<>();

        Log.d("NIOM", "Splitting Start");
        int helper = 0;
        for (int i = 0; i < gameActionString.length() - 1; i++) {
            char char1 = gameActionString.charAt(i);
            char char2 = gameActionString.charAt(i + 1);
            if ('}' == char1 && '{' == char2) {
                Log.d("NIOM", "Splitting1");
                gameactions.add(gameActionString.substring(helper, i + 1));
                helper = i + 1;
            } else if ('}' == char1 && !('{' == char2)) {
                Log.d("NIOM", "Splitting2");
                gameactions.add(gameActionString.substring(helper, i + 1));
                helper = i + 1;
            }
        }
        gameactions.add(gameActionString.substring(helper));

        for (int i = 0; i < gameactions.size(); i++) {
            Log.d("Action", gameactions.get(i));
            try {
                actions.add(gson.fromJson(gameactions.get(i), GameActions.class));
                Log.d("GAMEACTION", gameAction.action.toString());
            } catch (Exception e) {
                Log.e("JSon error", "ERROR");
            }
        }
    }

    public void updatesProcessed() {
        actions = new LinkedList<>();
    }

    public void writeReady() {
        String ready = "ready";
        //sendReceive.write(ready.getBytes());
    }

    public boolean waitforClientsreadyingup() {
        while (countready != numclients) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public boolean isNotReady() {
        if (serverClass == null) {
            return true;
        } else if (sendReceive == null) {
            return true;
        }
        return !(sendReceive.ready && serverClass.ready);
    }

    public void close() {
        if (serverClass != null) {
            closeServer();
        }
        if (clientClass != null) {
            closeClient();
        }
        if (sendReceive != null) {
            closeSendReceive();
        }
        if (handler != null) {
            closeHandler();
        }
    }

    void closeServer() {
        try {
            if (serverClass.socket != null){
                serverClass.socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (serverClass.serverSocket != null) {
                serverClass.serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        serverClass = null;
    }

    void closeClient() {
        try {
            clientClass.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientClass = null;
    }

    void closeSendReceive() {
        try {
            sendReceive.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (OutputStream o : sendReceive.outputStream) {
            try {
                o.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (InputStream i : sendReceive.inputStream) {
            try {
                i.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sendReceive = null;
    }

    void closeHandler() {
        handler = null;
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_READ:
                    byte[] readBuffer = (byte[]) msg.obj;
                    String tmpmsg = new String(readBuffer, 0, msg.arg1);
                    if (!tmpmsg.equals("ready")) {
                        Log.d("JSon Empfangen", tmpmsg);
                        actions = receiveGameaction(tmpmsg);
                        observerInterface.dataChanged();
                    } else {
                        countready++;
                    }
                    break;
            }
            return true;
        }
    });


    public class ServerClass extends Thread {
        Socket socket;
        ServerSocket serverSocket;
        boolean ready = false;

        @Override
        public void run() {
            if (serverSocket != null) {
                if (!serverSocket.isClosed()) {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            Log.d("@serverclass", "Serverclass running");
            try {
                serverSocket = new ServerSocket(8888);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("@error", "sc catched");
            }

            sendReceive = new SendReceive();
            sendReceive.start();
            sendReceive.ready = true;
        }

        public boolean addClient() {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (socket != null) {
                Log.d("HOST","Client Socket");
                Log.d("HOST",socket.toString());
                sendReceive.addClient(socket);
                return true;
            }
            Log.d("HOST", "No one connectet");
            return false;
        }
    }

    private class SendReceive extends Thread {
        private Socket socket;
        private ArrayList<InputStream> inputStream = new ArrayList<>();
        private ArrayList<OutputStream> outputStream = new ArrayList<>();
        boolean ready = false;

        public SendReceive(Socket socket) {
            this.socket = socket;

            try {
                Log.d("CLIENT", "addHostSocket");
                Log.d("CLIENT", socket.toString());
                this.inputStream.add(socket.getInputStream());
                this.outputStream.add(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public SendReceive() {
        }

        public void addClient(Socket socket) {
            this.socket = socket;

            try {
                Log.d("HOST", "addClientSocket");
                Log.d("HOST", socket.toString());
                this.inputStream.add(socket.getInputStream());
                this.outputStream.add(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            Log.d("SENDRECIEVE", "SendRecieveist jetzt gestartet");
            byte[] buffer = new byte[1024];

            int bytes;

            while (socket != null) {
                if (inputStream != null) {
                    for (InputStream input : inputStream) {
                        try {
                            bytes = input.read(buffer);
                            if (bytes > 0) {
                                handler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        public void write(byte[] bytes) {
            for (OutputStream output : outputStream) {
                try {
                    output.write(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public class ClientClass extends Thread {
        Socket socket;
        String hostAdd;

        public ClientClass(String hostAddress) {
            hostAdd = hostAddress;
            socket = new Socket();
        }

        @Override
        public void run() {
            super.run();
            try {
                socket.connect(new InetSocketAddress(hostAdd, 8888), 500);
            } catch (IOException e) {
                e.printStackTrace();
            }

            sendReceive = new SendReceive(socket);
            sendReceive.start();
        }
    }
}