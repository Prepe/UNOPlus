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
public class PlayCardTest {

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
    public void playCardTest() {
    /*    PowerMockito.mockStatic(Log.class);


        Player player1 = new Player(0);
        Player player2 = new Player(1);
        LinkedList<Player> list = new LinkedList<>();
        list.add(player1);
        list.add(player2);
        PlayerList playerList = new PlayerList();
        playerList.setPlayers(list);
        Card card1 = new Card(Card.colors.WILD, Card.values.ONE);
        gameController.setPlayerList(playerList);
        gameController.setUpGame();



        //Test if

        GameActions testgameAction1 = new GameActions(GameActions.actions.PLAY_CARD, 1, card1, false);
        GameActions expected1 = new GameActions(GameActions.actions.NEXT_PLAYER, 1);

        doNothing().when(gameViewProt).updateAllConnected(testgameAction1);

        gameController.logic.setLastCardColor(Card.colors.GREEN);
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
*///TODO Fix Test
    }
}
