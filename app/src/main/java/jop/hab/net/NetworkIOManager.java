package jop.hab.net;

import android.os.Handler;
import android.os.Message;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import com.example.marti.unoplus.GameActions;
import com.example.marti.unoplus.cards.Card;
import com.google.gson.Gson;
import com.example.marti.unoplus.gameLogicImpl.GameControler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by jopihabich on 15.04.18.
 */

public class NetworkIOManager {
    GameControler GC;

    ObserverInterface observerInterface;

    ServerClass serverClass;
    ClientClass clientClass;
    SendReceive sendReceive;


    String hostAdress;



    boolean MODE_IS_SERVER = false;
    static final int MESSAGE_READ = 1;

    String testText;
    GameActions gameAction;

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

    public String getTestText() {
        return testText;
    }

    public GameActions getGameAction() {
        return gameAction;
    }

    public void open() {

        if (MODE_IS_SERVER) {

            serverClass = new ServerClass();
            serverClass.start();
            Log.d("@mode", MODE_IS_SERVER + "");
        } else {
            clientClass = new ClientClass(hostAdress);
            clientClass.start();
            Log.d("@mode", MODE_IS_SERVER + "");

        }


    }

    public void writeMsg(String msg) {

        sendReceive.write(msg.getBytes());
    }

   /* public void writeCard(Card c) {


        sendReceive.write(toJSon(c).getBytes());

    }*/

    public void writeGameaction(GameActions gameAction) {

        Gson gson = new Gson();

        String GameActionString = gson.toJson(gameAction);

        sendReceive.write(GameActionString.getBytes());
    }

    public GameActions receiveGameaction(String gameActionString) {

        Gson gson = new Gson();

        GameActions gameAction = gson.fromJson(gameActionString, GameActions.class);

        return gameAction;
    }

      /*  public String toJSon(Card c) {

            try {

                // convert Java Object to JSON
                //Object gets into a special formatted String

                JSONObject jsonObj = new JSONObject();

                jsonObj.put("value", c.getValue());

                jsonObj.put("color", c.getColor());


                return jsonObj.toString();


            } catch (JSONException ex) {
                ex.printStackTrace();

            }


            return null;

        }

        public Card fromJsonString (String cardString){
            try {
                JSONObject jObj = new JSONObject(cardString);
                String color = jObj.getString("color");
                String value = jObj.getString("value");



               Card fromStringconvertCard = new Card(color,value);

               return fromStringconvertCard;

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }*/



        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                switch (msg.what) {

                    case MESSAGE_READ:
                        byte[] readBuffer = (byte[]) msg.obj;
                        String tmpmsg = new String(readBuffer, 0, msg.arg1);
                        //String tmpcardString = new String(readBuffer, 0, msg.arg1);


                       // testText = tmpmsg;

                       gameAction = receiveGameaction(tmpmsg);

                       callGameController(gameAction);

                        Log.d("@handler", tmpmsg);


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


                Log.d("@serverclass", "Serverclass running");

                try {
//                    Log.d("socket",serverSocket.toString());
                    serverSocket = new ServerSocket(8888);
                    socket = serverSocket.accept();


                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("@error", "sc catched");
                }
                // sendReceive = new SendReceive(socket,serverSocket);
                sendReceive = new SendReceive(socket);

                sendReceive.start();


            }
        }

        private class SendReceive extends Thread {


            private Socket socket;
            private InputStream inputStream;
            private OutputStream outputStream;

            /* public SendReceive(Socket socket, ServerSocket serverSocket) {



                 this.socket = socket;
                 try {
                     socket.connect(serverSocket.getLocalSocketAddress());
                 } catch (IOException e) {
                     e.printStackTrace();
                 }


                 try {
                     Log.d("@sendreceive","sr created1");

                     this.inputStream = socket.getInputStream();
                     this.outputStream = socket.getOutputStream();

                 } catch (IOException e) {
                     e.printStackTrace();
                 }

             }*/
            public SendReceive(Socket socket) {


                this.socket = socket;

                try {
                    Log.d("@sendreceive", "sr created2");

                    this.inputStream = socket.getInputStream();
                    this.outputStream = socket.getOutputStream();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void run() {

                Log.d("@sendreceive", "sr running2");


                byte[] buffer = new byte[1024];
                int bytes;

                while (socket != null) {

                    try {
                        bytes = inputStream.read(buffer);
                        if (bytes > 0) {

                            handler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }


            }


            public void write(byte[] bytes) {

                try {

                    Log.d("@write", bytes.toString());

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

                Log.d("@clientclass", hostAddress);
                hostAdd = hostAddress;
                socket = new Socket();
            }

            @Override
            public void run() {
                super.run();
                try {

                    Log.d("socket", socket.toString());
                    socket.connect(new InetSocketAddress(hostAdd, 8888), 500);


                } catch (IOException e) {
                    e.printStackTrace();
                }

                sendReceive = new SendReceive(socket);
                sendReceive.start();
            }
        }

        void callGameController (GameActions action) {
            switch (action.action) {
                case DRAW_CARD:
                    GC.drawCard(action.playerID);
                    break;
                case DROP_CARD:
                    GC.dropCard(action.playerID);
                    break;
                case TRADE_CARD:
                    //GC.tradeCard();
                    break;
                case PLAY_CARD:
                    GC.playCard(action.playerID,action.card);
                    break;
                case WISH_COLOR:
                    GC.colorWish(action.playerID,action.colorWish);
                    break;
            }
        }
    }
