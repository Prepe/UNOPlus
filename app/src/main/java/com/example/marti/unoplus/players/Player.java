package com.example.marti.unoplus.players;

import com.example.marti.unoplus.GameActions;
import com.example.marti.unoplus.cards.Card;
import com.example.marti.unoplus.gameLogicImpl.GameController;
import com.example.marti.unoplus.gameLogicImpl.GameViewProt;

import java.util.LinkedList;
import java.util.List;

import jop.hab.net.NetworkIOManager;
import jop.hab.net.ObserverInterface;

public class Player {
    static String name;
    static int ID;
    public GameController gameController;
    public static boolean cheated = true;
    GameViewProt gameViewProt;
    Card lastCard;

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

    //<---------- Player Actions ---------->
    public void drawCard(){
        GameActions action;
        action = new GameActions(GameActions.actions.DRAW_CARD, ID);
        gameViewProt.updateAllPlayers(action);
    }

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

    boolean checkID(int ID){
        return ID == this.ID;
    }

    void updateGame(Card lastCard) {
        this.lastCard = lastCard;
    }

    void gotCard(int ID, List<Card> cards) {
        if (checkID(ID)) {
            for (Card c : cards) {
                this.cards.add(c);
            }
        }
    }

    void cardPlayed(int ID, Card card){
        if (checkID(ID)) {
            this.cards.remove(card);
        }
    }
}
