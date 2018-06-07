package com.example.marti.unoplus.cards;

import java.util.LinkedList;

/**
 * Created by marti on 05.06.2018.
 */

public class HandCardList {
    HandCardNode first;
    HandCardNode last;
    int nextIndex = 0;

    public int getCount() {
        return nextIndex;
    }

    public LinkedList<Card> getHand() {
        LinkedList<Card> handcards = new LinkedList<>();
        HandCardNode pointer = first;

        while (pointer != null) {
            handcards.add(pointer.getCard());
            pointer = pointer.getNext();
        }

        return handcards;
    }

    public boolean addCard(Card card) {
        if (first == null) {
            first = new HandCardNode(nextIndex,card,null,null);
            last = first;
            nextIndex++;

            return true;
        } else {
            HandCardNode pointer = first;
            Card pointerCard;
            while (pointer != null) {
                pointerCard = pointer.getCard();
                if (card.getColor().ordinal() < pointerCard.getColor().ordinal()) {
                    addCardInfont(card,pointer);
                    return true;
                } else if (card.getColor().ordinal() == pointerCard.getColor().ordinal()) {
                    if (card.getValue().ordinal() <= pointerCard.getValue().ordinal()) {
                        addCardInfont(card,pointer);
                        return true;
                    }
                }
                pointer = pointer.getNext();
            }
            HandCardNode temp = new HandCardNode(nextIndex,card,last.nextCard,last);
            last.fixNetxPointer(temp);
            last = temp;
            nextIndex++;
        }
        return false;
    }

    public boolean removeCard(Card card) {
        HandCardNode pointer = first;

        Card pointerCard;
        while (pointer != null) {
            pointerCard = pointer.getCard();
            if (card.getColor() == pointerCard.getColor()) {
                if (card.getValue() == pointerCard.getValue()) {
                    if (pointer.getNext() != null) {
                        pointer.getNext().fixPrevPointer(pointer.getPrev());
                    } else {
                        last = pointer.getPrev();
                    }
                    if (pointer.getPrev() != null) {
                        pointer.getPrev().fixNetxPointer(pointer.getNext());
                    } else {
                        first = pointer.getNext();
                    }
                    pointer.removePointer();
                    nextIndex--;
                    return true;
                }
            }
            pointer = pointer.getNext();
        }

        return false;
    }

    void addCardInfont(Card card, HandCardNode pointer) {
        new HandCardNode(pointer.getIndex(),card,pointer,pointer.getPrev());
        nextIndex++;
        if (pointer == first) {
            first = pointer.getPrev();
        }
    }
}
