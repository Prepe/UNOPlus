package com.example.marti.unoplus;

import android.util.Log;

import com.example.marti.unoplus.Screens.GameViewProt;
import com.example.marti.unoplus.cards.Card;
import com.example.marti.unoplus.cards.Deck;
import com.example.marti.unoplus.gameLogicImpl.GameController;
import com.example.marti.unoplus.gameLogicImpl.GameLogic;
import com.example.marti.unoplus.players.Player;
import com.example.marti.unoplus.players.PlayerList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.LinkedList;

import static org.mockito.Mockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class CheckCardTest {

    @Mock
    GameViewProt gameViewProt;
    GameController gameController;
    GameLogic gameLogic;
    Deck deck;


    @Before
    public void setup() {
        boolean[] temp = {true,true,true,true,true,true,true};
        gameController = new GameController(gameViewProt,temp);
        gameViewProt = mock(GameViewProt.class);
        gameLogic = new GameLogic();
        deck = new Deck(true,true,true);
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
}
