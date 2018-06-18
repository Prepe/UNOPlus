package com.example.marti.unoplus.cards;

/**
 * Created by marti on 05.06.2018.
 */

public class HandCardNode {
    private int index;
    private Card card;
    HandCardNode nextCard;
    HandCardNode prevCard;

    public HandCardNode(int index, Card card, HandCardNode next, HandCardNode prev) {
        this.index = index;
        this.card = card;
        this.nextCard = next;
        this.prevCard = prev;

        if (nextCard != null) {
            nextCard.fixPrevPointer( this);
        }
        if (prevCard != null) {
            prevCard.fixNetxPointer(this);
        }
        fixIndex();
    }

    public int getIndex() {
        return index;
    }

    public Card getCard() {
        return card;
    }

    public HandCardNode getNext() {
        return nextCard;
    }

    public HandCardNode getPrev() {
        return prevCard;
    }

    public void fixNetxPointer(HandCardNode nextCard) {
        this.nextCard = nextCard;
    }

    public void fixPrevPointer(HandCardNode prevCard) {
        this.prevCard = prevCard;
    }

    void fixIndex() {
        if (prevCard != null) {
            index = prevCard.getIndex() + 1;
        }
        if (nextCard != null) {
            nextCard.fixIndex();
        }
    }

    public void removePointer() {
        nextCard = null;
        prevCard = null;
    }
}
