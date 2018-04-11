package com.example.marti.unoplus.gameLogicImpl;

import java.util.LinkedList;

/**
 * Created by marti on 10.04.2018.
 */

public class GameControler {
    String players;     //reference to all Players in the Game  TODO Change Type
    String deck;              //reference to the Deck that is used    TODO Change Type
    GameLogic logic;        //reference to the GameLogic
    int startingHand = 7;   //Amount of Cards every Player gets at the start of the Game
    float turnTime;         //Turn Timer for the Game

    public GameControler(String playersList, String gameDeck, GameLogic gameLogic) {
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
        drawHandCardsForPlayers();
        logic.playTopCard();
    }

    private void drawHandCardsForPlayers() {
/*        for (String p : players) {
            for (int i = 0; i < startingHand; i++) {
                //p.drawCard();
            }
        }
*/    }

    //Method for all Players to call to draw Cards form the Deck
    public LinkedList<String> drawCard(String player) {
        LinkedList<String> cards;
        cards = null; //deck.drawCards(logic.getCardDrawCount());
        return cards;
    }

    //Method for all Players to call when they want to play a Card
    public boolean playCard(String player, String card) {
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
    public boolean tradeCard(String player, String card) {
        //TODO implement

        return false;
    }

}
