package com.example.marti.unoplus.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.marti.unoplus.GameStatics;
import com.example.marti.unoplus.R;

import jop.hab.net.FullscreenActivity;
import jop.hab.net.MainActivityTest;

public class MainMenu extends AppCompatActivity {
    public MainMenu() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GameStatics.currentActivity = this;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main_menu);

        findViewById(R.id.spielerstellen).setOnClickListener(handler);
        findViewById(R.id.spielbeitreten).setOnClickListener(handler);
        findViewById(R.id.einstellungen).setOnClickListener(handler);
        findViewById(R.id.exitbutton).setOnClickListener(handler);
        findViewById(R.id.devModeButton).setOnClickListener(handler);
    }

    View.OnClickListener handler = new View.OnClickListener(){
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.spielerstellen:
                    startActivity(new Intent(MainMenu.this, LobbyScreen.class));
                    break;

                case R.id.spielbeitreten:
                    startActivity(new Intent(MainMenu.this, MainActivityTest.class));
                    break;

                case R.id.einstellungen:
                    startActivity(new Intent(MainMenu.this, Settings.class));
                    break;

                case R.id.exitbutton:
                    System.exit(0);
                    break;

                case R.id.devModeButton:
                    GameStatics.devMode = true;
                    startActivity(new Intent(MainMenu.this, GameScreen.class));
                    break;
            }
        }
    };
}
