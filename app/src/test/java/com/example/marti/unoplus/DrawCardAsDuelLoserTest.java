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
public class DrawCardAsDuelLoserTest {

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
        gameLogic = new GameLogic(playerList, deck, gameController,true,true);

        //if deck is empty
        for (int i = 0; i < 132; i++) {
            deck.draw();
        }

        GameActions expectedGA1 = new GameActions(GameActions.actions.UPDATE, card1, gameLogic.nextPlayer(gameLogic.getActivePlayer()).getID());

        doNothing().when(gameViewProt).updateAllConnected(expectedGA1);

        gameController.forcedCardDraw(gameLogic.getActivePlayer().getID());

        Assert.assertEquals(expectedGA1.action, gameController.gA.action);

        //if deck is not empty
        deck = new Deck(true,true,true);

        GameActions expectedGA2 = new GameActions(GameActions.actions.UPDATE, card1, gameLogic.nextPlayer(gameLogic.getActivePlayer()).getID());

        doNothing().when(gameViewProt).updateAllConnected(expectedGA2);

        gameController.forcedCardDraw(gameLogic.getActivePlayer().getID());

        Assert.assertEquals(expectedGA2.action, gameController.gA.action);

    }
}
