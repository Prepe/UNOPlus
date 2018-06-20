package com.example.marti.unoplus.gameLogicImpl;

import com.example.marti.unoplus.cards.Card;

/**
 * Created by ekzhu on 11.06.2018.
 */

public class DuelData {
    private int duelStarterID;
    private int opponentID;
    private Card.colors starterColor;
    private int loserIDfortests;



    public DuelData(int duelStarterID, int opponentID, Card.colors starterColor) {
        this.duelStarterID = duelStarterID;
        this.opponentID = opponentID;
        this.starterColor = starterColor;
    }


    public int getDuelLoserID(Card.colors opponentColor) {
        if (opponentColor.equals(starterColor)){
            return duelStarterID;
        } else {
            return opponentID;
        }
    }
    public void setLoserIDfortests(int loserIDfortests) {
        this.loserIDfortests = loserIDfortests;
    }
}
