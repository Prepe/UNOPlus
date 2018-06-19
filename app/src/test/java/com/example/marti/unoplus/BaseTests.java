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

import java.util.LinkedList;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Luca on 16.06.2018.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class BaseTests {

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
        //deck = new Deck();
    }


    @Test
    public void drawCardTest() {
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
        //Test if

        GameActions testgameAction1 = new GameActions(GameActions.actions.DRAW_CARD, 1);


        GameActions expected2 = new GameActions(GameActions.actions.DRAW_CARD, 1, cards);

        doNothing().when(gameViewProt).updateAllConnected(testgameAction1);

        gameController.callGameController(testgameAction1);


        Assert.assertEquals(expected2.action, gameController.gA.action);

        Assert.assertEquals(expected2.nextPlayerID, gameController.gA.nextPlayerID);

        //test else

        gameController.setUpGame();

        GameActions testgameAction2 = new GameActions(GameActions.actions.DRAW_CARD, 1);

        GameActions expected = new GameActions(GameActions.actions.NEXT_PLAYER, 1);

        gameController.callGameController(testgameAction2);

        Assert.assertEquals(expected.action, gameController.gA.action);

        Assert.assertEquals(expected.playerID, gameController.gA.playerID);
    }

    @Test
    public void playCardTest() {
        PowerMockito.mockStatic(Log.class);


        Player player1 = new Player(0);
        Player player2 = new Player(1);
        LinkedList<Player> list = new LinkedList<>();
        list.add(player1);
        list.add(player2);
        PlayerList playerList = new PlayerList();
        playerList.setPlayers(list);
        Card card1 = new Card(Card.colors.GREEN, Card.values.ONE);
        gameController.setPlayerList(playerList);
        gameController.setUpGame();



        //Test if

        GameActions testgameAction1 = new GameActions(GameActions.actions.PLAY_CARD, 1, card1, false);
        GameActions expected1 = new GameActions(GameActions.actions.NEXT_PLAYER, 1);

        doNothing().when(gameViewProt).updateAllConnected(testgameAction1);

        gameController.logic.setLastCardColor(Card.colors.WILD);
        gameController.logic.setLastCardValue(Card.values.FIVE);



        gameController.callGameController(testgameAction1);

        Assert.assertEquals(expected1.action, gameController.gA.action);
        Assert.assertEquals(expected1.playerID, gameController.gA.playerID);
        Assert.assertEquals(expected1.check, gameController.gA.check);

        //Test else
        gameController.setUpGame();

        GameActions testgameAction2 = new GameActions(GameActions.actions.PLAY_CARD, 1, card1, false);
        GameActions expected2 = new GameActions(GameActions.actions.PLAY_CARD, 1, false);

        doNothing().when(gameViewProt).updateAllConnected(testgameAction1);

        gameController.logic.setLastCardColor(Card.colors.RED);
        gameController.logic.setLastCardValue(Card.values.FIVE);

        gameController.callGameController(testgameAction2);

        Assert.assertEquals(expected2.action, gameController.gA.action);
        Assert.assertEquals(expected2.playerID, gameController.gA.playerID);
        Assert.assertEquals(expected2.check, gameController.gA.check);


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

        //Test if

        GameActions testgameAction1 = new GameActions(GameActions.actions.DROP_CARD, 0);
        GameActions expected1 = new GameActions(GameActions.actions.DROP_CARD, 0, true);

        doNothing().when(gameViewProt).updateAllConnected(testgameAction1);


        gameController.callGameController(testgameAction1);

        Assert.assertEquals(expected1.action, gameController.gA.action);
        Assert.assertEquals(expected1.nextPlayerID, gameController.gA.nextPlayerID);
        Assert.assertEquals(expected1.check, gameController.gA.check);


        //Test else
        gameController.setUpGame();


        GameActions testgameAction2 = new GameActions(GameActions.actions.DROP_CARD, 1);
        GameActions expected2 = new GameActions(GameActions.actions.DROP_CARD, 1, false);

        doNothing().when(gameViewProt).updateAllConnected(testgameAction2);


        gameController.callGameController(testgameAction2);

        Assert.assertEquals(expected2.action, gameController.gA.action);
        Assert.assertEquals(expected2.nextPlayerID, gameController.gA.nextPlayerID);
        Assert.assertEquals(expected2.check, gameController.gA.check);


    }

    @Test
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


    }

    @Test
    public void startDuelTest() {

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

        GameActions testgameAction1 = new GameActions(GameActions.actions.DUEL_START, 0, 1, Card.colors.GREEN);
        GameActions expected1 = new GameActions(GameActions.actions.DUEL_OPPONENT, 1, 0);

        doNothing().when(gameViewProt).updateAllConnected(testgameAction1);


        gameController.callGameController(testgameAction1);

        Assert.assertEquals(expected1.action, gameController.gA.action);
        Assert.assertEquals(expected1.nextPlayerID, gameController.gA.nextPlayerID);
        Assert.assertEquals(expected1.playerID, gameController.gA.playerID);
    }

    @Test
    public void endDuelTest() {

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

        GameActions testgameAction0 = new GameActions(GameActions.actions.DUEL_START, 0, 1, Card.colors.GREEN);
        doNothing().when(gameViewProt).updateAllConnected(testgameAction0);


        gameController.callGameController(testgameAction0);
        //Test if

        GameActions testgameAction1 = new GameActions(GameActions.actions.DUEL_OPPONENT, 0, 1, Card.colors.GREEN);
        GameActions expected1 = new GameActions(GameActions.actions.UPDATE, 0);

        doNothing().when(gameViewProt).updateAllConnected(testgameAction1);

        gameController.duelData.setLoserIDfortests(0);

        gameController.callGameController(testgameAction1);

        Assert.assertEquals(expected1.action, gameController.gA.action);


    }








    @Test
    public void callUnoTest(){
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
    /*
    @Test
    public void drawCardAsDuelLoser(){
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
        gameController = mock(GameController.class);
        gameLogic = new GameLogic(playerList, deck, gameController);

        //if deck is empty
        for (int i = 0; i < 132; i++) {
            deck.draw();
        }

        GameActions expectedGA1 = new GameActions(GameActions.actions.UPDATE, card1, gameLogic.nextPlayer(gameLogic.getActivePlayer()).getID());

        doNothing().when(gameViewProt).updateAllConnected(expectedGA1);

        gameController.drawCardAsDuelLoser(gameLogic.getActivePlayer().getID());

        Assert.assertEquals(expectedGA1.action, gameController.gA.action);

        //if deck is not empty
        deck = new Deck();

        GameActions expectedGA2 = new GameActions(GameActions.actions.UPDATE, card1, gameLogic.nextPlayer(gameLogic.getActivePlayer()).getID());

        doNothing().when(gameViewProt).updateAllConnected(expectedGA2);

        gameController.drawCardAsDuelLoser(gameLogic.getActivePlayer().getID());

        Assert.assertEquals(expectedGA2.action, gameController.gA.action);

    }


     @Test
    public void dropCardTest2(){
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
        gameLogic = new GameLogic(playerList, deck, gameController);

        //if player hasn't cheated yet and is not the active player
        GameActions testGA1 = new GameActions(GameActions.actions.DROP_CARD, 1, 0, true);
        GameActions expectedGA1 = new GameActions(GameActions.actions.DROP_CARD, 0, true);

        doNothing().when(gameViewProt).updateAllConnected(expectedGA1);

        gameController.callGameController(testGA1);

        Assert.assertEquals(expectedGA1.action, gameController.gA.action);
        Assert.assertEquals(expectedGA1.playerID, gameController.gA.playerID);
        Assert.assertEquals(expectedGA1.check, gameController.gA.check);


        //else
        GameActions testGA2 = new GameActions(GameActions.actions.DROP_CARD, 0, 1, true);
        GameActions expectedGA2 = new GameActions(GameActions.actions.DROP_CARD, 1, false);

        doNothing().when(gameViewProt).updateAllConnected(expectedGA2);

        gameController.callGameController(testGA2);

        Assert.assertEquals(expectedGA2.action, gameController.gA.action);
        Assert.assertEquals(expectedGA2.playerID, gameController.gA.playerID);
        Assert.assertEquals(expectedGA2.check, gameController.gA.check);

    }



     @Test
    public void colorWishTest(){
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
        gameLogic = new GameLogic(playerList, deck, gameController);

        // player == active player
        GameActions testGA1 = new GameActions(GameActions.actions.WISH_COLOR, 0, Card.colors.BLUE);
        GameActions expectedGA1 = new GameActions(GameActions.actions.UPDATE, 1, new Card(gameLogic.getLastCardColor(), gameLogic.getLastCardValue()));

        doNothing().when(gameViewProt).updateAllConnected(expectedGA1);

        gameController.callGameController(testGA1);

        Assert.assertEquals(expectedGA1.action, gameController.gA.action);
        Assert.assertEquals(expectedGA1.playerID, gameController.gA.playerID);
        Assert.assertEquals(expectedGA1.card, gameController.gA.card);
        //@TODO: Problem mit expectedGA1 => erwartet wird UPDATE, liefert aber NEXT_PLAYER. Erwartet man NEXT_PLAYER, wird UPDATE geliefert


        // player != active player
        GameActions testGA2 = new GameActions(GameActions.actions.WISH_COLOR, gameLogic.nextPlayer(gameLogic.getActivePlayer()).getID(), Card.colors.BLUE);
        GameActions expectedGA2 = new GameActions(GameActions.actions.UPDATE, 0, new Card(gameLogic.getLastCardColor(), gameLogic.getLastCardValue()));

        doNothing().when(gameViewProt).updateAllConnected(expectedGA2);

        gameController.callGameController(testGA2);

        Assert.assertEquals(expectedGA2.action, gameController.gA.action);
        Assert.assertEquals(expectedGA2.playerID, gameController.gA.playerID);
    }

*/

}
