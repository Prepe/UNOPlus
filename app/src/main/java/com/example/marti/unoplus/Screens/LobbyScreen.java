package com.example.marti.unoplus.Screens;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.marti.unoplus.GameActions;
import com.example.marti.unoplus.GameStatics;
import com.example.marti.unoplus.R;

import java.net.InetAddress;
import java.util.LinkedList;

import jop.hab.net.NetworkIOManager;
import jop.hab.net.ObserverInterface;
import jop.hab.net.WifiDirectBroadcastReceiver;


public class LobbyScreen extends AppCompatActivity implements ObserverInterface {
    //P2P network
    IntentFilter intentFilter;
    WifiP2pManager.Channel mChannel;
    WifiP2pManager mManager;
    BroadcastReceiver mReceiver;
    String deviceAddress;

    NetworkIOManager NIOManager;
    int playerCount = 1;

    //Buttons and Options Checkboxes
    Button startGame;
    CheckBox duelCheck;
    CheckBox hotDropCheck;
    CheckBox cardSpinCheck;
    CheckBox dropCardCheck;
    CheckBox tradeCardCheck;
    CheckBox conterPlayCheck;
    CheckBox quickPlayCheck;

    public LobbyScreen() {
        startNetwork();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateGameStatics();

        setUpIntentFilter();

        setUpWiFi();
    }

    void updateGameStatics() {
        GameStatics.currentActivity = this;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.lobby_screen);
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
        if (GameStatics.wifiManager.isWifiEnabled()) {
            GameStatics.wifiManager.setWifiEnabled(false);
            GameStatics.wifiManager.setWifiEnabled(true);
        } else {
            GameStatics.wifiManager.setWifiEnabled(true);
        }

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel, this);

        startDiscover();

        createGroup();
    }

    void startDiscover() {
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Code for when the discovery initiation is successful goes here.
                // No services have actually been discovered yet, so this method
                // can often be left blank. Code for peer discovery goes in the
                // onReceive method, detailed below.
                Toast.makeText(getApplicationContext(),"Creating Lobby",Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(int reasonCode) {
                // Code for when the discovery initiation fails goes here.
                // Alert the user that something went wrong.
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startDiscover();
            }
        });
    }

    void createGroup() {
        mManager.createGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Device is ready to accept incoming connections from peers.
                Toast.makeText(getApplicationContext(), "Lobby Opend", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(getApplicationContext(), "P2P group creation failed. Retry.", Toast.LENGTH_SHORT).show();
                createGroup();
            }
        });
    }

    void startNetwork() {
        NIOManager = new NetworkIOManager(this);
        NIOManager.setMode("server");
    }

    public WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            InetAddress groupOwnerAddress = info.groupOwnerAddress;
            deviceAddress = groupOwnerAddress.getHostAddress();

            if (info.groupFormed && info.isGroupOwner) {
                NIOManager.setHostAdress(deviceAddress);
                NIOManager.open();

                startGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NIOManager.setNumclients(playerCount);
                        Intent i = new Intent(getBaseContext(), GameViewProt.class);
                        GameStatics.NIOManager = NIOManager;
                        NIOManager.writeGameaction(new GameActions(GameActions.actions.INIT_GAME,0,true));
                        startActivity(i);
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

    @Override
    public void NIOReady() {

    }

    @Override
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

    void handleUpdate(GameActions action) {
        if (action.action == GameActions.actions.INIT_PLAYER) {
            playerCount++;
            Toast.makeText(getApplicationContext(), playerCount + " Players ready", Toast.LENGTH_SHORT).show();
        }
    }
}