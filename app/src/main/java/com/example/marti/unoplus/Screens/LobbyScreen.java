package com.example.marti.unoplus.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.marti.unoplus.GameStatics;
import com.example.marti.unoplus.R;
import com.example.marti.unoplus.gameLogicImpl.GameViewProt;


public class LobbyScreen extends AppCompatActivity {

    public LobbyScreen() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GameStatics.currentActivity = this;

        //Initialize network components
        GameStatics.Initialize(false); //TODO : Determine how server does it.

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.lobby_screen);


        Button button= findViewById(R.id.verbindenbutton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LobbyScreen.this, GameViewProt.class));

                /*
                boolean kt = false;
                boolean kw = false;
                boolean tt = false;
                CheckBox kartentauschen = (CheckBox) findViewById(R.id.kartentauschen);
                CheckBox kartenwegwerfen = (CheckBox) findViewById(R.id.kartenwegwerfen);
                CheckBox tischteufel = (CheckBox) findViewById(R.id.tischteufel);
                */

                /*
                TODO: Die eingestellten Spieloptionen/Schummelfunktionen müssen noch im GC implementiert/beachtet und diesem übergeben werden.
                 */
                /*
                if(kartentauschen.isChecked()){
                    kt = true;
                }
                if(kartenwegwerfen.isChecked()){
                    kw = true;
                }
                if(tischteufel.isChecked()){
                    tt = true;
                }

                Deck Deck = new Deck();

                PlayerList PlayerList = new PlayerList();
                ArrayList<PlayerList> players = new ArrayList<>();

                GameLogic GameLogic = new GameLogic(PlayerList, Deck);
                GameController GameController = new GameController(PlayerList, Deck, GameLogic);
                */

            }
        });
    }
}