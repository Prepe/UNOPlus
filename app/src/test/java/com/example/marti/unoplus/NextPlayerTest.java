package com.example.marti.unoplus;

import android.util.Log;

import com.example.marti.unoplus.Screens.GameViewProt;
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
public class NextPlayerTest {

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
    public void nextPlayerTest(){
        PowerMockito.mockStatic(Log.class);

        Player player1 = new Player(0);
        Player player2 = new Player(1);
        Player player3 = null;
        Player player4 = new Player(2);
        LinkedList<Player> list = new LinkedList<>();
        list.add(player1);
        list.add(player2);
        list.add(player3);
        list.add(player4);
        PlayerList playerList = new PlayerList();
        playerList.setPlayers(list);

        gameLogic = new GameLogic(playerList, deck, gameController,true,true);

        //if player == null
        gameLogic.nextPlayer(player3);
        Assert.assertEquals(player2, gameLogic.getActivePlayer());

        //if !reverse && !skip
        gameLogic.nextPlayer(player1);
        Assert.assertEquals(player2, gameLogic.getActivePlayer());

        //if !reverse && skip
        gameLogic.skipNext();
        gameLogic.nextPlayer(player2);
        Assert.assertEquals(player4, gameLogic.getActivePlayer());

        //if reverse && !skip
        gameLogic.toggleReverse();
        gameLogic.nextPlayer(player1);
        Assert.assertEquals(player4, gameLogic.getActivePlayer());

        //if reverse && skip
        gameLogic.skipNext();
        gameLogic.nextPlayer(player2);
        Assert.assertEquals(player4, gameLogic.getActivePlayer());
    }
}
