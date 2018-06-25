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

import static org.mockito.Mockito.mock;


@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class HotDropTest {

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
    public void hotDropTest(){
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

        long[] timestamps = {(long) 147, (long)587};

        LinkedList<Card> cards = new LinkedList<>();
        Card card1 = new Card(Card.colors.RED, Card.values.FIVE);
        Card card2 = new Card(Card.colors.YELLOW, Card.values.ONE);

        cards.add(card1);
        cards.add(card2);

        GameActions testGameActionPlayer1 = new GameActions(GameActions.actions.HOT_DROP, 0,timestamps[0]);
        GameActions testGameActionPlayer2 = new GameActions(GameActions.actions.HOT_DROP, 1, timestamps[1]);
        GameActions expectedPlayer = new GameActions(GameActions.actions.UPDATE, 1, cards);

        gameController.callGameController(testGameActionPlayer1);

        Assert.assertEquals(expectedPlayer.action, gameController.gA.action);

        gameController.callGameController(testGameActionPlayer2);

        Assert.assertEquals(expectedPlayer.action, gameController.gA.action);
    }
}
