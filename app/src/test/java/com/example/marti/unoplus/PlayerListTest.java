package com.example.marti.unoplus;

import com.example.marti.unoplus.Screens.GameViewProt;
import com.example.marti.unoplus.gameLogicImpl.GameController;
import com.example.marti.unoplus.players.Player;
import com.example.marti.unoplus.players.PlayerList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static org.mockito.Mockito.mock;

public class PlayerListTest {

    GameViewProt gameViewProt;
    GameController gameController;
    PlayerList playerList;
    Player player1 = new Player(0);
    Player player2 = new Player(1);
    LinkedList<Player> list = new LinkedList<>();


    @Before

    public void setup() {
        boolean[] temp = {true,true,true,true,true,true,true};
        gameController = new GameController(gameViewProt,temp);

        gameViewProt = mock(GameViewProt.class);
        playerList = new PlayerList();
        list.add(player1);
        list.add(player2);
        playerList.setPlayers(list);
    }

    @After
    public void tearDown() throws Exception {
        gameController = null;
        gameViewProt = null;
        playerList = null;
        list = null;
    }

    @Test
    public void setPlayers() {
        Assert.assertEquals(player1, playerList.getFirst());
    }

    @Test
    public void getNext() {
        Assert.assertEquals(player2, playerList.getNext(player1));
    }

    @Test
    public void getPrevious() {
        Assert.assertEquals(player1, playerList.getPrevious(player2));
    }

    @Test
    public void getPlayer() {
        Assert.assertEquals(player1, playerList.getPlayer(0));
    }

    @Test
    public void playerCount() {
        Assert.assertEquals(2, playerList.playerCount());
    }

    @Test
    public void getFirst() {
        Assert.assertEquals(player1, playerList.getFirst());
    }

    @Test
    public void getPlayers() {
        Assert.assertEquals(list, playerList.getPlayers());
    }
}