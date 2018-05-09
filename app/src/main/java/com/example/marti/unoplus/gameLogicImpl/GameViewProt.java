package com.example.marti.unoplus.gameLogicImpl;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.marti.unoplus.Client.HandCardView;
import com.example.marti.unoplus.Client.PlayedCardView;
import com.example.marti.unoplus.GameActions;
import com.example.marti.unoplus.GameStatics;
import com.example.marti.unoplus.R;
import com.example.marti.unoplus.cards.Card;
import com.example.marti.unoplus.cards.CardView;
import com.example.marti.unoplus.cards.Deck;
import com.example.marti.unoplus.players.Player;
import com.example.marti.unoplus.players.PlayerList;
import com.example.marti.unoplus.sound.Sounds;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jop.hab.net.NetworkIOManager;
import jop.hab.net.ObserverInterface;

public class GameViewProt extends AppCompatActivity implements ObserverInterface{

    PlayerList players;//reference to all Players in the Game
    Deck deck;              //reference to the Deck that is used
    GameLogic logic;        //reference to the GameLogic
    NetworkIOManager NIOmanager;
    int startingHand = 7;//Amount of Cards every Player gets at the start of the Game
    float turnTime;         //Turn Timer for the Game
    public GameActions gA;         //Object that gets send to all Players
    boolean[] calledUNO;    //
    boolean[] dropedCard;   //
    boolean[] tradedCard;   //
    String hostAdress;
    String mode;
    GameActions recievedGA;
    boolean isGameController =false;
    GameController gameController;
    Player player;
    HandCardView hcv;
    ArrayList<HandCardView> handCards = null;
    PlayedCardView playedCardView = null;


    public GameViewProt (){
        super();
        this.handCards = new ArrayList<HandCardView>();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.game_screen);

        hostAdress = getIntent().getStringExtra("adress");
        mode = getIntent().getStringExtra("mode");

        NIOmanager = new NetworkIOManager(this);
        NIOmanager.setMode(mode);
        NIOmanager.setHostAdress(hostAdress);
        NIOmanager.open();

        GameStatics.currentActivity = this;
        GameStatics.Initialize(true);






        //Hier werden die IP und der Modus über den Intent aus der MainActivityTest abgefragt



        if(mode.equals("server")){

            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            isGameController =true;
            gameController= new GameController(this);

            PlayerList pl = new PlayerList();
            ArrayList l = new ArrayList<Player>();
            Player temp = new Player(this,0);
            player=temp;
            l.add(temp);
            temp = new Player(this, 1);
            l.add(temp);

            Log.d("time","before ga");
            NIOmanager.writeGameaction(new GameActions(GameActions.actions.TRADE_CARD,temp.getID(),true));
            Log.d("time","after ga");
            pl.setPlayers(l);
            gameController.setPlayerList(pl);
            gameController.setUpGame();


        }else{

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



        //GameActions g = new GameActions(GameActions.actions.UPDATE,new Card(Card.colors.BLUE, Card.values.EIGHT),0);

        //NIOmanager.writeGameaction(g);
        //verschoben in NIOReady Call ;-)
        //setUpGame();

    }


    //Method to Update all Players after GC and GL have finished
    public void updateAllPlayers(GameActions gA) {

        Log.d("Time", "updateAllPLayrs will schon was vom NIO");

        NIOmanager.writeGameaction(gA);





    }


    //distripiutung game actions
    void callGameController(GameActions action) {

        if (!action.gcSend&&isGameController){
            switch (action.action) {
                case DRAW_CARD:
                    gameController.drawCard(action.playerID);
                    break;
                case DROP_CARD:
                    gameController.dropCard(action.playerID);
                    break;
                case TRADE_CARD:
                    //GC.tradeCard();
                    break;
                case PLAY_CARD:
                    gameController.playCard(action.playerID, action.card);
                    break;
                case WISH_COLOR:
                    gameController.colorWish(action.playerID, action.colorWish);
                    break;
            }
        } else {
            Log.d("player","callplayer");
                player.callPlayer(action);


                drawUpdate();


        }
    }

    @Override
    public void onFinish() {
        Card.colors rndcolor = GameStatics.randomEnum(Card.colors.class);
        Card.values rndvalue = GameStatics.randomEnum(Card.values.class);
        GameStatics.net.CLIENT_GetNewCardForHand('0', new Card(rndcolor, rndvalue));

        Toast.makeText(getApplicationContext(), "ZEIT VORBEI! Karte gezogen", Toast.LENGTH_LONG).show();
        //timeUp(context);    eventuell so oder mit TOAST
        this.start();
    }

    @Override
    public void onStart() {
        super.onStart();

        this.playedCardView = new PlayedCardView(this.getApplicationContext(), this);

        this.playedCardView.updateCard(new Card(Card.colors.RED, Card.values.EIGHT));

        for (int i = 0; i < 7; i++) {
            Card.colors rndcolor = GameStatics.randomEnum(Card.colors.class);
            Card.values rndvalue = GameStatics.randomEnum(Card.values.class);
            GameStatics.net.CLIENT_GetNewCardForHand('0', new Card(rndcolor, rndvalue));
        }
    }

    public void UpdateCurrentPlayCard(Card card) {
        this.playedCardView.updateCard(card);
    }

    private void drawUpdate() {
        List <Card> hand= player.getHand();





        for (Card c:hand) {

            Log.d("HANDCARD",c.getValue().toString());









        }


    }

    @Override
    public void dataChanged() {
  try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }

        recievedGA =NIOmanager.getGameAction();
if(recievedGA.action.equals(GameActions.actions.TRADE_CARD)) {

    if (!isGameController && player == null) {


        player = new Player(this, recievedGA.playerID);


    }

}else {

    callGameController(recievedGA);
}

    }

    @Override
    public void NIOReady() {

    }
}
