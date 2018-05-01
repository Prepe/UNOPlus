package com.example.marti.unoplus.gameLogicImpl;

import com.example.marti.unoplus.cards.Card;
import com.example.marti.unoplus.cards.Deck;
import com.example.marti.unoplus.players.Player;
import com.example.marti.unoplus.players.PlayerList;

import java.util.LinkedList;

/**
 * Created by marti on 10.04.2018.
 */

public class GameControler {
    PlayerList players;     //reference to all Players in the Game
    Deck deck;              //reference to the Deck that is used
    GameLogic logic;        //reference to the GameLogic
    int startingHand = 7;   //Amount of Cards every Player gets at the start of the Game
    float turnTime;         //Turn Timer for the Game

    public GameControler(PlayerList playersList, Deck gameDeck, GameLogic gameLogic) {
        players = playersList;
        deck = gameDeck;
        logic = gameLogic;

        setUpGame();
        updateAllPlayers();
    }

    //Method to Update all Players
    public void updateAllPlayers() {
        //TODO update the Players to show what happens
    }

    //Give all Players cards and Play the first Card
    private void setUpGame() {
        deck.shuffle();
        drawHandCardsForPlayers();
        logic.playTopCard();
    }

    private void drawHandCardsForPlayers() {
        for (Player p : players.getPlayers()) {
            for (int i = 0; i < startingHand; i++) {
                p.drawCard();
            }
        }
    }

    //Method for all Players to call to draw Cards form the Deck
    public LinkedList<Card> drawCard() {
        LinkedList<Card> cards = new LinkedList<>();
        if (deck.isEmptyDeck()) {
            deck.replaceTakeDeck();
        }
        for (int i = 0; i < logic.getCardDrawCount(); i++) {
            cards.add(deck.draw());
        }
        return cards;
    }

    //Method for all Players to call when they want to play a Card
    public boolean playCard(Player player, Card card) {
        boolean cardOK = logic.checkCard(card);
        if (cardOK) {
            logic.runLogic(player, card);
        }
        return cardOK;
    }

    /*
    * Method that times each Turn for Players
    * When at 0 draws a Card for the active Player
    * Should be threded
    * */
    private void runTimer() {
        //TODO implement
    }


    //Method to cheat and drop a Card
    public boolean dropCard() {
        //TODO implement

        return false;
    }

    //Method to cheat and trade a Card with a Player
    public Card tradeCard(Player player, Card card) {
        //TODO implement

        return card;
    }

    public Player getWinningPlayer() {
        for (Player p : players.getPlayers()) {
            if (p.getHandSize() == 0) {
                return p;
            }
        }
        return null;
    }

    public boolean won() {
        for (Player p : players.getPlayers()) {
            if (p.getHandSize() == 0) {
                return true;
            }
        }
        return false;
    }

}
