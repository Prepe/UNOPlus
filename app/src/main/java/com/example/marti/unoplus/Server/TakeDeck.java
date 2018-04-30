package com.example.marti.unoplus.Server;

import com.example.marti.unoplus.cards.Card;

import java.util.ArrayList;

/**
 * Created by ekzhu on 30.04.2018.
 */

public class TakeDeck {

    ArrayList<Card> cards = null;

    public TakeDeck()
    {
        this.cards = new ArrayList<Card>();
    }


    //Here all cards will be created
    public void CreateCards()
    {
        //Dummy loop, replace with actual card creating code later
        for(int i = 0; i < 30; i++)
        {
            this.cards.add(new Card(Card.colors.BLUE, Card.values.EIGHT)); //dummy cards
        }
    }

    public Card TakeOneCard()
    {
        Card card = this.cards.remove(0);
        if (this.cards.isEmpty())
        {

        }
        return card;
    }
}
