package com.example.marti.unoplus.gameLogicImpl;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marti.unoplus.GameActions;
import com.example.marti.unoplus.GameStatics;
import com.example.marti.unoplus.R;
import com.example.marti.unoplus.cards.Card;
import com.example.marti.unoplus.cards.Deck;
import com.example.marti.unoplus.cards.HandCardView;
import com.example.marti.unoplus.cards.PlayedCardView;
import com.example.marti.unoplus.players.Player;
import com.example.marti.unoplus.players.PlayerList;
import com.example.marti.unoplus.sound.SoundManager;
import com.example.marti.unoplus.sound.Sounds;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jop.hab.net.NetworkIOManager;
import jop.hab.net.ObserverInterface;

import static com.example.marti.unoplus.GameActions.actions.DRAW_CARD;
import static com.example.marti.unoplus.GameActions.actions.DROP_CARD;

public class GameViewProt extends AppCompatActivity implements ObserverInterface {
    NetworkIOManager NIOmanager;
    String hostAdress;
    String mode;
    boolean isGameController = false;
    GameController gameController;
    public Player player;
    GameActions recievedGA;
    ArrayList<HandCardView> handCards;
    PlayedCardView playedCardView;
    Button buttongetcard;
    TextView numCards;
    TextView numCards2;
    SoundManager soundManager;
    CountDownTimer timer;
    List<Card> card = new LinkedList<>();
    Button unoButton;
    public PlayerList playerList;


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

        numCards = (TextView) findViewById(R.id.numCards1);
        numCards2 = (TextView) findViewById(R.id.numCards2);

        //Hier werden die IP und der Modus über den Intent aus der ConnectionScreen abgefragt
        hostAdress = getIntent().getStringExtra("adress");
        mode = getIntent().getStringExtra("mode");

        NIOmanager = new NetworkIOManager(this);
        NIOmanager.setMode(mode);
        NIOmanager.setHostAdress(hostAdress);
        NIOmanager.open();

        GameStatics.currentActivity = this;
        GameStatics.Initialize(true);

        //der NIOManager wird instanziert und die Parameter werden übergeben
        //Der NIO kann daten schreiben und empfangen.
        //Empfangen funktioniert automatisch über die dataChanged Mehtode..siehe unten
        //Schreiben der Daten geht über writeGameAction()... kann alles belieben geändert/erweitert werden

        findViewById(R.id.buttongetcard).setOnClickListener(handler);

        unoButton = findViewById(R.id.unounobutton);
        unoButton.setOnClickListener(handler);
        ArrayList<String> playersSS = new ArrayList<>();

        //int playerSize = playerList.playerCount();
        int plsize = 0;
        plsize = playerCountTest(plsize);
        for(int i = 1; i <= 2; i++ ){
            playersSS.add("Player "+i);

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, playersSS);

