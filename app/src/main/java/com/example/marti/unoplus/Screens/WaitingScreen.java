package com.example.marti.unoplus.Screens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.marti.unoplus.R;
import com.example.marti.unoplus.gameLogicImpl.GameViewProt;

import jop.hab.net.ConnectionScreen;

public class WaitingScreen extends AppCompatActivity {
    Button verbindenbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_waiting_screen);

        verbindenbutton = findViewById(R.id.verbindenbutton);
    }

    View.OnClickListener handler = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.verbindenbutton:
                    Intent i = new Intent(WaitingScreen.this, GameViewProt.class);
                    startActivity(i);
                    break;
            }
        }
    };

}
