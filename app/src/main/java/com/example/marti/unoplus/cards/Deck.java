package com.example.marti.unoplus.cards;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by ekzhu on 02.04.2018.
 */

public class Deck {

    public Deck() {
        decksinit();
        buildDeck();
    }

    LinkedList<Card> deck;          //the deck where players take cards from
    LinkedList<Card> drawnCards;    //all drawn cards land here waiting to be reshuffeld

    private void decksinit() {
        deck = new LinkedList<>();
        drawnCards = new LinkedList<>();
    }

    //Build the initial deck

    /* An Uno deck contains 108 cards.
        Each color contains:
            2 of each number 1 - 9
            1 of number 0
            2 skips
            2 reverses
            2 draw 2 cards
        Also:
            4 wild cards (color all)
            4 wild draw 4 cards (color all)

     */

    //Build and shuffle a new deck for a game
    private void buildDeck() {
        createNormalCards();
        createWildCards();

        createHotDrop();
        createDuel();
        createCardSpin();

        shuffle();
        System.out.println(this.deck.size() + " cards created and put in deck");
    }

    //Create cards that occur in each color
    private void createNormalCards() {
        for (int color = 0; color < 4; color++) {
            for (int num = 0; num < 13; num++) {
                int x = (num == 0) ? 1 : 2;
                for (int i = 0; i < x; i++) {
                    deck.add(new Card(color, num));
                }
            }
        }
    }

    //Create wild cards
    private void createWildCards() {
        for (int i = 0; i < 4; i++) {
            for (int x = 0; x < 2; x++) {
                int action = (x == 0) ? 13 : 14;
                deck.add(new Card(4, action));
            }
        }
    }

    private void createHotDrop() {
        for (int i = 0; i < 4; i++) {
            for (int x = 0; x < 2; x++) {
                int action = 15;
                deck.add(new Card(4, action));
            }
        }
    }

    private void createDuel() {
        for (int i = 0; i < 4; i++) {
            for (int x = 0; x < 2; x++) {
                int action = 16;
                deck.add(new Card(4, action));
            }
        }
    }

    private void createCardSpin() {
        for (int i = 0; i < 4; i++) {
            for (int x = 0; x < 2; x++) {
                int action = 17;
                deck.add(new Card(4, action));
            }
        }
    }

    //Get size of the deck
    private int getDeckSize() {
        return this.deck.size();
    }

    //Check if the deck empty
    public boolean isEmptyDeck() {
        if (this.deck.size() == 0)
            return true;
        else {
            return false;
        }
    }

    //Draws the top card from the take deck and adds it to player's deck
    public Card draw() {
        //Check if pile empty, if so, take top card, shuffle played deck and make take deck from it
        int takeDeckSize = deck.size();
        if (takeDeckSize == 0) {
            replaceTakeDeck();
        }

        Card lastCard = deck.getLast();
        deck.remove(lastCard);
        drawnCards.add(lastCard);

        return lastCard;
    }

    public void shuffle() {
        Collections.shuffle(this.deck);
    }

    public void replaceTakeDeck() {
        /*
        //Move top card in played deck to new deck

        //Make played deck the main deck and shuffle take deck

        //Make the new deck the played deck
        */

        deck = drawnCards;
        drawnCards = new LinkedList<>();
        shuffle();
    }

    @Override
    public String toString() {
        return "Deck{" +
                "deck=" + deck +
                '}';
    }

}
