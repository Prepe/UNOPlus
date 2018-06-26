package com.example.marti.unoplus;

import com.example.marti.unoplus.Screens.GameViewProt;
import com.example.marti.unoplus.cards.Card;
import com.example.marti.unoplus.gameLogicImpl.GameController;
import com.example.marti.unoplus.gameLogicImpl.GameLogic;
import com.example.marti.unoplus.players.Player;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.LinkedList;

import static org.mockito.Mockito.mock;

public class PlayerTest {
    @Mock
    private GameViewProt gameViewProt;
    private GameController gameController;
    private GameLogic gameLogic;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private LinkedList<Card> cards = new LinkedList<>();
    private Card card1 = new Card(Card.colors.GREEN, Card.values.TWO);
    private Card card2 = new Card(Card.colors.BLUE, Card.values.THREE);
    private Card card3 = new Card(Card.colors.RED, Card.values.ONE);

    @Before
    public void setUp() throws Exception {
        gameController = new GameController(gameViewProt);
        gameViewProt = mock(GameViewProt.class);
        gameLogic = new GameLogic();
        player1 = new Player(0);
        player2 = new Player(1);
        player3 = new Player(1);
        player4 = new Player(1);
    }

    @After
    public void tearDown() throws Exception {
        gameController = null;
        gameViewProt = null;
        gameLogic = null;
    }

    @Test
    public void setID() {
        player3.setID(2);
        player4.setID(3);
        int id3 = player3.getID();
        int id4 = player4.getID();
        Assert.assertEquals(2, id3);
        Assert.assertEquals(3, id4);
    }

    @Test
    public void setHand() {
        //Simulate starting hand
        for(int i = 0; i < 7; i++) {
            cards.add(card1);
        }
        player1.setHand(cards);

        Assert.assertEquals(7, player1.getHandSize());
    }

    @Test
    public void getHandSize() {
        cards.add(card3);
        player1.setHand(cards);
        Assert.assertEquals(1, player1.getHandSize());

        for(int i = 0; i < 999; i++){
            cards.add(card3);
        }
        player2.setHand(cards);
        Assert.assertEquals(1000, player1.getHandSize());
    }

    @Test
    public void getID() {
        int idPlayer1 = player1.getID();
        int idPlayer2 = player2.getID();
        Assert.assertEquals(0, idPlayer1);
        Assert.assertEquals(1, idPlayer2);
    }

    @Test
    public void getHand() {
        cards.add(card1);
        player1.setHand(cards);
        cards.add(card2);
        player2.setHand(cards);
        Assert.assertEquals(cards, player1.getHand());
        Assert.assertEquals(cards, player1.getHand());
    }

    @Test
    public void getMillSecs() {
        Assert.assertNotNull(player1.getMillSecs());
    }

    @Test
    public void hasCard() {
        cards.add(card2);
        player1.setHand(cards);
        player1.hasCard(card2);
        player1.hand.addCard(card2);
        Assert.assertEquals(true, player1.hand.getHand().contains(card2));
    }

    @Test
    public void setNewHand() {
        cards.add(card3);
        player1.setNewHand(0, cards);
        Assert.assertEquals(1, player1.hand.getCount());
    }
}