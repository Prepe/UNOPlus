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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marti.unoplus.GameActions;
import com.example.marti.unoplus.GameStatics;
import com.example.marti.unoplus.R;
import com.example.marti.unoplus.Screens.GameViewProt;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ConnectionScreen extends AppCompatActivity implements ObserverInterface {

    Button btnOnOff, btnDiscover, btnStart;
    ListView listView;
    TextView ConnectionStatus;

    WifiManager wifiManager;

    List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;
    public  ArrayList<String> connectedDevices = new ArrayList<>();

    IntentFilter intentFilter;
    WifiP2pManager.Channel mChannel;
    WifiP2pManager mManager;
    BroadcastReceiver mReceiver;
    String hostDeviceAddress;

    NetworkIOManager NIOManager;
    boolean ready = false;

    //Hier wird die Verbindung zwischen den geräten hergstellt. Dann wird die IP Adresse des Hosts und der Mode ("host oder server)
    //des aktuellen gerätes über einen Intent an den GameController weiter gegben.
    //siehe weiter unten Zeile 196/212

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateGameStatics();
        setUpGameView();

        setUpIntentFilter();
        setUpWiFi();

        exqListener();
    }

    void updateGameStatics() {
        GameStatics.currentActivity = this;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.test_activity_layout);
    }

    void setUpGameView() {
        btnOnOff = (Button) findViewById(R.id.onOff);
        btnDiscover = (Button) findViewById(R.id.discover);
        btnStart = (Button) findViewById(R.id.start);

        listView = (ListView) findViewById(R.id.peerListView);

        ConnectionStatus = (TextView) findViewById(R.id.connectionStatus);
    }

    void setUpIntentFilter() {
        intentFilter = new IntentFilter();

        // Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    void setUpWiFi() {
        GameStatics.wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!GameStatics.wifiManager.isWifiEnabled()) {
            GameStatics.wifiManager.setWifiEnabled(true);
            long temp = System.currentTimeMillis();
            while (System.currentTimeMillis()-temp<10000);
        }
        btnOnOff.setText("On");

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel, this);
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
                    }
                });
            }
        });
    }

    WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            InetAddress groupOwnerAdress = info.groupOwnerAddress;
            hostDeviceAddress = groupOwnerAdress.getHostAddress();

            if (info.groupFormed) {
                ConnectionStatus.setText("Connected");
                startNIO();
                NIOManager.setHostAdress(hostDeviceAddress);
                NIOManager.open();

                btnStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!ready) {
                            NIOManager.writeGameaction(new GameActions(GameActions.actions.INIT_PLAYER, 0, true));
                            ready = true;
                        }
                    }
                });
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
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

    public int  getNUMConnectedDevices (){
        return connectedDevices.size();
    }

    void startNIO() {
        NIOManager = new NetworkIOManager(this);
        NIOManager.setMode("client");
    }

    public void dataChanged() {
        LinkedList<GameActions> actionsToProcess = NIOManager.getGameAction();
        if (actionsToProcess == null) {
            Log.e("GVP", "Reseved Actions where NULL");
            return;
        }

        if (actionsToProcess.size() == 0) {
            Log.e("GVP", "Reseved Actions where 0");
            return;
        }

        for (int i = 0; i < actionsToProcess.size(); i++) {
            GameActions recievedGA = actionsToProcess.get(i);
            handleUpdate(recievedGA);
        }
        NIOManager.updatesProcessed();
    }

    public void NIOReady() {

    }

    void handleUpdate(GameActions action) {
        if (action.action == GameActions.actions.INIT_GAME) {
            GameStatics.NIOManager = NIOManager;
            Intent i = new Intent(getBaseContext(), GameViewProt.class);
            startActivity(i);
        }
    }
}

