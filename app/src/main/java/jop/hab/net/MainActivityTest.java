package jop.hab.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marti.unoplus.R;
import com.example.marti.unoplus.gameLogicImpl.GameController;
import com.example.marti.unoplus.gameLogicImpl.GameViewProt;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class MainActivityTest extends AppCompatActivity {

    Button btnOnOff, btnDiscover, btnSend;
    ListView listView;
    TextView read_msg_box, ConnectionStatus;
    EditText writeMsg;
    WifiManager wifiManager;

    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;

    List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;


    static final int MESSAGE_READ = 1;


//Hier wird die Verbindung zwischen den geräten hergstellt. Dann wird die IP Adresse des Hosts und der Mode ("host oder server)
    //des aktuellen gerätes über einen Intent an den GameController weiter gegben.
    //siehe weiter unten Zeile 196/212


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_layout);

        initialWork();
        exqListener();

    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {

                case MESSAGE_READ:
                    byte[] readBuffer = (byte[]) msg.obj;
                    String tmpmsg = new String(readBuffer, 0, msg.arg1);
                    read_msg_box.setText(tmpmsg);
                    break;
            }

            return true;
        }
    });


    private void exqListener() {

        btnOnOff.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if (wifiManager.isWifiEnabled()) {

                    wifiManager.setWifiEnabled(false);
                    btnOnOff.setText("On");
                    Log.d("WIFI", "WIFI SET ON");

                } else {
                    wifiManager.setWifiEnabled(true);
                    btnOnOff.setText("Off");
                    Log.d("WIFI", "WIFI SET OFF");


                }

            }
        });

        btnDiscover.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        ConnectionStatus.setText("Discover Started");

                    }

                    @Override
                    public void onFailure(int reason) {
                        ConnectionStatus.setText("Discover Starting Failed");

                    }
                });
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final WifiP2pDevice device = deviceArray[position];
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;


                mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), "Connected to " + device.deviceName, Toast.LENGTH_SHORT).show();
                        ;

                    }

                    @Override
                    public void onFailure(int reason) {
                        Toast.makeText(getApplicationContext(), "not connected", Toast.LENGTH_SHORT).show();
                        ;

                    }
                });
            }
        });


      /*  btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = writeMsg.getText().toString();
                sendReceive.write(msg.getBytes());


            }
        });*/
    }

    WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {

            final InetAddress groupOwnerAdress = info.groupOwnerAddress;

            if (info.groupFormed && info.isGroupOwner) {

                ConnectionStatus.setText("Host");
              //  serverClass = new ServerClass();
                //serverClass.start();

                //Wenn das Gerät ein Server ist, wird die eigene IP und der String "server" an den GC weiter gegeben
                //GC wird gestartet, intent sollte jedem klar sein
                //Weiter im GC

                Intent i = new Intent(getBaseContext(),GameViewProt.class);
                i.putExtra("mode", "server");
                i.putExtra("adress", groupOwnerAdress.getHostAddress());
                startActivity(i);



            } else if (info.groupFormed) {

                ConnectionStatus.setText("Client");
             //   clientClass = new ClientClass(groupOwnerAdress);
               // clientClass.start();


                //Wenn das Gerät ein Client ist, wird die Server IP und der String "client" an den GC weiter gegeben

                //GC wird gestartet, intent sollte jedem klar sein
                //Weiter im GC
                Intent i = new Intent(getBaseContext(),GameViewProt.class);

               // Intent i = new Intent(getBaseContext(),Player.class);
                i.putExtra("mode", "client");
                i.putExtra("adress", groupOwnerAdress.getHostAddress());
                startActivity(i);

                //da hamm de serveradresse
                //Serveradresse is eigentlich das wichtige
                //gemma weiter ins game



            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    private void initialWork() {

        btnOnOff = (Button) findViewById(R.id.onOff);
        btnDiscover = (Button) findViewById(R.id.discover);

        listView = (ListView) findViewById(R.id.peerListView);

        ConnectionStatus = (TextView) findViewById(R.id.connectionStatus);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //Für WLAN ON / OFF

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel, this);

        mIntentFilter = new IntentFilter();


        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);


    }


    WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            if (!peerList.getDeviceList().equals(peers)) {

                peers.clear();
                peers.addAll(peerList.getDeviceList());

                deviceNameArray = new String[peerList.getDeviceList().size()];
                deviceArray = new WifiP2pDevice[peerList.getDeviceList().size()];
                int index = 0;

                for (WifiP2pDevice device : peerList.getDeviceList()) {
                    deviceNameArray[index] = device.deviceName;
                    deviceArray[index] = device;
                    index++;
                }

                //Erzeug Array Adapter aus deviceNameArray für ListView
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, deviceNameArray);
                listView.setAdapter(adapter);
            }

            if (peers.size() == 0) {

                Toast.makeText(getApplicationContext(), "No Devices Found", Toast.LENGTH_SHORT).show();
                return;
            }

        }
    };

/*
    public class ServerClass extends Thread {

        Socket socket;
        ServerSocket serverSocket;

        @Override
        public void run() {
            Log.d("serverrun","1");


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
            Log.d("sendrecrun","1");


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
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public class ClientClass extends Thread {

        Socket socket;
        String hostAdd;

        public ClientClass(InetAddress hostAddress) {

            hostAdd = hostAddress.getHostAddress();
            socket = new Socket();
        }

        @Override
        public void run() {
            super.run();
            Log.d("clientrun","1");
            try {
                socket.connect(new InetSocketAddress(hostAdd, 8888), 500);
                sendReceive = new SendReceive(socket);
                sendReceive.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


*/}