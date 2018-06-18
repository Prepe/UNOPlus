package com.example.marti.unoplus.gameLogicImpl;

import com.example.marti.unoplus.GameActions;
import com.example.marti.unoplus.cards.Card;
import com.example.marti.unoplus.cards.CardEffects;
import com.example.marti.unoplus.cards.Deck;
import com.example.marti.unoplus.players.Player;
import com.example.marti.unoplus.players.PlayerList;

public class GameLogic {
    PlayerList playerList;      //reference to all Players
    Deck deck;                  //reference to the Deck that is used
    Player activePlayer;        //well active player (its his turn)
    CardEffects effects;        //used to call CardEffects
    Card.values lastCardValue;  //The value of the card that is on top of the discard pile
    Card.colors lastCardColor;  //The color of the card that is on top of the discard pile
    GameController controller;
    int cardDrawCount = 1;      //the amount the next Player has to draw from the deck
    boolean reverse = false;    //is the game currently reversed or not
    boolean skip = false;       //is the next Player suspended or not

    public GameLogic(PlayerList pL, Deck gameDeck, GameController gc) {
        controller = gc;
        effects = new CardEffects(this, gc);
        playerList = pL;
        deck = gameDeck;

        activePlayer = playerList.getFirst();
    }
    public GameLogic(){}

    //Basic GameLogic should only be called when the card is good to play or player has to draw a card (card == null)
    public void runLogic(Player player, Card card) {
        playCard(card);
        effects.cardEffect(player, card);
    }

    //Return the amount of Cards the Player has to draw and set next draw to 1
    public int getCardDrawCount() {
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
    * */
    public Player nextPlayer(Player player) {
        if (player == null) {
            player = activePlayer;
        }

        if (reverse) {
            if (skip) {
                skip = false;
                activePlayer = playerList.getPrevious(playerList.getPrevious(player));
            } else {
                activePlayer = playerList.getPrevious(player);
            }
        } else {
            if (skip) {
                skip = false;
                activePlayer = playerList.getNext(playerList.getNext(player));
            } else {
                activePlayer = playerList.getNext(player);
            }
        }

        controller.gA = new GameActions(GameActions.actions.UPDATE,activePlayer.getID(),new Card(lastCardColor,lastCardValue));
        controller.update();

        return activePlayer;
    }

    /*
    * Checks what Player wants to play a card and if he is allowed to play it
    * */
    public boolean checkCard(Card card, Player player) {
        //Check if the player is the active player
        if (player.equals(activePlayer)) {
            //Check for active +2/4 effect
            if (cardDrawCount > 1) {
                //Check if played card is a +4
                if (card.value == Card.values.PLUS_FOUR) {
                    return true;
                }
                //Check for played card is +2 and last card was +2
                if (checkValue(card)) {
                    return true;
                }
                //Check for played card is +2 and last card was +4
                if (card.value == Card.values.PLUS_TWO && checkColor(card)) {
                    return true;
                }
            } else {
                //Check card for right Value
                if (checkValue(card)) {
                    return true;

                    //Check card for right Color
                } else if (checkColor(card)) {
                    return true;
                }
            }
        } else if (checkValue(card) && checkColor(card)) {
            activePlayer = player;
            return true;
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
    public void playTopCard(Card topCard) {
        lastCardValue = topCard.value;
        lastCardColor = topCard.color;
        effects.cardEffect(null, topCard);
    }

    //Tells the game to skip the next player
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
        lastCardValue = Card.values.CHOOSE_COLOR;
    }

    public PlayerList getPlayerList (){
        return playerList;
    }

    public boolean checkifreversed (){


        return reverse;
    }

    public void setLastCardValue(Card.values lastCardValue) {
        this.lastCardValue = lastCardValue;
    }

    public void setLastCardColor(Card.colors lastCardColor) {
        this.lastCardColor = lastCardColor;
    }

    public Card.colors getLastCardColor(){return lastCardColor;}

    public Card.values getLastCardValue(){return lastCardValue;}

}
