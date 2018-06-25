package com.example.marti.unoplus;

import android.support.annotation.VisibleForTesting;
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

import static org.mockito.Mockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class CheckValueTest {

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

    @VisibleForTesting boolean checkValue(Card card) {
        //Check for matching Value
        if (card.getValue() == gameLogic.getLastCardValue()) {
            return true;
        }
        return false;
    }

    @Test
    public void checkValueTest(){
        PowerMockito.mockStatic(Log.class);

        Player player1 = new Player(0);
        Player player2 = new Player(1);
        LinkedList<Player> list = new LinkedList<>();
        list.add(player1);
        list.add(player2);
        PlayerList playerList = new PlayerList();
        playerList.setPlayers(list);

        Card card1 = new Card(Card.colors.RED, Card.values.FIVE);
        Card card2 = new Card(Card.colors.YELLOW, Card.values.FIVE);
        Card card3 = new Card(Card.colors.GREEN, Card.values.THREE);

        gameLogic = new GameLogic(playerList, deck, gameController,true,true);
        gameController.setPlayerList(playerList);
        gameController.setUpGame();

        //if value == lastCardValue
        gameLogic.playTopCard(card1);
        boolean actual1 = checkValue(card2);
        Assert.assertTrue(actual1);

        //if value != lastCardValue
        gameLogic.playTopCard(card1);
        boolean actual2 = checkValue(card3);
        Assert.assertFalse(actual2);
    }

}
