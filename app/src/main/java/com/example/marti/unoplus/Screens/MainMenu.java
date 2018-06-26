package com.example.marti.unoplus.Screens;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.marti.unoplus.GameStatics;
import com.example.marti.unoplus.R;
import com.example.marti.unoplus.sound.Sounds;
import com.example.marti.unoplus.sound.SoundManager;


import jop.hab.net.ConnectionScreen;

public class MainMenu extends AppCompatActivity {
    public MainMenu() {
        super();
    }

    SoundManager soundManager;

    @Override
    protected void onPause() {
        super.onPause();
        soundManager.playSound(Sounds.THEMESTOP);

    }

    @Override
    protected void onResume() {
        super.onResume();
        soundManager.playSound(Sounds.THEMESTART);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GameStatics.wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        GameStatics.resetWiFi(false);
        GameStatics.currentActivity = this;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main_menu);

        findViewById(R.id.spielerstellen).setOnClickListener(handler);
        findViewById(R.id.spielbeitreten).setOnClickListener(handler);
        findViewById(R.id.einstellungen).setOnClickListener(handler);
        findViewById(R.id.exitbutton).setOnClickListener(handler);


        soundManager = new SoundManager(this);
        soundManager.playSound(Sounds.THEMESTART);
    }

    View.OnClickListener handler = new View.OnClickListener(){
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.spielerstellen:
                    soundManager.playSound(Sounds.THEMESTOP);
                    GameStatics.reset =  GameStatics.resetWiFi(false);
                    startActivity(new Intent(MainMenu.this, LobbyScreen.class));
                    break;
                case R.id.spielbeitreten:
                    soundManager.playSound(Sounds.THEMESTOP);
                    GameStatics.resetWiFi(false);
                    startActivity(new Intent(MainMenu.this, ConnectionScreen.class));
                    break;
                case R.id.einstellungen:
                    startActivity(new Intent(MainMenu.this, Settings.class));
                    break;
                case R.id.exitbutton:
                    soundManager.playSound(Sounds.THEMESTOP);
                    GameStatics.resetWiFi(true);
                    System.exit(0);
                    break;
            }
        }
    };
}
