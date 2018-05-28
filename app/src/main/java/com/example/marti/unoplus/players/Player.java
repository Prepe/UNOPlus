package com.example.marti.unoplus.players;

import android.util.Log;

import com.example.marti.unoplus.GameActions;
import com.example.marti.unoplus.GameStatics;
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


    public Player(Integer id){
        ID = id;
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
    //Ask for Cards
    public void drawCard(){
        GameActions action;
        action = new GameActions(GameActions.actions.DRAW_CARD, ID);
    }

    //Tell what Card you want to play
    public void playCard(Card c){
        GameActions action;
        action = new GameActions(GameActions.actions.PLAY_CARD, ID, c);
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
                if (action.check) {
                    wrongCard(action.playerID);
                } else {
                    cardPlayed(action.playerID, action.card);
                }
                break;
            case UPDATE:
                updateGame(action.nextPlayerID, action.card);
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
    boolean checkID(int pID){
        return pID == this.ID;
    }

    //Update Game Screen (last played Card, etc.)
    void updateGame(int nextPID, Card card) {
        updateLastCard(card);
        if (ID == nextPID) {
            gameViewProt.toastYourTurn();
        }
    }

    void updateLastCard(Card lastCard) {
        this.lastCard = lastCard;
        this.gameViewProt.updateCurrentPlayCard(this.lastCard);
    }

    //Add given Cards to your handcards
    void gotCard(int pID, List<Card> cards) {
        if (checkID(pID)) {
            for (Card c : cards) {
                this.handcards.add(c);
                this.gameViewProt.addCardToHand(c);
            }
            updateHandCardCounter(cards.size(), pID);
        } else {
            updateHandCardCounter(cards.size(), pID);
        }

    }

    //A Card was played so now you can remove it and/or update Screen
    void cardPlayed(int pID, Card card){
        if (checkID(pID)) {
            this.handcards.remove(card);
            updateHandCardCounter(-1, pID);
            this.gameViewProt.removeCardFromHand(card);
            if (this.handcards.size() == 0) {
                winGame();
            }
        } else {
            updateHandCardCounter(-1, pID);
        }

        updateLastCard(card);
    }

    //Your intended Card couldn't be played
    void wrongCard(int pID) {
        if (checkID(pID)) {
            gameViewProt.toastWrongCard();
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

    void winGame() {
        GameActions win = new GameActions(GameActions.actions.GAME_FINISH,ID,true);
        gameViewProt.writeNetMessage(win);
        gameViewProt.toastGameFinsihed(ID);
    }
}
