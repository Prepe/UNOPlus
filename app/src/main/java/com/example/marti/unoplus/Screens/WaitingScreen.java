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

import static java.lang.Thread.sleep;

public class WaitingScreen extends AppCompatActivity {
    Button verbindenbutton;
    String hostAdress;
    String mode;
    int numClients;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_waiting_screen);

        verbindenbutton = findViewById(R.id.verbindenbutton);
        hostAdress = getIntent().getStringExtra("adress");
        mode = getIntent().getStringExtra("mode");
        numClients = getIntent().getIntExtra("numofclients",1);



       run();
    }
public  void run (){

    Thread waitingscreen = new Thread(){
        public void run() {
            try {
                sleep(3000);
                Intent i = new Intent(WaitingScreen.this, GameViewProt.class);
                i.putExtra("mode", mode);
                i.putExtra("adress", hostAdress);
                i.putExtra("numofclients",numClients);
                startActivity(i);
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    waitingscreen.start();



}


    View.OnClickListener handler = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.verbindenbutton:

                    break;
            }
        }
    };

}
