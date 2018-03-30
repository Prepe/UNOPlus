package com.example.marti.unoplus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class Settings extends AppCompatActivity {

    public Settings() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.settings_menu);
        findViewById(R.id.zurueckbuttonmenu).setOnClickListener(handler);
        //findViewById(R.id.button_settings_1).setOnClickListener(handler);
    }

    View.OnClickListener handler = new View.OnClickListener() {
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.zurueckbuttonmenu:
                    // doStuff
                    startActivity(new Intent(Settings.this, MainMenu.class));
                    break;
            }

            }
        };

    }


