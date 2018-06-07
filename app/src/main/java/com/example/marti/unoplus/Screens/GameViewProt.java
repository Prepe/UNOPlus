package com.example.marti.unoplus.Screens;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.os.Vibrator;
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
import com.example.marti.unoplus.cards.HandCardView;
import com.example.marti.unoplus.cards.PlayedCardView;
import com.example.marti.unoplus.cards.ThrowAwayView;
import com.example.marti.unoplus.gameLogicImpl.GameController;
import com.example.marti.unoplus.players.Player;
import com.example.marti.unoplus.players.PlayerList;
import com.example.marti.unoplus.sound.SoundManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import jop.hab.net.NetworkIOManager;
import jop.hab.net.ObserverInterface;

public class GameViewProt extends AppCompatActivity implements ObserverInterface {
    NetworkIOManager NIOmanager;
    String hostAdress;
    String mode;
    int numClients;
    boolean isGameController = false;
    GameController gameController;
    public Player player;
    GameActions recievedGA;
    ArrayList<HandCardView> handCards;
    PlayedCardView playedCardView;
    ThrowAwayView throwAwayView;
    Button buttongetcard;
    TextView numCards;
    TextView numCards2;
    SoundManager soundManager;
    public CountDownTimer timer;
    Button unoButton;
    Vibrator vibrator;
    LinkedList<Player> tempPlayers;
    int playerCount;

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
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //Hier werden die IP und der Modus über den Intent aus der ConnectionScreen abgefragt
        hostAdress = getIntent().getStringExtra("adress");
        mode = getIntent().getStringExtra("mode");
        numClients = getIntent().getIntExtra("numofclients", 1);

        NIOmanager = new NetworkIOManager(this);
        NIOmanager.setMode(mode);
        NIOmanager.setHostAdress(hostAdress);
        NIOmanager.setNumclients(numClients);
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
        for (int i = 1; i <= 2; i++) {
            playersSS.add("Player " + i);

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

        final TextView myCounter = findViewById(R.id.countdown);
        timer = new CountDownTimer(20000, 1000) {

            @Override
            public void onFinish() {
                player.drawCardIfTimesUp();

                Toast.makeText(getApplicationContext(), "ZEIT VORBEI! Karte gezogen", Toast.LENGTH_LONG).show();
                //timeUp(context);    eventuell so oder mit TOAST
                timer.cancel();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                myCounter.setText("Verbleibende Zeit: " + String.valueOf(millisUntilFinished / 1000));
            }
        };
    }

