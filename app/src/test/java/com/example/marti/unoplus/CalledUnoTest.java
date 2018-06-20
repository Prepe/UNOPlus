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

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class CalledUnoTest {

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

    /*@Test
    public void calledUnoTest() {

        PowerMockito.mockStatic(Log.class);


        Player player1 = new Player(0);
        Player player2 = new Player(1);
        LinkedList<Player> list = new LinkedList<>();
        list.add(player1);
        list.add(player2);
        PlayerList playerList = new PlayerList();
        playerList.setPlayers(list);

        gameController.setPlayerList(playerList);
        gameController.setUpGame();

        //Test if

        GameActions testgameAction1 = new GameActions(GameActions.actions.CALL_UNO, 0);
        GameActions expected1 = new GameActions(GameActions.actions.CALL_UNO, 0, true);

        doNothing().when(gameViewProt).updateAllConnected(testgameAction1);


        gameController.callGameController(testgameAction1);

        Assert.assertEquals(expected1.action, gameController.gA.action);
        Assert.assertEquals(expected1.nextPlayerID, gameController.gA.nextPlayerID);
        Assert.assertEquals(expected1.check, gameController.gA.check);


        //Test else


        GameActions testgameAction2 = new GameActions(GameActions.actions.CALL_UNO, 0);
        GameActions expected2 = new GameActions(GameActions.actions.CALL_UNO, 0, false);
        doNothing().when(gameViewProt).updateAllConnected(testgameAction2);


        gameController.callGameController(testgameAction2);

        Assert.assertEquals(expected2.action, gameController.gA.action);
        Assert.assertEquals(expected2.nextPlayerID, gameController.gA.nextPlayerID);
        Assert.assertEquals(expected2.check, gameController.gA.check);

    }*/


    @Test
    public void calledUnoTest2(){
        PowerMockito.mockStatic(Log.class);

        Player player1 = new Player(0);
        Player player2 = new Player(1);
        LinkedList<Player> list = new LinkedList<>();
        list.add(player1);
        list.add(player2);
        PlayerList playerList = new PlayerList();
        playerList.setPlayers(list);
        LinkedList<Card> cards = new LinkedList<>();
        Card card1 = new Card(Card.colors.GREEN, Card.values.ONE);
        gameController.setPlayerList(playerList);
        gameController.setUpGame();
        gameLogic = new GameLogic(playerList, deck, gameController,true,true);

        //if
        GameActions testGA1 = new GameActions(GameActions.actions.CALL_UNO, 1);
        GameActions expectedGA1 = new GameActions(GameActions.actions.CALL_UNO, 1, true);

        doNothing().when(gameViewProt).updateAllConnected(expectedGA1);

        gameController.callGameController(testGA1);

        Assert.assertEquals(expectedGA1.action, gameController.gA.action);
        Assert.assertEquals(expectedGA1.playerID, gameController.gA.playerID);
        Assert.assertEquals(expectedGA1.check, gameController.gA.check);

        //else
        GameActions testGA2 = new GameActions(GameActions.actions.CALL_UNO, 1);
        GameActions expectedGA2 = new GameActions(GameActions.actions.DRAW_CARD, 1, cards);

        doNothing().when(gameViewProt).updateAllConnected(expectedGA2);

        gameController.callGameController(testGA2);

        Assert.assertEquals(expectedGA2.action, gameController.gA.action);
        Assert.assertEquals(expectedGA2.playerID, gameController.gA.playerID);
        Assert.assertEquals(expectedGA2.check, gameController.gA.check);
    }
}
