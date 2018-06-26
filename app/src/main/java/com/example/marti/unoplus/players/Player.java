package com.example.marti.unoplus.players;

import android.os.Handler;
import android.util.Log;

import com.example.marti.unoplus.GameActions;
import com.example.marti.unoplus.Screens.GameViewProt;
import com.example.marti.unoplus.cards.Card;
import com.example.marti.unoplus.cards.HandCardList;

import java.util.LinkedList;
import java.util.List;

public class Player {
    private GameViewProt gameViewProt;
    private Card lastCard;
    private LinkedList<Card> handcards; //Hand
    private Card dropCard;
    private String playerName;
    private Integer ID;
    private int[] handcardcounter;
    private int tradedwith;
    private boolean activeTrade;
    private boolean cardSpinStart = false;
    public HandCardList hand;

    //Variables needed for the Hot-Drop-Feature
    private int seconds = 0;
    private boolean startRun = true;
  
    private int millsecs = 0;

    public Player(Integer id) {
        ID = id;
        hand = new HandCardList();
    }

    public void setGV(GameViewProt gv) {
        gameViewProt = gv;
    }

    public void setID(int id) {
        ID = id;
    }

    public void setHand(LinkedList<Card> cards) {
        handcards = cards;
    }

    public int getHandSize() {
        return getHand().size();
    }

    public int[] getHandcardcounter() {
        return handcardcounter;
    }

    public void setName(String name){playerName = name;}

    public Integer getID() {
        return ID;
    }

    public String getName() {
        return this.playerName;
    }

    public LinkedList<Card> getHand() {
        return this.handcards;
    }

    public int getMillSecs() {
        return this.millsecs;
    }

    private void initialsiedHandCardCounters(int size) {
        handcardcounter = new int[size];
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
        this.gameViewProt.writeNetMessage(action);
    }

    //Tell what card has to be dropped
    public void dropCard(Card card) {
        dropCard = card;
        GameActions tam = new GameActions(GameActions.actions.DROP_CARD, ID);
        gameViewProt.writeNetMessage(tam);
    }

    //Tell which player called uno
    public void callUno(int player) {
        GameActions action = new GameActions(GameActions.actions.CALL_UNO, player);
        gameViewProt.writeNetMessage(action);
    }

