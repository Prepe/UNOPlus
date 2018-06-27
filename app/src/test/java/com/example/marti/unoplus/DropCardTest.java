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
public class DropCardTest {

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
    }

    @Test
    public void dropCardTest() {
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
        LinkedList<Card> cards = new LinkedList<>();

        Card card1 = new Card(Card.colors.GREEN, Card.values.TWO);

        cards.add(card1);


        GameActions gameAction = new GameActions(GameActions.actions.DROP_CARD, 0);
        GameActions expected = new GameActions(GameActions.actions.DROP_CARD, 0);

        doNothing().when(gameViewProt).updateAllConnected(gameAction);

        gameController.callGameController(gameAction);

        Assert.assertEquals(expected.action, gameController.gA.action);

    }


   /* @Test
    public void dropCardTest2() {

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

        GameActions testgameAction1 = new GameActions(GameActions.actions.DROP_CARD, 0);
        GameActions expected1 = new GameActions(GameActions.actions.DROP_CARD, 0, true);

        doNothing().when(gameViewProt).updateAllConnected(testgameAction1);

        gameController.callGameController(testgameAction1);

        junit.framework.Assert.assertEquals(expected1.action, gameController.gA.action);
        junit.framework.Assert.assertEquals(expected1.nextPlayerID, gameController.gA.nextPlayerID);
        junit.framework.Assert.assertEquals(expected1.check, gameController.gA.check);


        //Test else
        gameController.setUpGame();


        GameActions testgameAction2 = new GameActions(GameActions.actions.DROP_CARD, 1);
        GameActions expected2 = new GameActions(GameActions.actions.DROP_CARD, 1, false);

        doNothing().when(gameViewProt).updateAllConnected(testgameAction2);

        gameController.callGameController(testgameAction2);

        junit.framework.Assert.assertEquals(expected2.action, gameController.gA.action);
        junit.framework.Assert.assertEquals(expected2.nextPlayerID, gameController.gA.nextPlayerID);
        junit.framework.Assert.assertEquals(expected2.check, gameController.gA.check);

    }*/


    @Test
    public void dropCardTest3(){
/*        PowerMockito.mockStatic(Log.class);

        Player player1 = new Player(0);
        Player player2 = new Player(1);
        LinkedList<Player> list = new LinkedList<>();
        list.add(player1);
        list.add(player2);
        PlayerList playerList = new PlayerList();
        playerList.setPlayers(list);
        gameController.setPlayerList(playerList);
        gameController.setUpGame();
        gameLogic = new GameLogic(playerList, deck, gameController,true,true);

        //if player hasn't cheated yet and is not the active player
        GameActions testGA1 = new GameActions(GameActions.actions.DROP_CARD, 1, 0, true);
        GameActions expectedGA1 = new GameActions(GameActions.actions.DROP_CARD, 0, true);

        doNothing().when(gameViewProt).updateAllConnected(expectedGA1);

        gameController.callGameController(testGA1);

        junit.framework.Assert.assertEquals(expectedGA1.action, gameController.gA.action);
        junit.framework.Assert.assertEquals(expectedGA1.playerID, gameController.gA.playerID);
        junit.framework.Assert.assertEquals(expectedGA1.check, gameController.gA.check);


        //else
        GameActions testGA2 = new GameActions(GameActions.actions.DROP_CARD, 0, 1, true);
        GameActions expectedGA2 = new GameActions(GameActions.actions.DROP_CARD, 1, false);

        doNothing().when(gameViewProt).updateAllConnected(expectedGA2);

        gameController.callGameController(testGA2);

        junit.framework.Assert.assertEquals(expectedGA2.action, gameController.gA.action);
        junit.framework.Assert.assertEquals(expectedGA2.playerID, gameController.gA.playerID);
        junit.framework.Assert.assertEquals(expectedGA2.check, gameController.gA.check);

    *///TODO fix Test
}
}
