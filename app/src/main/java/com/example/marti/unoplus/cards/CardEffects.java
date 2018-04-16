package com.example.marti.unoplus.cards;

import com.example.marti.unoplus.Card;
import com.example.marti.unoplus.gameLogicImpl.GameControler;
import com.example.marti.unoplus.gameLogicImpl.GameLogic;
import com.example.marti.unoplus.Player;

/**
 * Created by marti on 10.04.2018.
 */

public class CardEffects {
    GameLogic gameLogic;
    GameControler gameControler;

    public CardEffects(GameLogic gL, GameControler gC) {
        gameLogic = gL;
        gameControler = gC;
    }

    //Method to call the cards effect
    public void cardEffect(Player player, Card cardValue) {
        switch (cardValue.getValue()) {
            default:
                //No Card effect!!
                break;
            case SKIP:
                skip();
                break;
            case TURN:
                reverse();
                break;
            case PLUS_TWO:
                takeTwo();
                break;
            case CHOOSE_COLOR:
                changeColor(player);
                break;
            case PLUS_FOUR:
                takeFour();
                changeColor(player);
                break;
        }
    }

    //TakeTwo Card effect method
    private void takeTwo() {
        gameLogic.changeCardDrawCount(2);
    }

    //ChangeColour Card effect method
    private void changeColor(Player player) {
        Card.colors colorWish = Card.colors.WILD;
        if (player != null) {
            //colorWish = player.wishForColor();
        }
        gameLogic.wishColor(colorWish);
    }

    //TakeFourChangeColour Card effect method
    private void takeFour() {
        gameLogic.changeCardDrawCount(4);
    }

    //Reverse Card effect method
    private void reverse() {
      gameLogic.toggleReverse();
    }

    //Suspend Card effect method
    private void skip() {
        gameLogic.skipNext();
    }
}
