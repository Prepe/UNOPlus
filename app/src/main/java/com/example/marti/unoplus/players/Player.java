package com.example.marti.unoplus.players;

import android.util.Log;

import com.example.marti.unoplus.GameActions;
import com.example.marti.unoplus.GameStatics;
import com.example.marti.unoplus.Screens.GameViewProt;
import com.example.marti.unoplus.cards.Card;
import com.example.marti.unoplus.cards.HandCardList;
import com.example.marti.unoplus.gameLogicImpl.GameController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Player {
    String playerName;
    Integer ID;
    GameViewProt gameViewProt;
    Card lastCard;
    LinkedList<Card> handcards; //Hand
    int[] handcardcounter;
    public HandCardList hand;
    Card dropCard;
    Card tradeCard;
    int tradedwith;
    boolean activeTrade;

    public Player(Integer id) {
        ID = id;
        //handcards = new LinkedList<>();
        hand = new HandCardList();
    }

    public void setGV(GameViewProt gv) {
        gameViewProt = gv;
    }

    public void setID(int id) {
        ID = id;
    }

    public List<Card> getHand() {
        return this.handcards;
    }

    public int getHandSize() {
        return getHand().size();
    }

    public String getName() {
        return this.playerName;
    }

    public Integer getID() {
        return ID;
    }

    public void initialsiedHandCardCounters(int size) {
        handcardcounter = new int[size];

    }

    public int[] getHandcardcounter() {
        return handcardcounter;
    }

    //<---------- Player Actions ---------->
    //Ask for Cards
    public void drawCard() {
        GameActions action;
        action = new GameActions(GameActions.actions.DRAW_CARD, ID);
        gameViewProt.writeNetMessage(action);
    }

    //Tell what Card you want to play
    public void playCard(Card c) {
        GameActions action;
        action = new GameActions(GameActions.actions.PLAY_CARD, ID, c);
        if (hand.getCount() == 1) {
            action.check = true;
        } else {
            action.check = false;
        }
        Log.d("GameDebug", "Player playCard :" + c.value.toString() + " " + c.color.toString());
        this.gameViewProt.writeNetMessage(action);
    }

    //Tell what card has to be dropped
    public void dropCard(Card card) {
        Log.d("GameDebug", "Player threw Card away :" + card.value.toString() + " " + card.color.toString());
        dropCard = card;
        GameActions tam = new GameActions(GameActions.actions.DROP_CARD, ID);
        gameViewProt.writeNetMessage(tam);
    }

    //Tell which player called uno
    public void callUno(int player) {
        Log.d("UNOUNO", "CALL UNO UNo");

        GameActions action = new GameActions(GameActions.actions.CALL_UNO, player);
        gameViewProt.writeNetMessage(action);
    }

    //Tell what card you want to trade and remove it
    public void tradeCard(int tradeTargetID , Card tradedCard) {
        hand.removeCard(tradedCard);
        gameViewProt.removeCardFromHand(tradedCard);
        activeTrade = true;
        tradedwith = tradeTargetID;
        Log.d("GameDebug", "Player wants to trade card :" + tradedCard.value.toString() + " " + tradedCard.color.toString() + "with Player ..");
        GameActions action = new GameActions(GameActions.actions.TRADE_CARD, ID, tradeTargetID, tradedCard, false);
        gameViewProt.writeNetMessage(action);
    }

    //<---------- Player Reactions ---------->
    public void callPlayer(GameActions action) {
        switch (action.action) {
            case DROP_CARD:
                this.canDropCard(action.playerID, action.check);
                break;
            case DRAW_CARD:
                gotCard(action.playerID, action.cards);
                break;
            case PLAY_CARD:
                if (action.check) {
                    cardPlayed(action.playerID, action.card);
                } else {
                    wrongCard(action.playerID);
                }
                break;
            case UPDATE:
                updateGame(action.playerID, action.card);
                break;
            case NEXT_PLAYER:
                //TODO ipml
                break;
            case WISH_COLOR:
                if (action.playerID == this.ID) {
                    this.gameViewProt.chooseColor();
                }
                break;
            case INIT_GAME:
                initialsiedHandCardCounters(action.nextPlayerID);
                break;
            case CALL_UNO:
                this.canSayUno(action.playerID, action.check);
                break;
            case TRADE_CARD:
                this.gotCardTrade(action.playerID, action.nextPlayerID, action.check, action.card);
                break;
        }
    }



    //Check if the GA is for you
    boolean checkID(int pID) {
        return pID == this.ID;
    }

    //Update Game Screen (last played Card, etc.)
    void updateGame(int nextPID, Card card) {
        updateLastCard(card);
        if (ID == nextPID) {
            gameViewProt.toastYourTurn();
        }
    }

    void updateLastCard(Card lastCard) {
        this.lastCard = lastCard;
        this.gameViewProt.updateCurrentPlayCard(this.lastCard);
    }

    //Add given Cards to your handcards
    void gotCard(int pID, List<Card> cards) {
        if (checkID(pID)) {
            for (Card c : cards) {
                //this.handcards.add(c);
                this.hand.addCard(c);
                //this.gameViewProt.addCardToHand(c);
                this.gameViewProt.handChanged(hand.getHand());
            }
            updateHandCardCounter(cards.size(), pID);
        } else {
            updateHandCardCounter(cards.size(), pID);
        }

    }

    //Your intended Card was played so now you can remove it
    void cardPlayed(int ID, Card card) {
        if (checkID(ID)) {
            this.hand.removeCard(card);
            this.gameViewProt.removeCardFromHand(card);
            this.gameViewProt.handChanged(hand.getHand());
            gameViewProt.timer.cancel();
            updateHandCardCounter(-1, ID);
            if (this.hand.getCount() == 0) {
                winGame();
            }
        } else {
            updateHandCardCounter(-1, ID);
        }

        updateLastCard(card);
    }

    //Your intended Card couldn't be played
    void wrongCard(int pID) {
        if (checkID(pID)) {
            gameViewProt.toastWrongCard();
        }
    }

    public boolean hasCard(Card card) {
        return this.hand.getHand().contains(card);
    }

    // Card which will be dropped can now be dropped
    void canDropCard(int playerID, Boolean canDrop) {
        if (canDrop == null) {
            return;
        }

        if (canDrop) {
            if (playerID == this.getID()) {
                hand.removeCard(dropCard);
                dropCard = null;
                updateHandCardCounter(-1, playerID);
                gameViewProt.handChanged(hand.getHand());
            } else {
                updateHandCardCounter(-1, playerID);
            }
        }
    }

    // if accepeted card will be traded if not card wont be traded
    void gotCardTrade(int traderID, int tradeTargetID, boolean accepted, Card tradedCard) {
        if(accepted){
            if(activeTrade && traderID == this.ID && tradedwith == tradeTargetID){
                activeTrade = false;
            } else if(traderID == tradeTargetID){
                if(traderID == this.ID){
                    activeTrade = false;
                    gameViewProt.toastAlreadyTraded();
                }
                updateHandCardCounter(-1, traderID);
            }

            LinkedList<Card> temp = new LinkedList<>();
            temp.add(tradedCard);
            gotCard(traderID, temp);

        } else {
            Log.d("TRADE_CARRD", "Player " + traderID + " wasnts to trade with Player " + tradeTargetID);
            updateHandCardCounter(-1, traderID);
            if(traderID == tradedwith && activeTrade){
                acceptTrade(tradeTargetID,  tradedCard);
            } else if(tradeTargetID == this.ID){
                gameViewProt.tradeOffer(traderID, tradedCard);
            }
        }
    }

    void canSayUno(int playerID, Boolean canSayUno) {
        if (canSayUno == null) {
            return;
        }

        if (canSayUno) {
            if (playerID == this.ID && hand.getCount() == 1){

            }
        } else {
            //TODO wrong
        }
    }

    //<---------- Misc ---------->
    public void createDummyCards() {
        List<Card> list = new ArrayList<Card>();
        for (int i = 0; i < 7; i++) {
            Card.colors rndcolor = GameStatics.randomEnum(Card.colors.class);
            Card.values rndvalue = GameStatics.randomEnum(Card.values.class);
            Card card = new Card(rndcolor, rndvalue);
            list.add(card);
        }
        this.gotCard(this.getID(), list);
    }

    public void drawCardIfTimesUp() {
        drawCard();
    }

    void updateHandCardCounter(int count, int ID) {
        handcardcounter[ID] += count;
        gameViewProt.updateCountersInView();
    }

    void winGame() {
        GameActions win = new GameActions(GameActions.actions.GAME_FINISH, ID, true);
        gameViewProt.writeNetMessage(win);
        gameViewProt.toastGameFinished(ID);
    }

    public void acceptTrade(int tradeTargetID,  Card tradedCard){
        GameActions trade = new GameActions(GameActions.actions.TRADE_CARD, ID, tradeTargetID, tradedCard,  true);
        gameViewProt.writeNetMessage(trade);
    }

    public void declineTrade(int traderID, Card tradedCard){
        GameActions trade = new GameActions(GameActions.actions.TRADE_CARD,traderID,  ID, tradedCard,  true);
        gameViewProt.writeNetMessage(trade);
    }
}
