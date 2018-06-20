package com.example.marti.unoplus;

import android.util.Log;

import com.example.marti.unoplus.Screens.GameViewProt;
import com.example.marti.unoplus.gameLogicImpl.GameController;
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
public class PlayerListTest {

    @Mock
    GameViewProt gameViewProt;
    GameController gameController;
    PlayerList playerList;

    @Before
    public void setup() {
        gameController = new GameController(gameViewProt);
        gameViewProt = mock(GameViewProt.class);
    }

    @Test
    public void playerListTest() {
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

        Assert.assertEquals(player1, playerList.getFirst());
        Assert.assertEquals(player1, playerList.getPlayer(0));
        Assert.assertEquals(list, playerList.getPlayers());
        Assert.assertEquals(player2, playerList.getNext(player1));
        Assert.assertEquals(player1, playerList.getPrevious(player2));
        Assert.assertEquals(2, playerList.playerCount());

    }
}
