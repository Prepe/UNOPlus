package com.example.marti.unoplus.gameLogicImpl;

import com.example.marti.unoplus.Card;
import com.example.marti.unoplus.cards.CardEffects;
import com.example.marti.unoplus.Deck;
import com.example.marti.unoplus.Player;
import com.example.marti.unoplus.PlayerList;

public class GameLogic {
    PlayerList playerList;      //reference to all Players
    Deck deck;                  //reference to the Deck that is used
    Player activePlayer;        //well active player (its his turn)
    CardEffects effects;        //used to call CardEffects
    int cardDrawCount = 1;      //the amount the next Player has to draw from the deck
    boolean reverse = false;    //is the game currently reversed or not
    boolean skip = false;       //is the next Player suspended or not
    Card.values lastCardValue;  //The value of the card that is on top of the discard pile
    Card.colors lastCardColor;  //The color of the card that is on top of the discard pile

    public GameLogic (PlayerList pL, Deck gameDeck) {
        playerList = pL;
        deck = gameDeck;

        activePlayer = playerList.getFirst();
    }

    //Basic GameLogic should only be called when the card is good to play or player has to draw a card (card == null)
    public Player runLogic (Player player, Card card) {
        if (card == null) {
            player.drawCard();
        } else {
            playCard(card);
            effects.cardEffect(player, card);
        }
        return nextPlayer(player);
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
    public Player getActivePlayer() {
        return activePlayer;
    }

    /*
    * But the lastCard into the discard Pile that gets reused when Deck is empty
    * Make the played card the lastCard and trigger its effect on the game
    * */
    private void playCard(Card card) {
        lastCardValue = card.value;
        lastCardColor = card.color;
    }

    /*
    * Return the next Player after checking the direction of the game
    * TODO fix nextPlayer when PlayerList is updated
    * */
    private Player nextPlayer (Player player) {
        if (reverse) {
            if (skip) {
                skip = false;
                //activePlayer = playerList.previousPlayer(playerList.previousPlayer(player));
            } else {
                //activePlayer = playerList.previousPlayer(player);
            }
        } else {
            if (skip) {
                skip = false;
                //activePlayer = playerList.nextPlayer(playerList.nextPlayer(player));
            } else {
                //activePlayer = playerList.nextPlayer(player);
            }
        }
        return activePlayer;
    }

    /*
    * Checks if the Card the Player wants to play can be played
    * */
    public boolean checkCard (Card card) {
        // Check if last card was a +2 and the card Effect is still active
        if (lastCardValue == Card.values.PLUS_TWO && cardDrawCount > 1) {
            if (checkValue(card) || card.value == Card.values.PLUS_FOUR) {
                return true;
            }
            // Check if last card was a +4 and the card Effect is still active
        } else if (lastCardValue == Card.values.PLUS_FOUR && cardDrawCount > 1) {
            if (checkValue(card) || card.color == lastCardColor) {
                return true;
            }
            // When no +2 or +4 Effect is active make normal Card Check
        } else {
            //Check card for right Value
            if (checkValue(card)) {
                return true;

                //Check card for right Color
            } else if (checkColor(card)) {
                return true;
            }
        }

        // If all checks fail return Card cannot be played
        return false;
    }

    //Check for the colour of the card
    private boolean checkColor(Card card) {
        /*
        * Check for wild Card
        * Wild Cards should be playable no matter what
        * */
        if (card.getColor() == Card.colors.WILD) {
            return true;
        }
        /*
        * Check for matching Colour
        * */
        if (card.getColor() == lastCardColor) {
            return true;
        }

        /*
        * If the last Card was a Wild Card (Card.color == WILD) any Card can be played
        * */
        if (lastCardColor == Card.colors.WILD) {
            return true;
        }

        return false;
    }

    //Check for the Value of the card
    private boolean checkValue(Card card) {
        //Check for matching Value
        if (card.getValue() == lastCardValue) {
            return true;
        }

        return false;
    }

    //Plays 1 Card from the Deck without logic Checks
    public void playTopCard() {
        Card card = deck.draw();
        lastCardValue = card.value;
        lastCardColor = card.color;
        effects.cardEffect(null, card);
    }

    //Tels the game to skip the next player
    public void skipNext() {
        skip = true;
    }

    //Reverses the turn order when called
    public void toggleReverse() {
        if (reverse) {
            reverse = false;
        } else {
            reverse = true;
        }
    }

    //Sets the color requirement for next card
    public void wishColor(Card.colors colorWish) {
        lastCardColor = colorWish;
    }
}