    //Tell what card you want to trade and remove it
    public void tradeCard(int tradeTargetID, Card tradedCard) {
        hand.removeCard(tradedCard);
        gameViewProt.removeCardFromHand(tradedCard);
        activeTrade = true;
        tradedwith = tradeTargetID;
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
            case HOT_DROP:
                this.gameViewProt.hotDrop();
                break;
            case DUEL_START:
                if (action.playerID == this.ID) {
                    this.gameViewProt.startDuel();
                }
                break;
            case DUEL_OPPONENT:
                if (action.playerID == this.ID) {
                    this.gameViewProt.duelOpponentDialog(action.nextPlayerID);
                }
                break;
            case CARD_SPIN:
                cardSpin(action);
                break;
        }
    }

    //Check if the GA is for you
    private boolean checkID(int pID) {
        return pID == this.ID;
    }

    //Update Game Screen (last played Card, etc.)
    private void updateGame(int nextPID, Card card) {
        updateLastCard(card);
        if (ID == nextPID) {
            gameViewProt.toastYourTurn();
        }
    }

    private void updateLastCard(Card lastCard) {
        this.lastCard = lastCard;
        this.gameViewProt.updateCurrentPlayCard(this.lastCard);
    }

    //Add given Cards to your handcards
    private void gotCard(int pID, List<Card> cards) {
        if (checkID(pID)) {
            for (Card c : cards) {
                this.hand.addCard(c);
                this.gameViewProt.handChanged(hand.getHand());
            }
            updateHandCardCounter(cards.size(), pID);
        } else {
            updateHandCardCounter(cards.size(), pID);
        }
    }

    //Your intended Card was played so now you can remove it
    private void cardPlayed(int ID, Card card) {
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
    private void wrongCard(int pID) {
        if (checkID(pID)) {
            gameViewProt.toastWrongCard();
        }
    }

    public boolean hasCard(Card card) {
        return this.hand.getHand().contains(card);
    }

    // Card which will be dropped can now be dropped
    private void canDropCard(int playerID, Boolean canDrop) {
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
    private void gotCardTrade(int traderID, int tradeTargetID, boolean accepted, Card tradedCard) {
        if (accepted) {
            if (activeTrade && traderID == this.ID && tradedwith == tradeTargetID) {
                activeTrade = false;
            } else if (traderID == tradeTargetID) {
                if (traderID == this.ID) {
                    activeTrade = false;
                    gameViewProt.toastAlreadyTraded();
                }
            }
            LinkedList<Card> temp = new LinkedList<>();
            temp.add(tradedCard);
            gotCard(traderID, temp);
        } else {
            updateHandCardCounter(-1, traderID);
            if (traderID == tradedwith && activeTrade) {
                acceptTrade(tradeTargetID, tradedCard);
            } else if (tradeTargetID == this.ID) {
                gameViewProt.tradeOffer(traderID, tradedCard);
            }
        }
    }

    //Check if player can say Uno
    private void canSayUno(int playerID, Boolean canSayUno) {
        if (canSayUno == null) {
            return;
        }

        if (canSayUno) {
            if (playerID == this.ID && hand.getCount() == 1) {

            }
        } else {
            //TODO wrong
        }
    }

    //Timer for each player when Hot-Drop-Card is played
    public void timer(final boolean startRun) {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                millsecs = seconds / 1000;

                if (startRun) {
                    seconds++;
                }
                handler.postDelayed(this, 100);
            }
        });
    }

    //<---------- Misc ---------->
    //Update the handcard counter of players
    private void updateHandCardCounter(int count, int ID) {
        handcardcounter[ID] += count;
        gameViewProt.updateCountersInView();
    }

    //called if player wins
    private void winGame() {
        GameActions win = new GameActions(GameActions.actions.GAME_FINISH, ID, true);
        gameViewProt.writeNetMessage(win);
        gameViewProt.toastGameFinished(ID);
    }

    //Called if player accepts trade Card Cheat
    public void acceptTrade(int tradeTargetID, Card tradedCard) {
        GameActions trade = new GameActions(GameActions.actions.TRADE_CARD, ID, tradeTargetID, tradedCard, true);
        gameViewProt.writeNetMessage(trade);
    }

    //Called if player cancel trade Card Cheat offer
    public void declineTrade(int traderID, Card tradedCard) {
        GameActions trade = new GameActions(GameActions.actions.TRADE_CARD, traderID, ID, tradedCard, true);
        gameViewProt.writeNetMessage(trade);
    }

    public void giveHand() {
        GameActions ga = new GameActions(GameActions.actions.GIVE_Hand, ID, hand.getHand());
        gameViewProt.writeNetMessage(ga);
    }

    public void setNewHand(int id, LinkedList<Card> cards) {
        if (id == ID) {
            for (Card card : cards) {
                hand.addCard(card);
            }
        }
    }

    private void cardSpin(GameActions action) {
        if (action.playerID == ID) {
            Log.d("PLAYER", "Called CardSpin");
            LinkedList<Card> temp = new LinkedList<>();
            if (!cardSpinStart) {
                Log.d("PLAYER", "Saving old Hand");
                cardSpinStart = true;
                temp = hand.getHand();
            }

            if (action.cards != null) {
                Log.d("PLAYER", "Override Hand");
                cardSpinStart = false;
                hand = new HandCardList();
                for (int i = 0; i < action.cards.size(); i++) {
                    hand.addCard(action.cards.get(i));
                }
                handcardcounter[ID] = hand.getCount();
                gameViewProt.handChanged(hand.getHand());
            }

            if (temp.size() != 0) {
                Log.d("PLAYER", "Sending old Hand");
                gameViewProt.writeNetMessage(new GameActions(GameActions.actions.CARD_SPIN, ID, temp));
            } else {
                Log.d("PLAYER", "Card Spin Finished");
                gameViewProt.writeNetMessage(new GameActions(GameActions.actions.CARD_SPIN, ID, true));
            }

        } else {
            Log.d("PLAYER", "Not my GA");
            if (action.cards != null) {
                Log.d("PLAYER", "UPDATE");
                handcardcounter[action.playerID] = action.cards.size();
            }
        }
        gameViewProt.updateCountersInView();
    }
}
