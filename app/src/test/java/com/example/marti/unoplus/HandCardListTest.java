package com.example.marti.unoplus;

import com.example.marti.unoplus.cards.Card;
import com.example.marti.unoplus.cards.HandCardList;
import com.example.marti.unoplus.cards.HandCardNode;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by marti on 20.06.2018.
 */

public class HandCardListTest {
    HandCardList handcards;
    Card red0 = new Card(Card.colors.RED, Card.values.ZERO);
    Card red1 = new Card(Card.colors.RED, Card.values.ONE);
    Card redTake2 = new Card(Card.colors.RED, Card.values.PLUS_TWO);
    Card blue0 = new Card(Card.colors.BLUE, Card.values.ZERO);
    Card green0 = new Card(Card.colors.GREEN, Card.values.ZERO);
    Card yello0 = new Card(Card.colors.YELLOW, Card.values.ZERO);
    Card wildColor = new Card(Card.colors.WILD, Card.values.CHOOSE_COLOR);
    Card wildTake4 = new Card(Card.colors.WILD, Card.values.PLUS_FOUR);
    Card[] sortedCards = {red0, red1, redTake2, blue0, green0, yello0, wildColor, wildTake4};

    @Before
    public void setUp() {
        handcards = new HandCardList();
    }

    @After
    public void cleanUp() {
        handcards = null;
    }

    @Test
    public void emptyHandTest() {
        Assert.assertEquals(0, handcards.getCount());
    }

    @Test
    public void addCardToHandTest() {
        Assert.assertEquals(true,handcards.addCard(red0));
        Assert.assertEquals(1, handcards.getCount());
        Assert.assertEquals(red0, handcards.getHand().getFirst());
    }

    @Test
    public void removeCardFromHandTest() {
        handcards.addCard(red0);
        Assert.assertEquals(1, handcards.getCount());

        Assert.assertEquals(true, handcards.removeCard(red0));
    }

    @Test
    public void removeCardFromHandTwiceTest() {
        handcards.addCard(red0);
        Assert.assertEquals(1, handcards.getCount());

        handcards.removeCard(red0);
        Assert.assertEquals(false, handcards.removeCard(red0));
    }

    @Test
    public void removeCardFromHandNotFoundTest() {
        Assert.assertEquals(false, handcards.removeCard(red0));
    }

    @Test
    public void addMultibleCardsToHandTest() {
        for (int i = 0; i < sortedCards.length; i++) {
            handcards.addCard(sortedCards[i]);
        }

        Assert.assertEquals(sortedCards.length, handcards.getCount());
    }

    @Test
    public void addRemoveMultibleCardsTest() {
        int count = sortedCards.length;
        addSortetCardsToHand();

        handcards.removeCard(red0);
        handcards.removeCard(blue0);
        handcards.removeCard(redTake2);
        count -= 3;

        handcards.addCard(wildColor);
        handcards.addCard(wildTake4);
        count += 2;

        Assert.assertEquals(count, handcards.getCount());
    }

    @Test
    public void sortedCardTest() {
        for (int i = 0; i < 10; i++) {
            addSortetCardsToHand();
        }

        Assert.assertEquals(true, sorted());
    }

    @Test
    public void handCardsTest() {
        addSortetCardsToHand();
        LinkedList<Card> hand = handcards.getHand();

        for (int i = 0; i < hand.size(); i++) {
            Assert.assertEquals(sortedCards[i], hand.get(i));
        }
    }

    @Test
    public void stressTest() {
        int counter = 0;
        int expectedCount = 0;

        while (counter < 10000) {
            double rand = Math.random();

            if (rand < 0.5) {
                rand = Math.random() * sortedCards.length - 1;
                handcards.addCard(sortedCards[(int)rand]);
                expectedCount++;
            } else {
                rand = Math.random() * sortedCards.length - 1;

                if (handcards.removeCard(sortedCards[(int)rand])) {
                    expectedCount--;
                }
            }

            counter++;
        }

        Assert.assertEquals(expectedCount,handcards.getCount());
    }

    void addSortetCardsToHand() {
        for (int j = 0; j < sortedCards.length; j++) {
            handcards.addCard(sortedCards[j]);
        }
    }

    boolean sorted() {
        HandCardNode pointer = handcards.getFirst();
        Card pointerCard;
        Card checkCard;

        while (pointer.getNext() != null) {
            pointerCard = pointer.getCard();
            checkCard = pointer.getNext().getCard();

            if (pointerCard.getValue().ordinal() >= checkCard.getValue().ordinal()) {
                if (pointerCard.getColor().ordinal() > checkCard.getColor().ordinal()) {
                    return false;
                }
            }
            pointer = pointer.getNext();
        }

        return true;
    }
}
