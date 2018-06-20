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
public class CardsTest {

    @Mock
    GameViewProt gameViewProt;
    GameController gameController;
    GameLogic gameLogic;
    Player player;

    @Before
    public void setup() {
        gameController = new GameController(gameViewProt);
        gameViewProt = mock(GameViewProt.class);
        gameLogic = new GameLogic();
    }

    @Test
    public void CardsTest() {
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

        Card card1 = new Card(Card.colors.GREEN, Card.values.TWO);
        Card card2 = new Card(Card.colors.GREEN, Card.values.TWO);
        Card card3 = new Card(Card.colors.YELLOW, Card.values.NINE);
        Card card4 = new Card(Card.colors.WILD, Card.values.PLUS_TWO);
        Card card5 = new Card(Card.colors.WILD, Card.values.SKIP);
        Card card6 = new Card(Card.colors.WILD, Card.values.HOT_DROP);
        Card card7 = new Card(Card.colors.WILD, Card.values.DUEL);
        Card card8 = new Card(Card.colors.WILD, Card.values.CARD_SPIN);
        Card card9 = new Card(Card.colors.WILD, Card.values.PLUS_FOUR);

        Assert.assertEquals(true,card1.hasSameCardValueAs(card2));
        Assert.assertEquals(false,card1.hasSameCardValueAs(card3));
        Assert.assertEquals(Card.colors.GREEN,card1.getColor());
        Assert.assertEquals(Card.values.TWO,card1.getValue());
        Assert.assertEquals(true,card4.isWildCard());
        Assert.assertEquals(false,card3.isWildCard());
        Assert.assertEquals(true,card5.isSkip());
        Assert.assertEquals(false,card4.isSkip());
        Assert.assertEquals(true,card6.isHotDrop());
        Assert.assertEquals(false,card5.isHotDrop());
        Assert.assertEquals(true,card7.isDuel());
        Assert.assertEquals(false,card6.isDuel());
        Assert.assertEquals(true,card8.isCardSpin());
        Assert.assertEquals(false,card7.isCardSpin());
        Assert.assertEquals(true,card9.isTakeFour());
        Assert.assertEquals(true,card4.isTakeTwo());

    }
}
