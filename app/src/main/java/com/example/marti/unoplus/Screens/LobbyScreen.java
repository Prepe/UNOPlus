package com.example.marti.unoplus.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.marti.unoplus.Deck;
import com.example.marti.unoplus.PlayerList;
import com.example.marti.unoplus.R;
import com.example.marti.unoplus.gameLogicImpl.GameControler;
import com.example.marti.unoplus.gameLogicImpl.GameLogic;

import java.util.ArrayList;

public class LobbyScreen extends AppCompatActivity {
    boolean kt = false;
    boolean kw = false;
    boolean tt = false;
    CardView p1 = (CardView) findViewById(R.id.player1);
    CardView p2 = (CardView) findViewById(R.id.player2);
    CardView p3 = (CardView) findViewById(R.id.player3);
    CardView p4 = (CardView) findViewById(R.id.player4);
    CheckBox kartentauschen = (CheckBox) findViewById(R.id.kartentauschen);
    CheckBox kartenwegwerfen = (CheckBox) findViewById(R.id.kartenwegwerfen);
    CheckBox tischteufel = (CheckBox) findViewById(R.id.tischteufel);

    public LobbyScreen() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.lobby_screen);


        Button button= (Button)findViewById(R.id.verbindenbutton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LobbyScreen.this, GameScreen.class));

                /*
                TODO: Die eingestellten Spieloptionen/Schummelfunktionen müssen noch im GC implementiert/beachtet und diesem übergeben werden.
                 */
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
                Deck.decksinit();
                Deck.buildDeck();
                Deck.createNormalCards();
                Deck.createWildCards();

                PlayerList PlayerList = new PlayerList();
                ArrayList<PlayerList> players = new ArrayList<PlayerList>();
                players.add();
                PlayerList.setPlayers(players);

                GameLogic GameLogic = new GameLogic(PlayerList, Deck);
                GameControler GameControler = new GameControler(PlayerList, Deck, GameLogic);

            }
        });
    }
}