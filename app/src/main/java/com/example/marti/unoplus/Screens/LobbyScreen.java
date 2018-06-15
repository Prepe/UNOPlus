package com.example.marti.unoplus.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import jop.hab.net.ConnectionScreen;

import com.example.marti.unoplus.GameStatics;
import com.example.marti.unoplus.R;
import static com.example.marti.unoplus.Screens.NameScreen.PLAYER_NAME;


public class LobbyScreen extends AppCompatActivity {

    String hostAdress;
    String mode;
    int numClients;
    public String playername;
    Button buttonStart;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.lobby_screen);

        hostAdress = getIntent().getStringExtra("adress");
        mode = getIntent().getStringExtra("mode");
        numClients = getIntent().getIntExtra("numofclients",1);
        playername = getIntent().getExtras().getString(NameScreen.PLAYER_NAME, "");

        Button button= findViewById(R.id.verbindenbutton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LobbyScreen.this, GameViewProt.class);
                i.putExtra("mode", mode);
                i.putExtra("adress", hostAdress);
                i.putExtra("numofclients",numClients);
                i.putExtra(PLAYER_NAME, playername);
                startActivity(i);


                boolean kt = false;
                boolean kw = false;
                boolean tt = false;
                CheckBox kartentauschen = (CheckBox) findViewById(R.id.kartentauschen);
                CheckBox kartenwegwerfen = (CheckBox) findViewById(R.id.kartenwegwerfen);
                CheckBox tischteufel = (CheckBox) findViewById(R.id.tischteufel);

                //@TODO boolean-Werte müssen noch an GameController übergeben werden
                if(kartentauschen.isChecked()){
                    kt = true;
                }
                if(kartenwegwerfen.isChecked()){
                    kw = true;
                }
                if(tischteufel.isChecked()){
                    tt = true;
                }

            }
        });
    }
}