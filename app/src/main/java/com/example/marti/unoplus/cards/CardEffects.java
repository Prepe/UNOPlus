package com.example.marti.unoplus.cards;

import com.example.marti.unoplus.GameActions;
import com.example.marti.unoplus.gameLogicImpl.GameController;
import com.example.marti.unoplus.gameLogicImpl.GameLogic;
import com.example.marti.unoplus.players.Player;

/**
 * Created by marti on 10.04.2018.
 */

public class CardEffects {
    GameLogic gameLogic;
    GameController gameController;

    public CardEffects(GameLogic gL, GameController gC) {
        gameLogic = gL;
        gameController = gC;
    }

    //Method to call the cards effect
    public void cardEffect(Player player, Card cardValue) {
        if (player == null) {
            player = gameLogic.getActivePlayer();
        }
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
                askForColorWish(player);
                break;
            case PLUS_FOUR:
                takeFour();
                askForColorWish(player);
                break;
            case HOT_DROP:
                hotDrop();
                break;
        }

        gameController.gA = new GameActions(GameActions.actions.NEXT_PLAYER, gameLogic.getActivePlayer().getID());
        gameController.update();
    }

    //TakeTwo Card effect method
    private void takeTwo() {
        gameLogic.changeCardDrawCount(2);
    }

    //ChangeColour Card effect method
    private void askForColorWish(Player player) {
        if (player != null) {
            gameController.gA = new GameActions(GameActions.actions.WISH_COLOR,player.getID(),true);
            gameController.update();
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

    //Hot Drop Card effect method
    private void hotDrop(){ gameLogic.playHotDrop();}
}
