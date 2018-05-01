package com.example.marti.unoplus.Server;

/**
 * Created by ekzhu on 30.04.2018.
 */

public class ServerLogic {

    public ServerLogic() {
        this.takedeck = new TakeDeck();
        this.takedeck.CreateCards();
        this.playdeck = new PlayDeck();
    }

    public int startingHand = 7;
    public TakeDeck takedeck = null;
    public PlayDeck playdeck = null;
}
