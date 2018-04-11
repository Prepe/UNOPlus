package com.example.marti.unoplus.cards;

import com.example.marti.unoplus.gameLogicImpl.GameControler;
import com.example.marti.unoplus.gameLogicImpl.GameLogic;

import static com.example.marti.unoplus.cards.Card.values.SKIP;

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
    public void cardEffect(Card cardValue) {
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
                changeColour();
                break;
            case PLUS_FOUR:
                takeFourChangeColour();
                break;
        }
    }

    //TakeTwo Card effect method
    private void takeTwo() {
        gameLogic.changeCardDrawCount(2);
    }

    //ChangeColour Card effect method
    private void changeColour() {
        //TODO implement
    }

    //TakeFourChangeColour Card effect method
    private void takeFourChangeColour() {
        gameLogic.changeCardDrawCount(4);
    }

    //Reverse Card effect method
    private void reverse() {
      //TODO implement
    }

    //Suspend Card effect method
    private void skip() {
        gameLogic.skipNext();
    }
}
