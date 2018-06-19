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
import com.example.marti.unoplus.cards.TradeCardView;
import com.example.marti.unoplus.gameLogicImpl.GameController;
import com.example.marti.unoplus.players.Player;
import com.example.marti.unoplus.players.PlayerList;
import com.example.marti.unoplus.sound.SoundManager;

import java.util.ArrayList;

import java.util.LinkedList;

import jop.hab.net.NetworkIOManager;
import jop.hab.net.ObserverInterface;

public class GameViewProt extends AppCompatActivity implements ObserverInterface {
    NetworkIOManager NIOmanager;
    String hostAdress, mode, playerName;
    int numClients;
    boolean isGameController = false;
    GameController gameController;
    public Player player;
    GameActions recievedGA;
    ArrayList<HandCardView> handCards;
    PlayedCardView playedCardView;
    ThrowAwayView throwAwayView;
    TradeCardView tradeCardView;
    TextView numCards;
    TextView numCards2;
    public CountDownTimer timer;
    Button unoButton;
    Vibrator vibrator;
    ArrayList<String> playersInListView = new ArrayList<>();
    boolean buttonPressed = false;
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


        //int playerSize = playerList.playerCount();
        int plsize = 0;
        plsize = playerCountTest(plsize);
        for (int i = 1; i <= 2; i++) {
            playersInListView.add("Player " + i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_items, playersInListView);

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
                        Toast.makeText(getApplicationContext(), "Player 4", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "ZEIT VORBEI! Karte gezogen", Toast.LENGTH_LONG).show();
                timer.cancel();
                player.drawCard();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                myCounter.setText("Verbleibende Zeit: " + String.valueOf(millisUntilFinished / 1000));
            }
        };
    }

    @Override
    public void dataChanged() {
        LinkedList<GameActions> actionsToProcess = NIOmanager.getGameAction();
        if (actionsToProcess == null) {
            Log.e("GVP", "Reseved Actions where NULL");
            return;
        }

        if (actionsToProcess.size() == 0) {
            Log.e("GVP", "Reseved Actions where 0");
            return;
        }

        for (int i = 0; i < actionsToProcess.size(); i++) {
            recievedGA = actionsToProcess.get(i);
            TextView tv = (TextView) findViewById(R.id.netmessage);
            tv.setText(recievedGA.action.toString());
            Log.d("GCP_Action", recievedGA.action.toString());
            //TODO change placeholder player ID
            if (specialUpdate(recievedGA)) {
                Log.d("GCP_Action", recievedGA.action.toString());
            } else {
                handleUpdate(recievedGA);
            }
            recievedGA = null;
            NIOmanager.updatesProcessed();
        }
    }

    boolean specialUpdate(GameActions action) {
        Log.d("GVP_UPDATE", "specialUpdate: " + action.action.name());
        if (action.action.equals(GameActions.actions.INIT_PLAYER)) {
            initPlayer(action);
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
        this.tradeCardView = new TradeCardView(this.getApplicationContext(), this);

        if (mode.equals("server")) {

            try {
                Thread.sleep(2000);
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

            while (NIOmanager.isNotReady()) {
                NIOmanager.open();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Log.d("HOST", "NumPlayers: " + playerCount);
            NIOmanager.writeGameaction(new GameActions(GameActions.actions.INIT_PLAYER, 0, 0, false));

        } else {
            Log.d("CLIENT", "Generating tempID");
            double random = Math.random() * 10 + 1;
            double tempID = 1;
            for (int i = 0; i < random; i++) {
                double rand = Math.random() * 10;
                tempID += random + rand;
                try {
                    Thread.sleep((int) tempID % 5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            player = new Player((int) (tempID * 1000));
            player.setGV(this);
            Log.d("CLIENT", "tempID: " + player.getID());
        }
    }

    void initPlayer(GameActions action) {
        if (!isGameController) {
            if (action.check) {
                Log.d("CLIENT", "playerID: " + player.getID());
                Log.d("CLIENT", "ID in Action: " + action.playerID);
                if (action.playerID.equals(player.getID())) {
                    if (action.nextPlayerID > 0) {
                        Log.d("CLIENT", "Setting new ID");
                        player.setID(action.nextPlayerID);
                        return;
                    }
                    Log.d("CLIENT", "ID <= 0");
                    return;
                }
                Log.d("CLIENT", "Not my ID");
                return;
            } else {
                Log.d("CLIENT", "Asking for new ID");
                NIOmanager.writeGameaction(new GameActions(GameActions.actions.INIT_PLAYER, player.getID(), 0, true));
                return;
            }
        } else if (action.playerID != 0 && action.nextPlayerID == 0 && !action.gcSend) {
            Log.d("HOST", "Give Player (ID: " + action.playerID + ") a new ID");
            gameInit(action.playerID);
            return;
        }
        Log.d("INIT_PLAYER", "FIX ME");
        return;
    }

    void gameInit(int tempID) {
        int nextID = tempPlayers.size();
        Player temp = new Player(nextID);
        tempPlayers.add(temp);
        GameActions tempA = new GameActions(GameActions.actions.INIT_PLAYER, tempID, nextID, true);
        tempA.gcSend = true;
        NIOmanager.writeGameaction(tempA);
        Log.d("PLAYER_SETUP", "Added new Player: " + temp.getID());

        if (nextID == playerCount) {
            PlayerList pl = new PlayerList();
            pl.setPlayers(tempPlayers);

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
                    player.callUno(player.getID());
                    break;
            }
        }
    };

    public boolean getButtonPressed() {
        return this.buttonPressed;
    }

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

    public void updateForHotDrop() {
        toastStartHotDrop();
        player.timer(true);
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

    //Method to choose Player with which you want to trade a card
    public void choosePlayer(final Card c) {
        Dialog d = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
                .setTitle("Such einen Player aus, mit dem du tauschen willst!")
                .setItems(new String[]{"Player 1", "Player 2", "Player 3", "Player 4"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dlg, int position) {
                        if (position == 0) {
                            finishTradeOffer(0, c);
                            dlg.cancel();
                        } else if (position == 1) {
                            finishTradeOffer(1, c);
                            dlg.cancel();
                        } else if (position == 2) {
                            finishTradeOffer(2, c);
                            dlg.cancel();
                        } else if (position == 3) {
                            finishTradeOffer(3, c);
                            dlg.cancel();
                        }
                    }
                })
                .create();
        d.setCanceledOnTouchOutside(false);
        d.show();
    }

    public void startDuel() {
        Dialog d = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
                .setTitle("Such eine Farbe für Duel aus!")
                .setItems(new String[]{"Rot", "Blau", "Gelb", "Grün"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dlg, int position) {
                        if (position == 0) {
                            dlg.cancel();
                            startDuel_chooseOpponent(Card.colors.RED);
                        } else if (position == 1) {
                            dlg.cancel();
                            startDuel_chooseOpponent(Card.colors.BLUE);
                        } else if (position == 2) {
                            dlg.cancel();
                            startDuel_chooseOpponent(Card.colors.YELLOW);
                        } else if (position == 3) {
                            dlg.cancel();
                            startDuel_chooseOpponent(Card.colors.GREEN);
                        }
                    }
                })
                .create();
        d.setCanceledOnTouchOutside(false);
        d.show();

    }

    private void startDuel_chooseOpponent(final Card.colors color) {
        Dialog d = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
                .setTitle("Such einen Opponent aus!")
                .setItems(new String[]{"Player 1", "Player 2", "Player 3", "Player 4"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dlg, int position) {
                        if (position == 0) {
                            writeNetMessage(new GameActions(GameActions.actions.DUEL_START, player.getID(), 0, color));
                            dlg.cancel();
                        } else if (position == 1) {
                            writeNetMessage(new GameActions(GameActions.actions.DUEL_START, player.getID(), 1, color));
                            dlg.cancel();
                        } else if (position == 2) {
                            writeNetMessage(new GameActions(GameActions.actions.DUEL_START, player.getID(), 2, color));
                            dlg.cancel();
                        } else if (position == 3) {
                            writeNetMessage(new GameActions(GameActions.actions.DUEL_START, player.getID(), 3, color));
                            dlg.cancel();
                        }
                    }
                })
                .create();
        d.setCanceledOnTouchOutside(false);
        d.show();
    }

    public void duelOpponentDialog(int duelStarterID) {
        Dialog d = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
                .setTitle("Spieler" + duelStarterID + "fordert dich zum Duel heraus. Such eine Farbe aus!")
                .setItems(new String[]{"Rot", "Blau", "Gelb", "Grün"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dlg, int position) {
                        if (position == 0) {
                            dlg.cancel();
                            writeNetMessage(new GameActions(GameActions.actions.DUEL_OPPONENT, player.getID(), Card.colors.RED));
                        } else if (position == 1) {
                            dlg.cancel();
                            writeNetMessage(new GameActions(GameActions.actions.DUEL_OPPONENT, player.getID(), Card.colors.BLUE));
                            ;
                        } else if (position == 2) {
                            dlg.cancel();
                            writeNetMessage(new GameActions(GameActions.actions.DUEL_OPPONENT, player.getID(), Card.colors.YELLOW));
                        } else if (position == 3) {
                            dlg.cancel();
                            writeNetMessage(new GameActions(GameActions.actions.DUEL_OPPONENT, player.getID(), Card.colors.GREEN));
                        }
                    }
                })
                .create();
        d.setCanceledOnTouchOutside(true);
        d.show();
    }

    void finishTradeOffer(int playerToTrade, Card c) {
        if (playerToTrade != player.getID()) {
            player.tradeCard(playerToTrade, c);
            ;
        } else {
            toastTradeError();
        }
    }

    public void hotDrop() {
        final long timer = System.currentTimeMillis();
        Dialog d = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
                .setItems(new String[]{"Klicke schnell!"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dlg, int position) {
                        writeNetMessage(new GameActions(GameActions.actions.HOT_DROP, player.getID(), System.currentTimeMillis() - timer));
                        dlg.cancel();
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

    public void toastYourNotAllowed() {
        Toast.makeText(getApplicationContext(), "Du besitzt mehr als 1 Karte!", Toast.LENGTH_SHORT).show();
    }

    public void toastCantTrade() {
        Toast.makeText(getApplicationContext(), "Du darfst nicht cheaten!", Toast.LENGTH_SHORT).show();
    }

    void toastTradeError() {
        Toast.makeText(getApplicationContext(), "Du kannst nicht mit dir selbst Tauschen!", Toast.LENGTH_SHORT).show();
    }

    public void toastAlreadyTraded() {
        Toast.makeText(getApplicationContext(), "Du hast schon getauscht!", Toast.LENGTH_SHORT).show();
    }

    public void toastGameFinished(int pID) {
        Log.d("GAME_END", "Sieger ist Spieler " + (pID + 1) + " mit der ID: " + pID);

        if (player.getID() == pID) {
            Intent intent = new Intent(getApplicationContext(), WinnerScreen.class);
            intent.putExtra("pID", pID + 1);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getApplicationContext(), LosingScreen.class);
            intent.putExtra("pID", pID + 1);
            startActivity(intent);
        }
    }

    public void tradeOffer(final int traderID, final Card tradedCard) {
        Dialog d = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
                .setTitle("Willst du eine Karte tauschen?")
                .setItems(new String[]{"JA", "NEIN"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dlg, int position) {
                        if (position == 0) {
                            player.acceptTrade(player.getID(), tradedCard);
                            dlg.cancel();
                        } else if (position == 1) {
                            player.declineTrade(traderID, tradedCard);
                            dlg.cancel();
                        }
                    }
                })
                .create();
        d.setCanceledOnTouchOutside(false);
        d.show();
    }

    public void toastStartHotDrop() {
        Toast.makeText(getApplicationContext(), "Klicke auf den 'Hot Drop' Button!", Toast.LENGTH_SHORT).show();
    }

    public void toastEndHotDropLooser() {
        Toast.makeText(getApplicationContext(), "Du warst leider zu langsam! +2 Karten", Toast.LENGTH_SHORT).show();
    }

    public void toastPlayersTime() {
        Toast.makeText(getApplicationContext(), "Deine Zeit: " + player.getMillSecs() + " Millisekunden", Toast.LENGTH_SHORT).show();
    }
}
