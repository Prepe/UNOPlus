package com.example.marti.unoplus;

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

/**
 * Created by Luca on 16.06.2018.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class GamecontrollerDrawCardTest {

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
        deck = new Deck(true, true, true);
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

        if (gameController.gA.action.equals(GameActions.actions.NEXT_PLAYER)) {
            Assert.assertEquals(GameActions.actions.NEXT_PLAYER, gameController.gA.action);
        } else {
            Assert.assertEquals(expected2.action, gameController.gA.action);
        }

        Assert.assertEquals(expected2.nextPlayerID, gameController.gA.nextPlayerID);

        //test else

        gameController.setUpGame();

        GameActions testgameAction2 = new GameActions(GameActions.actions.DRAW_CARD, 1);

        GameActions expected = new GameActions(GameActions.actions.NEXT_PLAYER, 0);

        gameController.callGameController(testgameAction2);

        Assert.assertEquals(expected.action, gameController.gA.action);

        Assert.assertEquals(expected.playerID, gameController.gA.playerID);
    }

}
