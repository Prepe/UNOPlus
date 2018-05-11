package com.example.marti.unoplus.gameLogicImpl;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.marti.unoplus.katiFixMe.Client.HandCardView;
import com.example.marti.unoplus.katiFixMe.Client.PlayedCardView;
import com.example.marti.unoplus.GameActions;
import com.example.marti.unoplus.GameStatics;
import com.example.marti.unoplus.R;
import com.example.marti.unoplus.cards.Deck;
import com.example.marti.unoplus.players.Player;
import com.example.marti.unoplus.players.PlayerList;

import java.util.ArrayList;

import jop.hab.net.NetworkIOManager;
import jop.hab.net.ObserverInterface;

public class GameViewProt extends AppCompatActivity implements ObserverInterface {

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
    boolean isGameController = false;
    GameController gameController;
    Player player;
    HandCardView hcv;
    ArrayList<HandCardView> handCards = null;
    PlayedCardView playedCardView = null;
    TextView numCards;
    CountDownTimer timer;
    Button devButton;


    public GameViewProt() {
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
/*
        final TextView myCounter = findViewById(R.id.countdown);
        timer = new CountDownTimer(20000, 1000) {

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
            public void onTick(long millisUntilFinished) {
                myCounter.setText("Verbleibende Zeit: " + String.valueOf(millisUntilFinished / 1000));
            }
        }.start();

        View.OnClickListener handler = new View.OnClickListener() {
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.unounobutton:
                        Toast.makeText(getApplicationContext(), "Uno!!", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.devGetCard:
                        Card.colors rndcolor = GameStatics.randomEnum(Card.colors.class);
                        Card.values rndvalue = GameStatics.randomEnum(Card.values.class);
                        GameStatics.net.CLIENT_GetNewCardForHand('0', new Card(rndcolor, rndvalue));
                        break;
                }
            }
        };


*/


        //Hier werden die IP und der Modus über den Intent aus der MainActivityTest abgefragt


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


        //GameActions g = new GameActions(GameActions.actions.UPDATE,new Card(Card.colors.BLUE, Card.values.EIGHT),0);

        //NIOmanager.writeGameaction(g);
        //verschoben in NIOReady Call ;-)
        //setUpGame();
/*
        if (GameStatics.devMode) {
            devButton = findViewById(R.id.devGetCard);
            devButton.setOnClickListener(handler);
            GameStatics.net = new UnoPlusNetwork(true);
        }
*/
    }

    /*

        //Method to Update all Players after GC and GL have finished
        public void updateAllPlayers(GameActions gA) {

            Log.d("Time", "updateAllPLayrs will schon was vom NIO");

            NIOmanager.writeGameaction(gA);





        }

    */
    //distripiutung game actions
    void callGameController(GameActions action) {

        if (!action.gcSend && isGameController) {
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
            Log.d("player", "callplayer");
            player.callPlayer(action);


            //drawUpdate();
//hallo

        }
    }

    /*
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
    */
    @Override
    public void dataChanged() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }

        recievedGA = NIOmanager.getGameAction();
        if (recievedGA.action.equals(GameActions.actions.TRADE_CARD)) {

            if (!isGameController && player == null) {


                player = new Player(this, recievedGA.playerID);


            }

        } else {

            callGameController(recievedGA);
        }

    }

    @Override
    public void NIOReady() {

    }
/*
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
                timer.start();

                int numCardshand = 0;
                for (int i = 0; i < handCards.size(); i++) {

                    numCardshand++;
                }

                String s = Integer.toString(numCardshand);
                System.out.println(numCardshand);
                numCards.setText("( " + s + " )");

                return;
            }
        }
        Log.d("GameDebug", "Didn't find it.... fix plz");


    }
    */
}
