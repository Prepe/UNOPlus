
package com.example.marti.unoplus.Screens;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marti.unoplus.Client.HandCardView;
import com.example.marti.unoplus.Client.PlayedCardView;
import com.example.marti.unoplus.GameStatics;
import com.example.marti.unoplus.Net.UnoPlusNetwork;
import com.example.marti.unoplus.R;
import com.example.marti.unoplus.Server.ServerLogic;
import com.example.marti.unoplus.cards.Card;
import com.example.marti.unoplus.players.Player;
import com.example.marti.unoplus.sound.Sounds;
import com.example.marti.unoplus.sound.SoundManager;

import java.util.ArrayList;

public class GameScreen extends AppCompatActivity {


    public GameScreen() {
        super();
        this.handCards = new ArrayList<HandCardView>();
        this.soundManager = soundManager;
    }

    Button unoButton;
    Button devButton;
    TextView numCards;
    CountDownTimer timer;
    SoundManager soundManager;

    private Context context;
    public ServerLogic serverLogic = null;
    Player player = null;

    Card currentPlayCard;
    PlayedCardView playedCardView = null;

    ArrayList<HandCardView> handCards = null;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        soundManager.playSound(Sounds.THEMESTART);
        timer.cancel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();

    }

    @Override
    protected void onResume() {
        super.onResume();
        timer.start();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GameStatics.currentActivity = this;
        GameStatics.Initialize(true);

        soundManager = new SoundManager(this);
        soundManager.playSound(Sounds.THEMESTOP);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.game_screen);

        //numCards = (TextView)findViewById(R.id.numCards1);
        numCards = (TextView) findViewById(R.id.numCards1);

        String[] players = {"Player 1", "Player2", "Player3", "Player4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, players);

        ListView lv = findViewById(R.id.list);
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                final TextView mTextView = (TextView) view;
                switch (position) {
                    case 0:
                        //Toast.makeText(getApplicationContext(), "Player 1", Toast.LENGTH_SHORT).show();
                        //TO DO
                        if (Player.cheated) {
                            //TODO
                        }
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

        unoButton = findViewById(R.id.unounobutton);
        unoButton.setOnClickListener(handler);

        if (GameStatics.devMode) {
            devButton = findViewById(R.id.devGetCard);
            devButton.setOnClickListener(handler);
            GameStatics.net = new UnoPlusNetwork(true);
        }


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
                myCounter.setText("Verbleibende Zeit: "
                        + String.valueOf(millisUntilFinished / 1000));
            }
        }.start();


    }

    @Override
    public void onStart() {
        super.onStart();

       // this.playedCardView = new PlayedCardView(this.getApplicationContext(), this);

        this.playedCardView.updateCard(new Card(Card.colors.RED, Card.values.EIGHT));

        soundManager.playSound(Sounds.DEALINGCARD);
        for (int i = 0; i < 7; i++) {
            Card.colors rndcolor = GameStatics.randomEnum(Card.colors.class);
            Card.values rndvalue = GameStatics.randomEnum(Card.values.class);
            GameStatics.net.CLIENT_GetNewCardForHand('0', new Card(rndcolor, rndvalue));
        }


    }


    public void UpdateCurrentPlayCard(Card card) {
        this.playedCardView.updateCard(card);
    }


    public void timeUp(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(
                GameScreen.this);
        builder.setTitle("Zeit vorbei!")
                .setMessage("Ziehe Karte")
                .setCancelable(false)
                .setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                GameScreen.this.getApplicationContext();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

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

    private void setUpDevGame() {
        this.serverLogic = new ServerLogic();
        // this.player = new Player(0);

    }
/*
    public void addCardToHand(Card card) {
        soundManager.playSound(Sounds.DRAWCARD);
        HandCardView cardview = new HandCardView(GameScreen.this, this, card);
        this.handCards.add(cardview);


        int numCardshand = 0;
        for (int i = 0; i < handCards.size(); i++) {

            numCardshand++;
        }

        String s = Integer.toString(numCardshand);
        System.out.println(numCardshand);
        numCards.setText("( " + s + " )");


        LinearLayout handBox = findViewById(R.id.playerHandLayout);
        handBox.addView(cardview.view);

    }
*/
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
                soundManager.playSound(Sounds.DROPCARD);
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

}