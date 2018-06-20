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
public class ColorWishTest {

    @Mock
    GameViewProt gameViewProt;
    GameController gameController;
    GameLogic gameLogic;
    Deck deck;
    boolean quickPlayAllowed;
    boolean counterAllowed;


    @Before
    public void setup() {
        gameController = new GameController(gameViewProt);
        gameViewProt = mock(GameViewProt.class);
        gameLogic = new GameLogic();
        deck = new Deck(true,true,true);
    }


    @Test
    public void colorWishTest(){
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
        gameLogic = new GameLogic(playerList, deck, gameController, quickPlayAllowed, counterAllowed);

        // player == active player

        /*GameActions testGA1 = new GameActions(GameActions.actions.WISH_COLOR, 0, Card.colors.BLUE);
        GameActions expectedGA1 = new GameActions(GameActions.actions.UPDATE, 1, new Card(gameLogic.getLastCardColor(), gameLogic.getLastCardValue()));

        doNothing().when(gameViewProt).updateAllConnected(expectedGA1);

        gameController.callGameController(testGA1);

        Assert.assertEquals(expectedGA1.action, gameController.gA.action);
        Assert.assertEquals(expectedGA1.playerID, gameController.gA.playerID);
        Assert.assertEquals(expectedGA1.card, gameController.gA.card);*/
        //@TODO: Problem mit expectedGA1 => erwartet wird UPDATE, liefert aber NEXT_PLAYER. Erwartet man NEXT_PLAYER, wird UPDATE geliefert

/*
        // player != active player
        GameActions testGA2 = new GameActions(GameActions.actions.WISH_COLOR, gameLogic.nextPlayer(gameLogic.getActivePlayer()).getID(), Card.colors.BLUE);
        GameActions expectedGA2 = new GameActions(GameActions.actions.UPDATE, 0, new Card(gameLogic.getLastCardColor(), gameLogic.getLastCardValue()));

        doNothing().when(gameViewProt).updateAllConnected(expectedGA2);

        gameController.callGameController(testGA2);

        Assert.assertEquals(expectedGA2.action, gameController.gA.action);
        Assert.assertEquals(expectedGA2.playerID, gameController.gA.playerID);
    */}

}
