package com.example.marti.unoplus.cards;

/**
 * Created by marti on 05.06.2018.
 */

public class HandCardNode {
    int index;
    Card card;
    HandCardNode nextCard;
    HandCardNode prevCard;

    public HandCardNode(int index, Card card, HandCardNode next, HandCardNode prev) {
        this.index = index;
        this.card = card;
        this.nextCard = next;
        this.prevCard = prev;

        if (nextCard != null) {
            nextCard.fixPointer(null, this);
        }
        if (prevCard != null) {
            prevCard.fixPointer(this, null);
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

    public void fixPointer(HandCardNode nextCard, HandCardNode prevCard) {
        if (nextCard != null) {
            this.nextCard = nextCard;
        }
        if (prevCard != null) {
            this.prevCard = prevCard;
        }
    }

    void fixIndex() {
        if (prevCard != null) {
            index = prevCard.getIndex()+1;
        }
        if (nextCard != null) {
            nextCard.fixIndex();
        }
    }
}
