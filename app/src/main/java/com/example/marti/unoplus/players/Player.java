package com.example.marti.unoplus.players;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marti.unoplus.GameActions;
import com.example.marti.unoplus.GameStatics;
import com.example.marti.unoplus.R;
import com.example.marti.unoplus.cards.Card;
import com.example.marti.unoplus.gameLogicImpl.GameViewProt;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Player {
    String playerName;
    Integer ID;
    GameViewProt gameViewProt;
    Card lastCard;
    LinkedList<Card> handcards; //Hand
    int [] handcardcounter;


    public Player(Integer id, String playerName){
        ID = id;
        this.playerName = playerName;
        handcards = new LinkedList<>();
    }

    public void setGV(GameViewProt gv) {
        gameViewProt = gv;
    }

    public void setID(int id) {
        ID = id;
    }

    public List<Card> getHand(){
        return this.handcards;
    }

    public int getHandSize() {
        return getHand().size();
    }

    public void setName(String name){this.playerName = name;}

    public String getName(){
        return this.playerName;
    }

    public Integer getID(){
        return ID;
    }

    public void initialsiedHandCardCounters(int size){
        handcardcounter = new int[size];

    }
    public int[] getHandcardcounter(){
        return handcardcounter;
    }

    //<---------- Player Actions ---------->
    //Ask server for Cards
    public void drawCard(){
        GameActions action;
        action = new GameActions(GameActions.actions.DRAW_CARD, ID);
       // gameViewProt.updateAllConnected(action);
    }

    //Tell Server what Card you want to play
    public void playCard(Card c){
        GameActions action;
        action = new GameActions(GameActions.actions.PLAY_CARD, ID, c);
        //gameViewProt.updateAllConnected(action);
        Log.d("GameDebug", "Player playCard :" + c.value.toString() + " " + c.color.toString());
        this.gameViewProt.writeNetMessage(action);
    }

    public void dropCard(Card c){

    }

    public void TradeCard(Card c, Player p){

    }

    //<---------- Player Reactions ---------->
    public void callPlayer(GameActions action) {
        switch(action.action){
            case DRAW_CARD:
                gotCard(action.playerID, action.cards);
                break;
            case PLAY_CARD:
                cardPlayed(action.playerID, action.card);
                break;
            case UPDATE:
                updateGame(action.card);
                break;
            case NEXT_PLAYER:
                //TODO ipml
                break;
            case WISH_COLOR:
                if(action.playerID == this.ID) {
                    this.gameViewProt.chooseColor();
                }
                break;
            case INIT_GAME:
                initialsiedHandCardCounters(action.nextPlayerID);
                break;
        }
    }

    //Check if the GA is for you
    boolean checkID(int ID){
        return ID == this.ID;
    }

    //Update Game Screen (last played Card, etc.)
    void updateGame(Card lastCard) {
        this.lastCard = lastCard;
        this.gameViewProt.updateCurrentPlayCard(this.lastCard);
    }

    //Add given Cards to your handcards
    void gotCard(int ID, List<Card> cards) {
        if (checkID(ID)) {
            for (Card c : cards) {
                this.handcards.add(c);
                this.gameViewProt.addCardToHand(c);
            }
            updateHandCardCounter(cards.size(), ID);
        } else {
            updateHandCardCounter(cards.size(), ID);
        }

    }

    //Your intended Card was played so now you can remove it
    void cardPlayed(int ID, Card card){
        if (checkID(ID)) {
            this.handcards.remove(card);
            updateHandCardCounter(-1, ID);
            this.gameViewProt.removeCardFromHand(card);

        } else {
            updateHandCardCounter(-1, ID);
        }
    }

    //<---------- Misc ---------->
    public void createDummyCards()
    {
        List<Card> list = new ArrayList<Card>();
        for (int i = 0; i < 7; i++)
        {
            Card.colors rndcolor = GameStatics.randomEnum(Card.colors.class);
            Card.values rndvalue = GameStatics.randomEnum(Card.values.class);
            Card card = new Card(rndcolor, rndvalue);
            list.add(card);
        }
        this.gotCard(this.getID(), list);
    }

    void updateHandCardCounter(int count, int ID ){
        handcardcounter[ID] += count;
        gameViewProt.updateCountersInView();
    }
}
