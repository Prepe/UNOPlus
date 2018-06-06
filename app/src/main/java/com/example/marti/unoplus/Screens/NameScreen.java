package com.example.marti.unoplus.Screens;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.marti.unoplus.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jop.hab.net.ConnectionScreen;

public class NameScreen extends AppCompatActivity {
    EditText playerName;
    public static final String PLAYER_NAME = "Spielername";
    WifiP2pManager manager;
    WifiP2pManager.Channel channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_name_screen);

        findViewById(R.id.weiterButton).setOnClickListener(handler);
        findViewById(R.id.zurückButton).setOnClickListener(handler);
        playerName = findViewById(R.id.playerName);


    }



    View.OnClickListener handler = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.weiterButton:
                    Intent i = new Intent(NameScreen.this, ConnectionScreen.class);
                    i.putExtra(PLAYER_NAME, getPlayerName());
                    startActivity(i);
                    break;
                case R.id.zurückButton:
                    startActivity(new Intent(NameScreen.this, MainMenu.class));
                    break;
            }
        }
    };

    private String getPlayerName() {
        return (playerName).getText().toString();
    }

}
