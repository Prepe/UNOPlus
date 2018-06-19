package com.example.marti.unoplus;

import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.example.marti.unoplus.Screens.GameViewProt;
import com.example.marti.unoplus.cards.Card;
import com.example.marti.unoplus.cards.Deck;
import com.example.marti.unoplus.gameLogicImpl.GameController;
import com.example.marti.unoplus.gameLogicImpl.GameLogic;
import com.example.marti.unoplus.players.Player;
import com.example.marti.unoplus.players.PlayerList;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedList;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class CheckColorTest {

    @Mock
    GameViewProt gameViewProt;
    GameController gameController;
    GameLogic gameLogic;
    Deck deck;


    @Before
    public void setup() {
        gameController = new GameController(gameViewProt);
        gameViewProt = mock(GameViewProt.class);
        gameLogic = new GameLogic();
        deck = new Deck(true,true,true);
    }


    //Check for the colour of the card
    @VisibleForTesting
    boolean checkColor(Card card) {
        /*
         * Check for wild Card
         * Wild Cards should be playable no matter what
         * */
        if (card.getColor() == Card.colors.WILD) {
            return true;
        }
        /*
         * Check for matching Colour
         * */
        if (card.getColor() == gameLogic.getLastCardColor()) {
            return true;
        }

        /*
         * If the last Card was a Wild Card (Card.color == WILD) any Card can be played
         * */
        if (gameLogic.getLastCardColor() == Card.colors.WILD) {
            return true;
        }

        return false;
    }

    @Test
    public void checkColorTest(){
        PowerMockito.mockStatic(Log.class);

        Player player1 = new Player(0);
        Player player2 = new Player(1);
        LinkedList<Player> list = new LinkedList<>();
        list.add(player1);
        list.add(player2);
        PlayerList playerList = new PlayerList();
        playerList.setPlayers(list);

        Card card1 = new Card(Card.colors.RED, Card.values.FIVE);
        Card card2 = new Card(Card.colors.YELLOW, Card.values.PLUS_TWO);
        Card card3 = new Card(Card.colors.WILD, Card.values.PLUS_FOUR);

        gameLogic = new GameLogic(playerList, deck, gameController,true,true);
        gameController.setPlayerList(playerList);
        gameController.setUpGame();

        //if color == wild
        boolean actual1 = checkColor(card3);
        Assert.assertTrue(actual1);

        //if color == lastCardColor
        gameLogic.playTopCard(card1);
        boolean actual2 = checkColor(card1);
        Assert.assertTrue(actual2);

        //if lastCardColor == wild
        gameLogic.playTopCard(card3);
        boolean actual3 = checkColor(card3);
        Assert.assertTrue(actual3);

        //if color != wild && color != lastCardColor && lastCardColor != wild
        gameLogic.playTopCard(card1);
        boolean actual4 = checkColor(card2);
        Assert.assertFalse(actual4);
    }
}
