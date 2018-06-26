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
public class EndDuelTest {

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
}
