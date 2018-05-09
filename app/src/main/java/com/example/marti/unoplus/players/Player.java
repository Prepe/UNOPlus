package com.example.marti.unoplus.players;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marti.unoplus.GameActions;
import com.example.marti.unoplus.cards.Card;
import com.example.marti.unoplus.gameLogicImpl.GameController;
import com.example.marti.unoplus.gameLogicImpl.GameViewProt;

import java.util.LinkedList;
import java.util.List;

import jop.hab.net.NetworkIOManager;
import jop.hab.net.ObserverInterface;

public class Player {
   NetworkIOManager networkIOManager;
    TextView textView;
    EditText editTextSend;
    Button btnSend;
    Button btnUnoUno;
    ImageButton btnr8;
    ImageButton btng7;
    ImageButton btnb3;
    ImageView playCard;
    String hostAdress;
    String mode;

/*
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        hostAdress = getIntent().getStringExtra("adress");
        mode = getIntent().getStringExtra("mode");


        networkIOManager = new NetworkIOManager(this);
        networkIOManager.setMode(mode);
        networkIOManager.setHostAdress(hostAdress);
        networkIOManager.open();


    }
*/

    static String name;
    static int ID;
    public GameController gameController;
    public static boolean cheated = true;
    GameViewProt gameViewProt;

    LinkedList<Card> cards; //Hand

    public List<Card> getHand(){
        return this.cards;
    }

    public int getHandSize() {
        return getHand().size();
    }

    public String getName(){
        return this.name;
    }

    public Player(GameViewProt gv, int id){
        gameViewProt = gv;
        this.ID = id;
        cards = new LinkedList<>();
    }

    public int getID(){
        return ID;
    }

    void drawCard(){
        GameActions action;
        action = new GameActions(GameActions.actions.DRAW_CARD, ID);
        gameViewProt.updateAllPlayers(action);
    }
/*
    void sendAction(GameActions actions){
        networkIOManager.writeGameaction(actions);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
*/

    void gotCard(int ID, List<Card> cards) {
        if (checkID(ID)) {
            for (Card c : cards) {
                this.cards.add(c);
            }
        }
    }

    boolean checkID(int ID){
        return ID == this.ID;
    }

    void playCard(Card c){
        GameActions action;
        action = new GameActions(GameActions.actions.PLAY_CARD, ID, c);
        gameViewProt.updateAllPlayers(action);
    }

    void dropCard(Card c){
        /*
        if(gameController.dropCard()) {
            cards.remove(c);
        }
        */
    }

    void TradeCard(Card c, Player p){
        cards.remove(c);
        c = gameController.tradeCard(p,c);
        cards.add(c);
    }

    void cardPlayed(int ID, Card card){
        if (checkID(ID)) {
            this.cards.remove(card);
        }
    }

    public void callPlayer(GameActions action) {
        switch(action.action){
            case DRAW_CARD:
                gotCard(action.playerID, action.cards);
                break;
            case PLAY_CARD:
                cardPlayed(action.playerID, action.card);
        }
    }

    /**
    @Override
    public void dataChanged() {
        GameActions action;
        action = networkIOManager.getGameAction();

        switch(action.action){
            case DRAW_CARD:
                gotCard(action.playerID, action.cards);
                break;
            case PLAY_CARD:
                cardPlayed(action.playerID, action.card);
        }


    }
**/

}
