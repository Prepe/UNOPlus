package com.example.marti.unoplus;

import com.example.marti.unoplus.cards.Card;

import java.util.List;

/**
 * Created by marti on 25.04.2018.
 */

public class GameActions {
    public enum actions {
        UPDATE (0),
        PLAY_CARD (1),
        DRAW_CARD (2),
        WISH_COLOR (3),
        DROP_CARD (4),
        TRADE_CARD (5),
        NEXT_PLAYER (6),
        INIT_GAME (7),
        GAME_FINISH (8);

        private int value;
        actions(int value){this.value = value;}
    }

    public actions action;
    public Integer playerID;
    public Integer nextPlayerID;
    public String countCards;
    public Card card;
    public List<Card> cards;
    public Boolean check;
    public Card.colors colorWish;
    public boolean gcSend = false;

    //Used to update all players to what card was last played and the who is currently the active player
    public GameActions (actions action, Card card, int nextPID) {
        this.action = action;
        nextPlayerID = nextPID;
        this.card = card;
    }

    //Used to tell all players who's turn it currently is (called after game logic has finished)
    public GameActions(actions action, int nextPID) {
        this.action = action;
        nextPlayerID = nextPID;
    }

    public GameActions(actions action, int nextPID, String countC) {
        this.action = action;
        nextPlayerID = nextPID;
        countCards = countC;
    }


    //Used to tell GC that player wants to play a card and same method gets returned to player if card can be played
    public GameActions(actions action, int pID, Card card) {
        this.action = action;
        playerID = pID;
        this.card = card;
    }

    //Used to give 1 player a number of card from the deck
    public GameActions(actions action, int pID, List<Card> drawnCards) {
        this.action = action;
        playerID = pID;
        cards = drawnCards;
    }

    //Used to tell GC what color the player wished after playing a color change or +4
    public GameActions(actions action, int pID, Card.colors color) {
        this.action = action;
        playerID = pID;
        colorWish = color;
    }

    //Used to tell a player if his action is valid (drop a card)
    public GameActions(actions action, int pID, boolean check) {
        this.action = action;
        playerID = pID;
        this.check = check;
    }
}
