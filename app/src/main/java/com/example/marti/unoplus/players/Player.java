package com.example.marti.unoplus.players;

import com.example.marti.unoplus.GameActions;
import com.example.marti.unoplus.cards.Card;
import com.example.marti.unoplus.gameLogicImpl.GameViewProt;

import java.util.LinkedList;
import java.util.List;

public class Player {
    static String playerName;
    static int ID;
    GameViewProt gameViewProt;
    Card lastCard;
    LinkedList<Card> handcards; //Hand


    public Player(GameViewProt gv, int id){
        gameViewProt = gv;
        this.ID = id;
        handcards = new LinkedList<>();
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

    public int getID(){
        return ID;
    }

    //<---------- Player Actions ---------->
    //Ask server for Cards
    public void drawCard(){
        GameActions action;
        action = new GameActions(GameActions.actions.DRAW_CARD, ID);
        gameViewProt.updateAllPlayers(action);
    }

    //Tell Server what Card you want to play
    public void playCard(Card c){
        GameActions action;
        action = new GameActions(GameActions.actions.PLAY_CARD, ID, c);
        gameViewProt.updateAllPlayers(action);
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
        }
    }

    //Check if the GA is for you
    boolean checkID(int ID){
        return ID == this.ID;
    }

    //Update Game Screen (last played Card, etc.)
    void updateGame(Card lastCard) {
        this.lastCard = lastCard;
    }

    //Add given Cards to your handcards
    void gotCard(int ID, List<Card> cards) {
        if (checkID(ID)) {
            for (Card c : cards) {
                this.handcards.add(c);
            }
        }
    }

    //Your intended Card was played so now you can remove it
    void cardPlayed(int ID, Card card){
        if (checkID(ID)) {
            this.handcards.remove(card);
        }
    }
}
