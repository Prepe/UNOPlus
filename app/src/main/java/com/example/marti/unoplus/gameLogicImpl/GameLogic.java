package com.example.marti.unoplus.gameLogicImpl;

import java.util.LinkedList;

public class GameLogic {
    LinkedList<String> playerList;  //reference to all Players                      TODO change Type
    String deck;                    //reference to the Deck that is used            TODO change Type
    String lastCard;                //The card that is on top of the discard pile   TODO change Type
    String activePlayer;            //well active player (its his turn)
    int cardDrawCount = 1;          //the amount the next Player has to draw from the deck
    boolean reverse = false;        //is the game currently reversed or not
    boolean suspend = false;        //is the next Player suspended or not

    public GameLogic (LinkedList<String> pL, String gameDeck) {
        playerList = pL;
        deck = gameDeck;

        activePlayer = playerList.getFirst();
    }

    //Basic GameLogic should only be called when the card is good to play or player has to draw a card (card == null)
    public String runLogic (String aktivePlayer, String card) {
        if (card == null) {
            //aktivePlayer.drawCard();
        } else {
            playCard(card);
        }
        return nextPlayer(aktivePlayer);
    }

    //Return the amount of Cards the Player has to draw and set next draw to 1
    public int getCardDrawCount () {
        int amount = cardDrawCount;
        cardDrawCount = 1;
        return amount;
    }

    //Change the CardDrawCount to either the shown amount on Card or add to last amount
    public void changeCardDrawCount(int amount) {
        if (cardDrawCount == 1) {
            cardDrawCount = amount;
        } else {
            cardDrawCount = cardDrawCount + amount;
        }
    }

    //returns the activePlayer
    public String getActivePlayer() {
        return activePlayer;
    }

    /*
    * But the lastCard into the discard Pile that gets reused when Deck is empty
    * Make the played card the lastCard and trigger its effect on the game
    * */
    private void playCard(String card) {
        //deck.addUsedCard(lastCard);
        lastCard = card;
        //lastCard.cardEffect();
    }

    /*
    * Return the next Player after checking the direction of the game
    * */
    private String nextPlayer (String player) {
        if (reverse) {
            if (suspend) {
                //activePlayer = playerList.previousPlayer(playerList.previousPlayer(player));
            } else {
                //activePlayer = playerList.previousPlayer(player);
            }
        } else {
            if (suspend) {
                //activePlayer = playerList.nextPlayer(playerList.nextPlayer(player));
            } else {
                //activePlayer = playerList.nextPlayer(player);
            }
        }
        return activePlayer;
    }

    /*
    * Checks if the Card the Player wants to play is OK to be played
    * */
    public boolean checkCard (String card) {
        //Check card for right Value
        if (checkValue(card)) {
            return true;
        }

        //Check card for right Color
        if (checkColor(card)) {
            return true;
        }

        return false;
    }

    //Check for the colour of the card
    private boolean checkColor(String card) {
/*        //Check for black Card
        if (card.getColour() == 0) {
            return true;
        }
        //Check for matching Colour
        if (card.getColour() == lastCard.getColour()) {
            return true;
        }
*/
        return false;
    }

    //Check for the Value of the card
    private boolean checkValue(String card) {
/*        //Check for matching Value
        if (card.getValue() == lastCard.getValue()) {
            return true;
        }
*/
        return false;
    }

    //Plays 1 Card from the Deck without logic Checks
    public void playTopCard() {
/*        if (lastCard != null) {
            deck.addUsedCard(lastCard);
        }
        lastCard = deck.drawCard(1);
        lastCard.effect();
*/    }
}
