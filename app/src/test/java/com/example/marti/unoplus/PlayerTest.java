package com.example.marti.unoplus;

import android.util.Log;

import com.example.marti.unoplus.Screens.GameViewProt;
import com.example.marti.unoplus.cards.Card;
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
public class PlayerTest {

    @Mock
    GameViewProt gameViewProt;
    GameController gameController;
    GameLogic gameLogic;
    Player player;

    @Before
    public void setup() {
        boolean[] temp = {true,true,true,true,true,true,true};
        gameController = new GameController(gameViewProt,temp);
        gameViewProt = mock(GameViewProt.class);
        gameLogic = new GameLogic();
    }

    @Test
    public void playerTest() {
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

        Card card1 = new Card(Card.colors.GREEN, Card.values.TWO);
        Card card2 = new Card(Card.colors.BLUE, Card.values.NINE);

        //Simulate starting hand
        for(int i = 0; i < 7; i++) {
            cards.add(card1);
        }
        player1.setHand(cards);
        player2.setHand(cards);
        int idPlayer1 = player1.getID();
        int idPlayer2 = player2.getID();

        Assert.assertEquals(7, player1.getHandSize());
        Assert.assertEquals(7, player2.getHandSize());
        Assert.assertEquals(cards, player2.getHand());
        Assert.assertEquals(cards, player1.getHand());
        Assert.assertEquals(0, idPlayer1);
        Assert.assertEquals(1, idPlayer2);

        player1.hand.removeCard(cards.get(0));

        player1.hand.addCard(card2);
        Assert.assertEquals(1, player1.hand.getCount());

        player1.hasCard(card2);
        Assert.assertEquals(true, player1.hand.getHand().contains(card2));
        Assert.assertEquals(false, player1.hand.getHand().contains(card1));

        player1.setNewHand(0, cards);
        Assert.assertEquals(7, player1.getHandSize());

        //Refactor later
        //player1.acceptTrade(player2.getID(), card1);
        //GameActions expected1 = new GameActions(GameActions.actions.TRADE_CARD, 1,0 , card1, true);


        //player1.drawCard();
        //player1.playCard(card1);
        //player1.tradeCard(player2.getID(), cards.getFirst());
        //doNothing().when(gameViewProt).updateAllConnected(expected1);
        //doNothing().when(gameViewProt).writeNetMessage();


    }
}