    @Override
    public void dataChanged() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }

        recievedGA = NIOmanager.getGameAction();

        TextView tv = (TextView) findViewById(R.id.netmessage);
        tv.setText(recievedGA.action.toString());
        Log.d("GCP_Action", recievedGA.action.toString());
        //TODO change placeholder player ID
        if (specialUpdate(recievedGA)) {
            Log.d("GCP_Action", recievedGA.action.toString());
        } else {
            handleUpdate(recievedGA);
        }

    }

    boolean specialUpdate(GameActions action) {
        Log.d("GVP_UPDATE", "specialUpdate: " + action.action.name());
        if (action.action.equals(GameActions.actions.INIT_PLAYER)) {
            if (!isGameController) {
                if (action.check && action.playerID == player.getID()) {
                    if (action.nextPlayerID != 0) {
                        Log.d("CLIENT", "Setting new ID");
                        player.setID(action.nextPlayerID);
                    }
                } else {
                    Log.d("CLIENT", "Asking for new ID");
                    NIOmanager.writeGameaction(new GameActions(GameActions.actions.INIT_PLAYER, player.getID(), 0, true));
                }
            } else if (action.playerID != 0 && action.nextPlayerID == 0){
                Log.d("HOST", "Give Player (ID: " + action.playerID + ") a new ID");
                gameInit(action.playerID);
            }

            return true;
        }

        if (action.action.equals(GameActions.actions.GAME_FINISH)) {
            toastGameFinished(action.playerID);

            return true;
        }

        return false;
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
        } else if (action.gcSend) {
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
        if (screenOn != null) {
            screenOn.setKeepScreenOn(true);
        }

        this.playedCardView = new PlayedCardView(this.getApplicationContext(), this);
        this.playedCardView.updateCard(null);

        this.throwAwayView = new ThrowAwayView(this.getApplicationContext(), this);


        if (mode.equals("server")) {

            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            isGameController = true;
            gameController = new GameController(this);

            tempPlayers = new LinkedList<>();
            player = new Player(0);
            player.setGV(this);
            tempPlayers.add(player);

            playerCount = numClients;

            NIOmanager.writeGameaction(new GameActions(GameActions.actions.INIT_PLAYER, 0, 0, false));

        } else {
            Log.d ("CLIENT", "Generating tempID");
            double random = Math.random() * 10;
            double tempID = 1;
            for (int i = 0; i < random; i++) {
                double rand = Math.random()*1000;
                tempID += random * rand;
                try {
                    Thread.sleep((int)tempID%5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            player = new Player((int)tempID*10000);
            player.setGV(this);
            Log.d("CLIENT", "tempID: " + tempID);

            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    void gameInit(int tempID) {
        int nextID = tempPlayers.getLast().getID() + 1;
        Player temp = new Player(nextID);
        tempPlayers.add(temp);
        NIOmanager.writeGameaction(new GameActions(GameActions.actions.INIT_PLAYER, tempID, nextID, true));
        Log.d("PLAYER_SETUP", "Added new Player: " + temp.getID());

        if (nextID == playerCount - 1) {
            PlayerList pl = new PlayerList();
            pl.setPlayers(tempPlayers);
            tempPlayers.clear();

            Log.d("PLAYER_SETUP", "Setup Playerlist finished");
            gameSetUp(pl);
        }
    }

    void gameSetUp(PlayerList pl) {
        gameController.setPlayerList(pl);
        GameActions temp1 = new GameActions(GameActions.actions.INIT_GAME, pl.playerCount());
        temp1.gcSend = true;
        NIOmanager.writeGameaction(temp1);
        handleUpdate(temp1);

        gameController.setUpGame();
    }

    public int playerCountTest(int sizePL) {
        return sizePL;
    }

    View.OnClickListener handler = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttongetcard:
                    player.drawCard();
                    break;

                case R.id.unounobutton:
                    Toast.makeText(getApplicationContext(), "Uno!!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //<--------- View Updates --------->
    //Player sends an action
    public void writeNetMessage(GameActions action) {
        Log.d("player", "playeraction");
        this.NIOmanager.writeGameaction(action);
        handleUpdate(action);
    }

    //Update the view to show the last played card
    public void updateCurrentPlayCard(Card card) {
        Log.d("PVG", card.getColor().toString());
        this.playedCardView.updateCard(card);
    }

    //Update View to show current handCard counters
    public void updateCountersInView() {
        int[] hcc = player.getHandcardcounter();

        numCards.setText("( " + hcc[0] + " )");
        numCards2.setText("( " + hcc[1] + " )");
    }

    public void handChanged(LinkedList<Card> hand) {
        Log.d("Handkarten", hand.size() + "");

        //Clear Hand
        LinearLayout handBox = findViewById(R.id.playerHandLayout);
        handBox.removeAllViews();
        handCards.clear();

        Card card;
        HandCardView cardview;
        for (int i = 0; i < hand.size(); i++) {
            card = hand.get(i);
            cardview = new HandCardView(GameViewProt.this, this, card);
            handCards.add(cardview);
            handBox.addView(cardview.view);
        }
    }

    //Visualy add Cards to player hand
    public void addCardToHand(Card card) {
        //soundManager.playSound(Sounds.DRAWCARD);
        HandCardView cardview = new HandCardView(GameViewProt.this, this, card);
        this.handCards.add(cardview);

        LinearLayout handBox = findViewById(R.id.playerHandLayout);
        handBox.addView(cardview.view);

        /*// gets the handCardViews
        int cardViewsCount = handBox.getChildCount();
        View[] cardViews = new View[cardViewsCount];

        for(int i = 0; i < cardViewsCount; i++)
            cardViews[i] = handBox.getChildAt(i);

        //sorts the cardViews array
        Arrays.sort(cardViews, new Comparator<View>() {
            @Override
            public int compare(View card1, View card2) {
                HandCardView Card1 = (HandCardView) card1.getTag();
                HandCardView Card2 = (HandCardView) card2.getTag();
                return Card1.compareTo(Card2);
            }
        });

        //after sorting remove all handCards and add sorted handCardViews
        handBox.removeAllViews();

        for(int  i = 0; i < cardViewsCount; i++)
            handBox.addView(cardViews[i]);
            */
    }

    public void deleteViews() {

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
                timer.cancel();

                return;
            }
        }
        Log.d("GameDebug", "Didn't find it.... fix plz");
    }

    //Method to choose color when special card is played
    public void chooseColor() {

        Dialog d = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
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

    void endGame() {
        startActivity(new Intent(this, MainMenu.class));
    }

    //<---------- Toasts ---------->
    public void toastYourTurn() {
        Toast.makeText(getApplicationContext(), "Du bist am Zug", Toast.LENGTH_SHORT).show();
        vibrator.vibrate(500);
        timer.start();
    }

    public void toastWrongCard() {
        Toast.makeText(getApplicationContext(), "Konnte Karte nicht spielen!", Toast.LENGTH_SHORT).show();
    }

    public void toastGameFinished(int pID) {
        Log.d("GAME_END", "Sieger ist Spieler " + (pID + 1) + " mit der ID: " + pID);

        String text;

        if (player.getID() == pID) {
            Intent intent = new Intent(getApplicationContext(), WinnerScreen.class);
            intent.putExtra("pID", pID + 1);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getApplicationContext(), LosingScreen.class);
            intent.putExtra("pID", pID + 1);
            startActivity(intent);
        }

        /*
        Dialog d = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT)
                .setTitle(text)
                .setItems(new String[]{"Ende"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dlg, int position) {
                        if (position == 0) {
                            endGame();
                            dlg.cancel();
                        }
                    }
                })
                .create();
        d.setCanceledOnTouchOutside(false);
        d.show();
        */
    }
}