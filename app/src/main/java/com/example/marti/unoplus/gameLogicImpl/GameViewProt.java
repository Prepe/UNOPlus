package com.example.marti.unoplus.gameLogicImpl;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.marti.unoplus.GameActions;
import com.example.marti.unoplus.GameStatics;
import com.example.marti.unoplus.R;
import com.example.marti.unoplus.cards.Card;
import com.example.marti.unoplus.cards.HandCardView;
import com.example.marti.unoplus.cards.PlayedCardView;
import com.example.marti.unoplus.players.Player;
import com.example.marti.unoplus.players.PlayerList;

import java.util.ArrayList;

import jop.hab.net.NetworkIOManager;
import jop.hab.net.ObserverInterface;

public class GameViewProt extends AppCompatActivity implements ObserverInterface {
    NetworkIOManager NIOmanager;
    String hostAdress;
    String mode;
    boolean isGameController = false;
    GameController gameController;
    Player player;
    GameActions recievedGA;
    ArrayList<HandCardView> handCards;
    PlayedCardView playedCardView;

    public GameViewProt() {
        super();
        this.handCards = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.game_screen);

        //Hier werden die IP und der Modus über den Intent aus der ConnectionScreen abgefragt
        hostAdress = getIntent().getStringExtra("adress");
        mode = getIntent().getStringExtra("mode");

        NIOmanager = new NetworkIOManager(this);
        NIOmanager.setMode(mode);
        NIOmanager.setHostAdress(hostAdress);
        NIOmanager.open();

        GameStatics.currentActivity = this;
        GameStatics.Initialize(true);


        if (mode.equals("server")) {

            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            isGameController = true;
            gameController = new GameController(this);

            PlayerList pl = new PlayerList();
            ArrayList l = new ArrayList<Player>();
            Player temp = new Player(this, 0);
            player = temp;
            l.add(temp);
            temp = new Player(this, 1);
            l.add(temp);

            Log.d("time", "before ga");
            NIOmanager.writeGameaction(new GameActions(GameActions.actions.TRADE_CARD, temp.getID(), true));
            Log.d("time", "after ga");

            pl.setPlayers(l);
            gameController.setPlayerList(pl);
            gameController.setUpGame();


        } else {

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //der NIOManager wird instanziert und die Parameter werden übergeben
        //Der NIO kann daten schreiben und empfangen.
        //Empfangen funktioniert automatisch über die dataChanged Mehtode..siehe unten
        //Schreiben der Daten geht über writeGameAction()... kann alles belieben geändert/erweitert werden
    }

    @Override
    public void dataChanged() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }

        recievedGA = NIOmanager.getGameAction();

        //TODO change placeholder player ID
        if (recievedGA.action.equals(GameActions.actions.TRADE_CARD)) {
            if (!isGameController && player == null) {
                player = new Player(this, recievedGA.playerID);
            }
        } else {
            handleUpdate(recievedGA);
        }

    }

    @Override
    public void NIOReady() {

    }

    //Method to Update all Players after GC and GL have finished
    public void updateAllPlayers(GameActions gA) {

        Log.d("Time", "updateAllPLayrs will schon was vom NIO");

        NIOmanager.writeGameaction(gA);
        handleUpdate(gA);
    }

    //distripiutung game actions
    void handleUpdate(GameActions action) {

        if (!action.gcSend && isGameController) {
            gameController.callGameController(action);
        } else if (action.gcSend){
            Log.d("player", "callplayer");
            player.callPlayer(action);
        }
    }

    //<--------- View Updates --------->
    //Player sends an action
    public void writeNetMessage(GameActions action)
    {
        Log.d("player","playeraction");
        this.NIOmanager.writeGameaction(action);
    }

    //Update the view to show the last played card
    public void updateCurrentPlayCard(Card card)
    {
        this.playedCardView.updateCard(card);
    }

    //Visualy add Cards to player hand
    public void addCardToHand(Card card) {
        //soundManager.playSound(Sounds.DRAWCARD);
        HandCardView cardview = new HandCardView(GameViewProt.this, this, card);
        this.handCards.add(cardview);


        int numCardshand = 0;
        for (int i = 0; i < handCards.size(); i++) {

            numCardshand++;
        }

        String s = Integer.toString(numCardshand);
        System.out.println(numCardshand);
        //numCards.setText("( " + s + " )");


        LinearLayout handBox = findViewById(R.id.playerHandLayout);
        handBox.addView(cardview.view);
    }

    //Visualy remove Cards to player hand
    public void removeCardFromHand(Card card) {
        if (GameStatics.devMode) {
            Log.d("GameDebug", "Gamescreen tries to remove following card from hand :" + card.value.toString() + " " + card.color.toString());
        }

        LinearLayout handBox = findViewById(R.id.playerHandLayout);
        for (HandCardView c : this.handCards) {
            if (c.card.hasSameCardValueAs(card)) {
                Log.d("GameDebug", "Found it in players hand!");

                handBox.removeView(c.view);
                this.handCards.remove(c);
                //soundManager.playSound(Sounds.DROPCARD);
                //timer.start();

                int numCardshand = 0;
                for (int i = 0; i < handCards.size(); i++) {

                    numCardshand++;
                }

                String s = Integer.toString(numCardshand);
                System.out.println(numCardshand);
                //numCards.setText("( " + s + " )");

                return;
            }
        }
        Log.d("GameDebug", "Didn't find it.... fix plz");
    }

}