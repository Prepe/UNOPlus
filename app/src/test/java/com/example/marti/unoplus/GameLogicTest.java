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
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Method;
import java.util.LinkedList;

import static org.mockito.Mockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class GameLogicTest {

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

    @Test
    public void nextPlayerTest(){
        PowerMockito.mockStatic(Log.class);

        Player player1 = new Player(0);
        Player player2 = new Player(1);
        Player player3 = null;
        Player player4 = new Player(2);
        LinkedList<Player> list = new LinkedList<>();
        list.add(player1);
        list.add(player2);
        list.add(player3);
        list.add(player4);
        PlayerList playerList = new PlayerList();
        playerList.setPlayers(list);

        gameLogic = new GameLogic(playerList, deck, gameController,true,true);

        //if player == null
        gameLogic.nextPlayer(player3);
        Assert.assertEquals(player2, gameLogic.getActivePlayer());

        //if !reverse && !skip
        gameLogic.nextPlayer(player1);
        Assert.assertEquals(player2, gameLogic.getActivePlayer());

        //if !reverse && skip
        gameLogic.skipNext();
        gameLogic.nextPlayer(player2);
        Assert.assertEquals(player4, gameLogic.getActivePlayer());

        //if reverse && !skip
        gameLogic.toggleReverse();
        gameLogic.nextPlayer(player1);
        Assert.assertEquals(player4, gameLogic.getActivePlayer());

        //if reverse && skip
        gameLogic.skipNext();
        gameLogic.nextPlayer(player2);
        Assert.assertEquals(player4, gameLogic.getActivePlayer());
    }

    @Test
    public void checkCardTest(){
        PowerMockito.mockStatic(Log.class);

        Player player1 = new Player(0);
        Player player2 = new Player(1);
        Player player3 = new Player(2);
        LinkedList<Player> list = new LinkedList<>();
        list.add(player1);
        list.add(player2);
        list.add(player3);
        PlayerList playerList = new PlayerList();
        playerList.setPlayers(list);

        Card card1 = new Card(Card.colors.RED, Card.values.FIVE);
        Card card2 = new Card(Card.colors.YELLOW, Card.values.PLUS_TWO);
        Card card3 = new Card(Card.colors.WILD, Card.values.PLUS_FOUR);
        Card card4 = new Card(Card.colors.GREEN, Card.values.FIVE);
        Card card5 = new Card(Card.colors.RED, Card.values.SEVEN);

        gameLogic = new GameLogic(playerList, deck, gameController,true,true);
        gameController.setPlayerList(playerList);
        gameController.setUpGame();

        //if active player && drawCount > 1 && cardValue = PlusFour
        gameLogic.changeCardDrawCount(2);
        gameLogic.playTopCard(card3);
        Player player = gameLogic.getActivePlayer();
        boolean actual1 = gameLogic.checkCard(card3, player);
        Assert.assertTrue(actual1);

        //if active player && drawCount > 1 && cardValue = PlusTwo && lastCardValue = PlusTwo
        gameLogic.playTopCard(card2);
        player = gameLogic.getActivePlayer();
        boolean actual2 = gameLogic.checkCard(card2, player);
        Assert.assertTrue(actual2);

        //if active player && drawCount > 1 && cardValue = PlusTwo && lastCardValue = PlusFour
        gameLogic.playTopCard(card3);
        player = gameLogic.getActivePlayer();
        boolean actual3 = gameLogic.checkCard(card2, player);
        Assert.assertTrue(actual3);

        //if active player && drawCount <= 1 && cardValue == cardValue
        gameLogic.changeCardDrawCount(1);
        gameLogic.playTopCard(card1);
        player = gameLogic.getActivePlayer();
        boolean actual4 = gameLogic.checkCard(card4, player);
        Assert.assertTrue(actual4);

        //if active player && drawCount <= 1 && cardColor == cardColor
        gameLogic.playTopCard(card5);
        player = gameLogic.getActivePlayer();
        boolean actual5 = gameLogic.checkCard(card5, player);
        Assert.assertTrue(actual5);

        //if cardValue == cardValue && cardColor == cardColor
        gameLogic.playTopCard(card4);
        boolean actual6 = gameLogic.checkCard(card4, player1);
        Assert.assertTrue(actual6);

        //if !active player && cardValue != cardValue && cardColor != cardColor
        gameLogic.playTopCard(card1);
        boolean actual7 = gameLogic.checkCard(card2, player1);
        Assert.assertFalse(actual7);
    }


    //Check for the colour of the card
    @VisibleForTesting boolean checkColor(Card card) {
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


    @VisibleForTesting boolean checkValue(Card card) {
        //Check for matching Value
        if (card.getValue() == gameLogic.getLastCardValue()) {
            return true;
        }
        return false;
    }

    @Test
    public void checkValueTest(){
        PowerMockito.mockStatic(Log.class);

        Player player1 = new Player(0);
        Player player2 = new Player(1);
        LinkedList<Player> list = new LinkedList<>();
        list.add(player1);
        list.add(player2);
        PlayerList playerList = new PlayerList();
        playerList.setPlayers(list);

        Card card1 = new Card(Card.colors.RED, Card.values.FIVE);
        Card card2 = new Card(Card.colors.YELLOW, Card.values.FIVE);
        Card card3 = new Card(Card.colors.GREEN, Card.values.THREE);

        gameLogic = new GameLogic(playerList, deck, gameController,true,true);
        gameController.setPlayerList(playerList);
        gameController.setUpGame();

        //if value == lastCardValue
        gameLogic.playTopCard(card1);
        boolean actual1 = checkValue(card2);
        Assert.assertTrue(actual1);

        //if value != lastCardValue
        gameLogic.playTopCard(card1);
        boolean actual2 = checkValue(card3);
        Assert.assertFalse(actual2);
    }

}
