package com.example.marti.unoplus;

import com.example.marti.unoplus.cards.Card;

import java.util.LinkedList;
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
        GAME_FINISH (8),
        THROW_CARD(9),
        THROW_CARD_CONFIRM (10),
        HOT_DROP (11),
        CARD_SPIN(12),
        DUEL_START(13),  //ask for color and opponent by card play
        DUEL_OPPONENT(14), //response from opponent with color
        GIVE_Hand (15),
        GET_NEWHand(16),
        GOT_Hand(17),
        DO_CardSpin(18);

        private int value;
        actions(int value){this.value = value;}
    }

    public actions action;
    public Integer playerID;
    public Integer nextPlayerID;
    public String countCards;
    public Card card;
    public LinkedList<Card> cards;
    public Boolean check;
    public Card.colors colorWish;
    public boolean gcSend = false;
    public Long timestamp;

    //Used to update all players to what card was last played and who is currently the active player
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

    public GameActions(actions action, int pID, Card card, boolean check) {
        this.action = action;
        playerID = pID;
        this.card = card;
        this.check = check;
    }

    //Used to give 1 player a number of card from the deck
    public GameActions(actions action, int pID, LinkedList<Card> drawnCards) {
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

    //Constructor for startDuel_Request
    public GameActions(actions action, int pID, int opponentPID, Card.colors color) {
        this.action = action;
        playerID = pID;
        nextPlayerID = opponentPID;
        colorWish = color;
    }

    //Constructor for startDuel_Response
    public GameActions(actions action, int pID, int opponentPID) {
        this.action = action;
        playerID = pID;
        nextPlayerID = opponentPID;
    }

    //Constructor for HotDrop
    public GameActions(actions action, int pID, long timestamp){
        this.action = action;
        playerID = pID;
        this.timestamp = timestamp;
    }
}
