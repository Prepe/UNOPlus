package com.example.marti.unoplus.cards;

import android.content.Intent;

import com.example.marti.unoplus.GameActions;
import com.example.marti.unoplus.Screens.GameViewProt;
import com.example.marti.unoplus.gameLogicImpl.GameController;
import com.example.marti.unoplus.gameLogicImpl.GameLogic;
import com.example.marti.unoplus.players.Player;
import com.example.marti.unoplus.players.PlayerList;

import java.lang.reflect.Array;
import java.util.LinkedList;

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
                hotDrop(player);
                break;
            case CARD_SPIN:
                cardSpin(player);
                break;
            case DUEL:
                startDuel(player);
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
            gameController.gA = new GameActions(GameActions.actions.WISH_COLOR, player.getID(), true);
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
    private void hotDrop(Player player) {
        gameController.gA = new GameActions(GameActions.actions.HOT_DROP, player.getID(), true);
        gameController.update();
    }

    private void cardSpin(Player player) {

        PlayerList players = gameLogic.getPlayerList();

        LinkedList<Card> cards;

        Player activePlayer = gameLogic.getActivePlayer();

        Integer activeID = activePlayer.getID();

        if (gameLogic.checkifreversed()) {

            cards = activePlayer.getHand();
            int i = activeID;
            int count = 0;


            Player givingPlayer;
            Player gettingPlayer;
            while (count < players.playerCount()-1) {
                gettingPlayer = players.getPlayer(i);
                givingPlayer = players.getPrevious(gettingPlayer);

                gettingPlayer.setHand(givingPlayer.getHand());

                if (i == 0) {
                    i = players.playerCount();
                } else {
                    i--;
                }
                count++;
            }

            activePlayer.setHand(cards);

            //players.getNext(players.getPlayer(i)).setHand(players.getPlayer(i).getHand());


        } else {

            cards = activePlayer.getHand();
            int i = activeID;
            int count = 0;


            Player givingPlayer;
            Player gettingPlayer;
            while (count < players.playerCount()-1) {
                gettingPlayer = players.getPlayer(i);
                givingPlayer = players.getPrevious(gettingPlayer);

                gettingPlayer.setHand(givingPlayer.getHand());

                if (i == 0) {
                    i = players.playerCount();
                } else {
                    i--;
                }
                count++;
            }

            activePlayer.setHand(cards);

        }

        gameController.gA = new GameActions(GameActions.actions.CARD_SPIN, player.getID(),true );
        gameController.update();
    }

    private void startDuel(Player player){
        if (player != null) {
            gameController.gA = new GameActions(GameActions.actions.DUEL_START, player.getID(), true);
            gameController.update();
        }
    }
}