        ListView lv = findViewById(R.id.list);
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                final TextView mTextView = (TextView) view;
                switch (position) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "Player 1", Toast.LENGTH_SHORT).show();
                        //TO DO

                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "Player 2", Toast.LENGTH_SHORT).show();
                        //TO DO
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "Player 3", Toast.LENGTH_SHORT).show();
                        //TO DO
                        break;
                    case 3:
                        Toast.makeText(getApplicationContext(), "Plöayer 4", Toast.LENGTH_SHORT).show();
                        //TO DO
                        break;
                    default:
                        // Nothing do!
                }

            }
        });
    }

    @Override
    public void dataChanged() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }

        recievedGA = NIOmanager.getGameAction();

        //TODO change placeholder player ID
        if (recievedGA.action.equals(GameActions.actions.TRADE_CARD)) {
            if (!isGameController && player.getID() == null) {
                player.setID(recievedGA.playerID);
            }
        } else {
            handleUpdate(recievedGA);
        }

    }

    @Override
    public void NIOReady() {

    }

    //Method to Update all Players after GC and GL have finished
    public void updateAllConnected(GameActions gA) {

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


    @Override
    public void onStart() {
        super.onStart();

        //Let the screen be always on.
        //No sleep mode during gameplay.
        ImageView screenOn = this.findViewById(R.id.unostack);
       if(screenOn != null) {
           screenOn.setKeepScreenOn(true);
       }


        if (mode.equals("server")) {
            // this.player.createDummyCards();
        }
        this.playedCardView = new PlayedCardView(this.getApplicationContext(), this);
        this.playedCardView.updateCard(null);


        if (mode.equals("server")) {

            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            isGameController = true;
            gameController = new GameController(this);

            PlayerList pl = new PlayerList();
            LinkedList<Player> l = new LinkedList<>();
            player = new Player(0);
            player.setGV(this);
            l.add(player);

            Player temp = new Player(1);
            l.add(temp);
            Log.d("time", "before ga");
            NIOmanager.writeGameaction(new GameActions(GameActions.actions.TRADE_CARD, temp.getID(), true));
            Log.d("time", "after ga");

            Log.d("GC Playerlist", "onStart: ");
            pl.setPlayers(l);
            gameController.setPlayerList(pl);
            GameActions temp1 = new GameActions(GameActions.actions.INIT_GAME, pl.playerCount() );
            temp1.gcSend = true;
            NIOmanager.writeGameaction(temp1);
            handleUpdate(temp1);

            gameController.setUpGame();
            int sizePL = l.size();

        } else {
            player = new Player(null);
            player.setGV(this);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }

    public int playerCountTest (int sizePL) {
        return sizePL;
    }

    View.OnClickListener handler = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.buttongetcard:
                    //List<Card> card = new LinkedList<>();
                    GameActions gA = new GameActions(GameActions.actions.DRAW_CARD, player.getID());
                    NIOmanager.writeGameaction(gA);
                    handleUpdate(gA);
                    break;

                case R.id.unounobutton:
                    Toast.makeText(getApplicationContext(), "Uno!!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //<--------- View Updates --------->
    //Player sends an action
    public void writeNetMessage(GameActions action)
    {
        Log.d("player","playeraction");
        this.NIOmanager.writeGameaction(action);
        handleUpdate(action);
    }

    //Update the view to show the last played card
    public void updateCurrentPlayCard(Card card)
    {
        Log.d("PVG", card.getColor().toString());
            this.playedCardView.updateCard(card);
    }

    public void updateCountersInView(){
        int[] hcc = player.getHandcardcounter();

        numCards.setText(hcc[0]+"");
        numCards2.setText(hcc[1]+"");
    }

    //Visualy add Cards to player hand
    public void addCardToHand(Card card) {
        //soundManager.playSound(Sounds.DRAWCARD);
        HandCardView cardview = new HandCardView(GameViewProt.this, this, card);
        this.handCards.add(cardview);

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

                return;
            }
        }
        Log.d("GameDebug", "Didn't find it.... fix plz");
    }

    //Method to choose color when special card is played
    public void chooseColor() {

        Dialog d = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT)
                .setTitle("Such eine Farbe aus!")
                .setItems(new String[]{"Rot", "Blau", "Gelb", "Grün"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dlg, int position) {
                        if (position == 0) {
                            writeNetMessage(new GameActions(GameActions.actions.WISH_COLOR, player.getID(), Card.colors.RED));
                            dlg.cancel();
                        } else if (position == 1) {
                            writeNetMessage(new GameActions(GameActions.actions.WISH_COLOR, player.getID(), Card.colors.BLUE));
                            dlg.cancel();
                        } else if (position == 2) {
                            writeNetMessage(new GameActions(GameActions.actions.WISH_COLOR, player.getID(), Card.colors.YELLOW));
                            dlg.cancel();
                        } else if (position == 3) {
                            writeNetMessage(new GameActions(GameActions.actions.WISH_COLOR, player.getID(), Card.colors.GREEN));
                            dlg.cancel();
                        }
                    }
                })
                .create();
        d.setCanceledOnTouchOutside(false);
        d.show();

    }

}