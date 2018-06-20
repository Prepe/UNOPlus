package jop.hab.net;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marti.unoplus.GameActions;
import com.example.marti.unoplus.R;
import com.example.marti.unoplus.Screens.GameViewProt;
import com.example.marti.unoplus.Screens.LobbyScreen;
import com.example.marti.unoplus.Screens.NameScreen;
import com.example.marti.unoplus.Screens.WaitingScreen;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ConnectionScreen extends AppCompatActivity {

    Button btnOnOff, btnDiscover, btnStart;
    ListView listView;
    TextView ConnectionStatus, textViewplayername;
    WifiManager wifiManager;

    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;

    List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;
    public ArrayList<String> connectedDevices = new ArrayList<>();
    public String playername;

    static final int MESSAGE_READ = 1;

    boolean hotDrop = false;
    boolean spinCard = false;
    boolean duel = false;
    boolean tradeCard = false;
    boolean dropCard = false;
    boolean quickPlay = false;
    int countDevicesconnected = 0;


//Hier wird die Verbindung zwischen den geräten hergstellt. Dann wird die IP Adresse des Hosts und der Mode ("host oder server)
    //des aktuellen gerätes über einen Intent an den GameController weiter gegben.
    //siehe weiter unten Zeile 196/212


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_layout);

        playername = getIntent().getExtras().getString(NameScreen.PLAYER_NAME, "");

        initialWork();
        exqListener();

    }


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
                        connectedDevices.add(device.deviceName);

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

    String groupOwnerAdressHost;
    WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {

            final InetAddress groupOwnerAdress = info.groupOwnerAddress;

            if (info.groupFormed && info.isGroupOwner) {

                ConnectionStatus.setText("Host");
                //  serverClass = new ServerClass();
                countDevicesconnected++;
                //serverClass.start();

                //Wenn das Gerät ein Server ist, wird die eigene IP und der String "server" an den GC weiter gegeben
                //GC wird gestartet, intent sollte jedem klar sein
                //Weiter im GC


                btnStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        groupOwnerAdressHost = groupOwnerAdress.getHostAddress();
                        selectOptionsDialog();
                        /*Intent i = new Intent(getBaseContext(), GameViewProt.class);
                        i.putExtra("mode", "server");
                        i.putExtra("adress", groupOwnerAdress.getHostAddress());
                        i.putExtra("numofclients",getNUMConnectedDevices());
                        startActivity(i);*/
                    }
                });


            } else if (info.groupFormed) {

                ConnectionStatus.setText("Client");

                //   clientClass = new ClientClass(groupOwnerAdress);
                // clientClass.start();

                //Wenn das Gerät ein Client ist, wird die Server IP und der String "client" an den GC weiter gegeben

                //GC wird gestartet, intent sollte jedem klar sein
                //Weiter im GC
                //Intent i = new Intent(getBaseContext(), GameViewProt.class);

                // Intent i = new Intent(getBaseContext(),Player.class);
                // i.putExtra("mode", "client");
                //i.putExtra("adress", groupOwnerAdress.getHostAddress());
                //startActivity(i);

                //da hamm de serveradresse
                //Serveradresse is eigentlich das wichtige
                //gemma weiter ins game

                btnStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getBaseContext(), GameViewProt.class);

                        i.putExtra("mode", "client");
                        i.putExtra("adress", groupOwnerAdress.getHostAddress());
                        i.putExtra("numofclients", getNUMConnectedDevices());
                        startActivity(i);
                    }
                });


            }

        }
    };

    public void selectOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("Wähle die Spieloptionen")
                .setMultiChoiceItems(new String[]{"Hot Drop", "Card Spin", "Duel", "Karten wegwerfen", "Karten tauschen", "Zwischenwerfen"}, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (which == 0 && isChecked) {
                                    hotDrop = true;
                                    Log.d("OPTIONS", "Hotdrop set true");
                                }
                                if (which == 1 && isChecked) {
                                    spinCard = true;
                                    Log.d("OPTIONS", "SpinCard set true");
                                }
                                if (which == 2 && isChecked) {
                                    duel = true;
                                    Log.d("OPTIONS", "Duel set true");
                                }
                                if (which == 3 && isChecked) {
                                    dropCard = true;
                                    Log.d("OPTIONS", "DropCard set true");
                                }
                                if (which == 4 && isChecked) {
                                    tradeCard = true;
                                    Log.d("OPTIONS", "TradeCard set true");
                                }
                                if (which == 5 && isChecked) {
                                    quickPlay = true;
                                    Log.d("OPTIONS", "Quick play set true");
                                }
                            }
                        })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        Intent i = new Intent(ConnectionScreen.this, GameViewProt.class);
                        i.putExtra("mode", "server");
                        i.putExtra("adress", groupOwnerAdressHost);
                        i.putExtra("numofclients", countDevicesconnected);

                        i.putExtra("HotDrop", hotDrop);
                        i.putExtra("SpinCard", spinCard);
                        i.putExtra("Duel", duel);
                        i.putExtra("DropCard", dropCard);
                        i.putExtra("TradeCard", tradeCard);
                        i.putExtra("QuickPlay", quickPlay);

                        startActivity(i);
                    }
                });
        builder.create();
        builder.show();
    }

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
        btnStart = (Button) findViewById(R.id.start);

        textViewplayername = (TextView) findViewById(R.id.playerName);
        textViewplayername.setText(playername);

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

        if (wifiManager.isWifiEnabled()) {

            wifiManager.setWifiEnabled(false);
            wifiManager.setWifiEnabled(true);

            Log.d("WIFI", "WIFI reset");
            Toast.makeText(getApplicationContext(), "Wifi enabled", Toast.LENGTH_SHORT).show();


        } else {
            wifiManager.setWifiEnabled(true);
            wifiManager.setWifiEnabled(false);
            wifiManager.setWifiEnabled(true);

            Log.d("WIFI", "WIFI reset");
            Toast.makeText(getApplicationContext(), "Wifi enabled", Toast.LENGTH_SHORT).show();


        }

        settingupName(playername);

    }

    public void settingupName(final String playername) {
        try {
            Method m = mManager.getClass().getMethod(
                    "setDeviceName",
                    new Class[]{WifiP2pManager.Channel.class, String.class, WifiP2pManager.ActionListener.class});
            m.invoke(mManager, mChannel, playername, new WifiP2pManager.ActionListener() {
                public void onSuccess() {
                    Log.d("setname", "yes!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }

                public void onFailure(int reason) {
                    //Code to be done while name change Fails
                    Log.d("setname", "fuck?????????????????????????????????????????????????????????????????????????????????????????");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public ArrayList<String> getConnectedDevices() {
        return connectedDevices;
    }

    public int getNUMConnectedDevices() {
        return connectedDevices.size();
    }
}

