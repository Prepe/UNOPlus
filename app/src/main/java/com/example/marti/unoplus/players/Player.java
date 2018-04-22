package com.example.marti.unoplus.players;

import com.example.marti.unoplus.cards.Card;
import com.example.marti.unoplus.gameLogicImpl.GameControler;

import java.util.LinkedList;
import java.util.List;

public class Player {

    static String name;
    public GameControler gameController;

    LinkedList<Card> cards;

    public List<Card> getHand() {
        return this.cards;
    }

    public String getName(){
        return this.name;
    }

    public void drawCard(){
        for(Card c : gameController.drawCard() ){
            cards.add(c);
        }

    }

    public void playCard(Card c){
        gameController.playCard(this, c);
    }

    public void dropCard(Card c){
        if(gameController.dropCard()) {
            cards.remove(c);
        }
    }

    public void TradeCard(Card c, Player p){
        cards.remove(c);
        c = gameController.tradeCard(p,c);
        cards.add(c);
    }


    public Player(String name){
        this.name = name;
    }
}
