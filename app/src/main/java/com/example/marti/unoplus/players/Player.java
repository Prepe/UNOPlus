package com.example.marti.unoplus.players;

import android.util.Log;

import com.example.marti.unoplus.GameActions;
import com.example.marti.unoplus.GameStatics;
import com.example.marti.unoplus.cards.Card;
import com.example.marti.unoplus.cards.HandCardList;
import com.example.marti.unoplus.Screens.GameViewProt;

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
    HandCardList hand;


    public Player(Integer id){
        ID = id;
        //handcards = new LinkedList<>();
        hand = new HandCardList();
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
            case THROW_CARD_CONFIRM:
                this.throwAwayCardConfirmed(action.playerID, action.card);
                break;
            case DRAW_CARD:
                gotCard(action.playerID, action.cards);
                break;
            case PLAY_CARD:
                if (action.check) {
                    cardPlayed(action.playerID, action.card);
                } else {
                    wrongCard(action.playerID);
                }
                break;
            case UPDATE:
                updateGame(action.playerID, action.card);
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
                //this.handcards.add(c);
                this.hand.addCard(c);
                //this.gameViewProt.addCardToHand(c);
                this.gameViewProt.handChanged(hand.getHand());
            }
            updateHandCardCounter(cards.size(), pID);
        } else {
            updateHandCardCounter(cards.size(), pID);
        }

    }

    //Your intended Card was played so now you can remove it
    void cardPlayed(int ID, Card card){
        if (checkID(ID)) {
            //this.handcards.remove(card);
            this.hand.removeCard(card);
            //this.gameViewProt.removeCardFromHand(card);
            this.gameViewProt.handChanged(hand.getHand());
            gameViewProt.timer.cancel();
            updateHandCardCounter(-1, ID);
        } else {
            updateHandCardCounter(-1, ID);
        }

        updateLastCard(card);
    }

    //Your intended Card couldn't be played
    void wrongCard(int pID) {
        if (checkID(pID)) {
            gameViewProt.toastWrongCard();
        }
    }

    public boolean hasCard(Card card)
    {
        return this.handcards.contains(card);
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

    public void drawCardIfTimesUp()
    {
        List<Card> list = new ArrayList<Card>();
            Card.colors rndcolor = GameStatics.randomEnum(Card.colors.class);
            Card.values rndvalue = GameStatics.randomEnum(Card.values.class);
            Card card = new Card(rndcolor, rndvalue);
            list.add(card);
        this.gotCard(this.getID(), list);
    }

    void updateHandCardCounter(int count, int ID ){
        handcardcounter[ID] += count;
        gameViewProt.updateCountersInView();
    }

    void winGame() {
        GameActions win = new GameActions(GameActions.actions.GAME_FINISH,ID,true);
        gameViewProt.writeNetMessage(win);
        gameViewProt.toastGameFinished(ID);
    }

    public void throwAwayCard(Card card){
        Log.d("GameDebug", "Player threw Card away :" + card.value.toString() + " " + card.color.toString());
        GameActions tam = new GameActions(GameActions.actions.THROW_CARD, ID,card);
        gameViewProt.writeNetMessage(tam);

    }

    void throwAwayCardConfirmed(int playerID, Card card){
        updateHandCardCounter(-1, playerID);
        if(playerID != this.getID()){
            return;
        }
        this.handcards.remove(card);
        gameViewProt.removeCardFromHand(card);

    }
}
