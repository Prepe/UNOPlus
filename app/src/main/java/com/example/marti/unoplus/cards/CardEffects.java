package com.example.marti.unoplus.cards;

import com.example.marti.unoplus.GameActions;
import com.example.marti.unoplus.gameLogicImpl.GameControler;
import com.example.marti.unoplus.gameLogicImpl.GameLogic;
import com.example.marti.unoplus.players.Player;

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
                gameLogic.nextPlayer(player);
                break;
            case SKIP:
                skip();
                gameLogic.nextPlayer(player);
                break;
            case TURN:
                reverse();
                gameLogic.nextPlayer(player);
                break;
            case PLUS_TWO:
                takeTwo();
                gameLogic.nextPlayer(player);
                break;
            case CHOOSE_COLOR:
                changeColor(player);
                break;
            case PLUS_FOUR:
                takeFour();
                changeColor(player);
                break;
        }

        gameControler.gA = new GameActions(GameActions.actions.NEXT_PLAYER, gameLogic.getActivePlayer().getID());
        gameControler.updateAllPlayers();
    }

    //TakeTwo Card effect method
    private void takeTwo() {
        gameLogic.changeCardDrawCount(2);
    }

    //ChangeColour Card effect method
    private void changeColor(Player player) {
        if (player != null) {
            gameControler.gA = new GameActions(GameActions.actions.WISH_COLOR,player.getID(),true);
        }
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
