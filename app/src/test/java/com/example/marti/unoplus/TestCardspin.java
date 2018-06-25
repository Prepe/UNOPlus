package com.example.marti.unoplus;

import android.util.Log;

import com.example.marti.unoplus.Screens.GameViewProt;
import com.example.marti.unoplus.cards.Card;
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
public class TestCardspin {

    @Mock
    GameViewProt gameViewProt;
    GameController gameController;
    GameLogic gameLogic;

    @Before
    public void setup() {
        boolean[] temp = {true,true,true,true,true,true,true};
        gameController = new GameController(gameViewProt,temp);
        gameViewProt = mock(GameViewProt.class);
        gameLogic = new GameLogic();

    }

    @Test
    public void firstGameactionsend() {
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

        GameActions testgameAction = new GameActions(GameActions.actions.CARD_SPIN, 0);
        testgameAction.playerID = 0;
        GameActions expected = new GameActions(GameActions.actions.CARD_SPIN, 1);
        expected.playerID = 1;

        doNothing().when(gameViewProt).updateAllConnected(testgameAction);

        gameController.callGameController(testgameAction);

        Assert.assertEquals(gameController.gA.action, expected.action);
        Assert.assertEquals(gameController.gA.playerID, expected.playerID);
    }

    @Test
    public void firstGameactionsendReversed() {
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

        GameActions testgameAction = new GameActions(GameActions.actions.CARD_SPIN, 0);
        testgameAction.playerID = 1;
        GameActions expected = new GameActions(GameActions.actions.CARD_SPIN, 1);
        expected.playerID = 0;

        doNothing().when(gameViewProt).updateAllConnected(testgameAction);

        gameLogic.toggleReverse();
        gameController.callGameController(testgameAction);

        Assert.assertEquals(gameController.gA.action, expected.action);

        Assert.assertEquals(gameController.gA.playerID, expected.playerID);
        gameLogic.toggleReverse();
    }

    /*
    Wird nicht gebraucht GOTHand und GIVEHand waren überbleibsel nicht genutzten codes
    @Test
    public void savegottencardsTest() {

        Card card1 = new Card(Card.colors.GREEN, Card.values.ONE);
        Card card2 = new Card(Card.colors.GREEN, Card.values.TWO);
        Card card3 = new Card(Card.colors.GREEN, Card.values.FIVE);
        LinkedList<Card> cards = new LinkedList<>();

        cards.add(card1);
        cards.add(card2);
        cards.add(card3);

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


        GameActions testgameAction = new GameActions(GameActions.actions.GIVE_Hand, 0, cards);
        GameActions expected = new GameActions(GameActions.actions.GOT_Hand, 0);

        doNothing().when(gameViewProt).updateAllConnected(testgameAction);

        gameController.callGameController(testgameAction);

        Assert.assertEquals(cards, gameController.getGottenHandsCards().get(0));
        Assert.assertEquals(expected.action, gameController.gA.action);


    }

    @Test
    public void spinTest() {

        Card card1 = new Card(Card.colors.GREEN, Card.values.ONE);
        Card card2 = new Card(Card.colors.GREEN, Card.values.TWO);
        Card card3 = new Card(Card.colors.GREEN, Card.values.FIVE);
        LinkedList<Card> cards1 = new LinkedList<>();
        LinkedList<Card> cards2 = new LinkedList<>();


        cards1.add(card1);
        cards1.add(card1);
        cards1.add(card1);
        cards2.add(card3);
        cards2.add(card3);
        cards2.add(card3);

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
        gameLogic.toggleReverse();

        GameActions savecards1 = new GameActions(GameActions.actions.GIVE_Hand, 0, cards1);
        GameActions savecards2 = new GameActions(GameActions.actions.GIVE_Hand, 1, cards2);
        GameActions expected1 = new GameActions(GameActions.actions.GET_NEWHand, 0, cards2);
        GameActions expected2 = new GameActions(GameActions.actions.GET_NEWHand, 1, cards1);

        gameController.callGameController(savecards1);

        GameActions testgameAction1 = new GameActions(GameActions.actions.DO_CardSpin, 0, cards1);
        GameActions testgameAction2 = new GameActions(GameActions.actions.DO_CardSpin, 1, cards2);
        doNothing().when(gameViewProt).updateAllConnected(testgameAction1);

        gameController.callGameController(testgameAction1);


        Assert.assertEquals(expected1.action, gameController.gA.action);
        Assert.assertEquals(expected1.playerID, gameController.gA.playerID);
        for (int i = 0; i < 3; i++) {
            Assert.assertEquals(expected2.cards.get(i).getValue(), gameController.gA.cards.get(i).getValue());
        }

        gameController.callGameController(savecards2);

        gameController.callGameController(testgameAction2);

        Assert.assertEquals(expected1.action, gameController.gA.action);
        Assert.assertEquals(expected1.playerID, gameController.gA.playerID);

        for (int i = 0; i < 3; i++) {
            Assert.assertEquals(expected1.cards.get(i).getValue(), gameController.gA.cards.get(i).getValue());
        }

    }

    @Test
    public void spinTestReverse() {

        Card card1 = new Card(Card.colors.GREEN, Card.values.ONE);
        Card card2 = new Card(Card.colors.GREEN, Card.values.TWO);
        Card card3 = new Card(Card.colors.GREEN, Card.values.FIVE);
        LinkedList<Card> cards1 = new LinkedList<>();
        LinkedList<Card> cards2 = new LinkedList<>();


        cards1.add(card1);
        cards1.add(card1);
        cards1.add(card1);
        cards2.add(card3);
        cards2.add(card3);
        cards2.add(card3);

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

        GameActions savecards1 = new GameActions(GameActions.actions.GIVE_Hand, 0, cards1);
        GameActions savecards2 = new GameActions(GameActions.actions.GIVE_Hand, 1, cards2);
        GameActions expected1 = new GameActions(GameActions.actions.GET_NEWHand, 0, cards2);
        GameActions expected2 = new GameActions(GameActions.actions.GET_NEWHand, 1, cards1);

        gameController.callGameController(savecards1);

        GameActions testgameAction1 = new GameActions(GameActions.actions.DO_CardSpin, 0, cards1);
        GameActions testgameAction2 = new GameActions(GameActions.actions.DO_CardSpin, 1, cards2);
        doNothing().when(gameViewProt).updateAllConnected(testgameAction1);

        gameController.callGameController(testgameAction1);


        Assert.assertEquals(expected1.action, gameController.gA.action);
        Assert.assertEquals(expected1.playerID, gameController.gA.playerID);
        for (int i = 0; i < 3; i++) {
            Assert.assertEquals(expected2.cards.get(i).getValue(), gameController.gA.cards.get(i).getValue());
        }

        gameController.callGameController(savecards2);

        gameController.callGameController(testgameAction2);

        Assert.assertEquals(expected1.action, gameController.gA.action);
        Assert.assertEquals(expected1.playerID, gameController.gA.playerID);

        for (int i = 0; i < 3; i++) {
            Assert.assertEquals(expected1.cards.get(i).getValue(), gameController.gA.cards.get(i).getValue());
        }

    }
    */
}
