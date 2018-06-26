package com.example.marti.unoplus;

import com.example.marti.unoplus.cards.Deck;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DeckTest {
    Deck deck;

    @Before
    public void setUp() throws Exception {
        deck = new Deck(true, true, true);

    }

    @After
    public void tearDown() throws Exception {
        deck = null;
    }

    @Test
    public void isEmptyDeck() {
        Assert.assertEquals(false,deck.isEmptyDeck());
        for(int i = 0; i < 132; i++)
            deck.draw();

        Assert.assertEquals(true,deck.isEmptyDeck());
    }

    @Test
    public void draw() {
        Assert.assertNotNull(deck.draw());
    }

    @Test
    public void replaceTakeDeck() {
        deck.draw();
        for(int i = 0; i < 131; i++)
            deck.draw();

        deck.replaceTakeDeck();
        Assert.assertEquals(false,deck.isEmptyDeck());
    }
}