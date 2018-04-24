package com.example.marti.unoplus.cards;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ekzhu on 02.04.2018.
 */

public class Deck {

    public ArrayList<Card> deck;

    public Deck() {
        this.deck = new ArrayList<>();
        decksinit();
        buildDeck();
    }

    Deck playeddeck; //deck where players take cards from ('graveyard')
    Deck takedeck;  //deck where players take cards from

    private void decksinit() {
        this.takedeck = new Deck();      // create deck where players take cards from
        this.playeddeck = new Deck();      // create deck where players put cards down
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
        Collections.shuffle(takedeck.deck);
        System.out.println(this.takedeck.deck.size() + " cards created and put in takedeck");
    }

    //Create cards that occur in each color
    private void createNormalCards() {
        for (int color = 0; color < 4; color++) {
            for (int num = 0; num < 13; num++) {
                int x = (num == 0) ? 1 : 2;
                for (int i = 0; i < x; i++) {
                    takedeck.deck.add(new Card(color, num));
                }
            }
        }
    }

    //Create wild cards
    private void createWildCards() {
        for (int i = 0; i < 4; i++) {
            for (int x = 0; x < 2; x++) {
                int action = (x == 0) ? 13 : 14;
                takedeck.deck.add(new Card(5, action));
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
        int takeDeckSize = takedeck.getDeckSize();
        if (takeDeckSize == 0) {
            replaceTakeDeck();
        }

        int lastIndex = takeDeckSize - 1;
        Card lastCard = takedeck.deck.get(lastIndex);
        takedeck.deck.remove(lastCard);
        this.deck.add(lastCard);

        return lastCard;
    }

    public void move(Card card, Deck originalDeck, Deck newDeck) {
        originalDeck.deck.remove(card);
        newDeck.deck.add(card);
    }

    public void shuffle() {
        Collections.shuffle(this.deck);
    }

    public Card getLastCardPlayed() {
        int deckSize = playeddeck.deck.size() - 1;
        return playeddeck.deck.get(deckSize);
    }

    public void replaceTakeDeck() {
        //Move top card in played deck to new deck
        Deck newDeck = new Deck();
        move(getLastCardPlayed(), playeddeck, newDeck);

        //Make played deck the main deck and shuffle take deck
        takedeck = playeddeck;
        takedeck.shuffle();

        //Make the new deck the played deck
        playeddeck = newDeck;

    }

    @Override
    public String toString() {
        return "Deck{" +
                "deck=" + deck +
                '}';
    }

}
