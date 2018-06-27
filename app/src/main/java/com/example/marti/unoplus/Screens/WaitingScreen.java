package com.example.marti.unoplus.Screens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.marti.unoplus.R;

import static com.example.marti.unoplus.Screens.NameScreen.PLAYER_NAME;

public class WaitingScreen extends AppCompatActivity {
    Button buttonStart;
    String hostAdress;
    String mode;
    int numClients;
    public String playername;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_waiting_screen);

        hostAdress = getIntent().getStringExtra("adress");
        mode = getIntent().getStringExtra("mode");
        numClients = getIntent().getIntExtra("numofclients",1);

        progressBar = findViewById(R.id.progressBar2);
        buttonStart = findViewById(R.id.buttonStart);

        buttonStart.postDelayed(new Runnable(){
            @Override
            public void run(){
                progressBar.setVisibility(View.INVISIBLE);
                buttonStart.setVisibility(View.VISIBLE);
            }
        }, 5000);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(WaitingScreen.this, GameViewProt.class);
                i.putExtra("mode", mode);
                i.putExtra("adress", hostAdress);
                i.putExtra("numofclients",numClients);
                i.putExtra(PLAYER_NAME, playername);
                startActivity(i);
            }
        });
    }


/*public  void run (){
    Thread waitingscreen = new Thread(){
        public void run() {
            try {
                sleep(3000);
                Intent i = new Intent(WaitingScreen.this, GameViewProt.class);
                i.putExtra("mode", mode);
                i.putExtra("adress", hostAdress);
                i.putExtra("numofclients",numClients);
                i.putExtra(PLAYER_NAME, playername);
                startActivity(i);
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    waitingscreen.start();
}*/

}
