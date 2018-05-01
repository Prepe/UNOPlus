package com.example.marti.unoplus;

import com.example.marti.unoplus.cards.Card;

import java.util.List;

/**
 * Created by marti on 25.04.2018.
 */

public class GameActions {
    public enum actions {
        PLAY_CARD(0),
        DRAW_CARD(1),
        WISH_COLOR(2),
        DROP_CARD(3),
        TRADE_CARD(4);

        private int value;

        private actions(int value) {
            this.value = value;
        }
    }

    public actions action;
    public Integer playerID;
    public Integer nextPlayerID;
    public List<Card> cards;
    public Boolean check;
    public Card.colors colorWish;

    public GameActions(actions action, int pID, int nextPID) {
        this.action = action;
        playerID = pID;
        nextPlayerID = nextPID;
    }

    public GameActions(actions action, int pID, List<Card> drawnCards) {
        this.action = action;
        playerID = pID;
        cards = drawnCards;
    }

    public GameActions(actions action, int pID, Card.colors color) {
        this.action = action;
        playerID = pID;
        colorWish = color;
    }

    public GameActions(actions action, int pID, boolean check) {
        this.action = action;
        playerID = pID;
        this.check = check;
    }
}
