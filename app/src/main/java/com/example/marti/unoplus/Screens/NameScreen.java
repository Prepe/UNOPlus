package com.example.marti.unoplus.Screens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.marti.unoplus.R;
import com.example.marti.unoplus.players.Player;

import jop.hab.net.ConnectionScreen;

public class NameScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_name_screen);

        findViewById(R.id.weiterButton).setOnClickListener(handler);
        findViewById(R.id.zurückButton).setOnClickListener(handler);
    }

    View.OnClickListener handler = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.weiterButton:
                    startActivity(new Intent(NameScreen.this, ConnectionScreen.class));
                    break;
                case R.id.zurückButton:
                    startActivity(new Intent(NameScreen.this, MainMenu.class));
                    break;
            }
        }
    };

}
