package com.example.marti.unoplus.gameLogicImpl;

import android.util.Log;

import com.example.marti.unoplus.GameActions;
import com.example.marti.unoplus.Screens.GameViewProt;
import com.example.marti.unoplus.cards.Card;
import com.example.marti.unoplus.cards.Deck;
import com.example.marti.unoplus.cards.HandCardList;
import com.example.marti.unoplus.players.Player;
import com.example.marti.unoplus.players.PlayerList;

import java.util.LinkedList;
import java.util.List;

//import com.example.marti.unoplus.Screens.CardViewTest;

/**
 * Created by marti on 10.04.2018.
 */

//Der GC muss das ObserverInterface implementieren, wichtig für automatische Datenabrfrage (Observer Pattern)

public class GameController {
    GameViewProt gvp;       //reference to GameViewPrototype
    PlayerList players;     //reference to all Players in the Game
    Deck deck;              //reference to the Deck that is used
    public GameLogic logic;        //reference to the GameLogic
    int startingHand = 7;   //Amount of Cards every Player gets at the start of the Game
    float turnTime;         //Turn Timer for the Game
    public GameActions gA;  //Object that gets send to all Players
    boolean[] mustCallUNO;  //bool array for players who called uno
    boolean[] droppedCard;  //bool array for players who cheated with dropped card
    boolean[] tradedCard;   //bool array for players who cheated with trade card
    boolean hasDrawn = false; //bool to check if player has already drawn a card
    public DuelData duelData;
    Player looser;
    long[] timestamps;
    LinkedList<LinkedList<Card>> gottenHandsCards = new LinkedList<>();
    int cardspincount = 0;

    //<---------- Method for setting up the Game ---------->
    public GameController(GameViewProt gvp) {
        this.gvp = gvp;
        deck = new Deck();
    }

    public void setPlayerList(PlayerList pl) {
        players = pl;
    }

    /*
     * Take all actions to set up game:
     * setting up the GameLogic (GameRules)
     * generation of the Deck that will be used
     * giving all players their handcards
     * playing the first card of the game
     */
    public void setUpGame() {
        logic = new GameLogic(players, deck, this);

        if (players != null) {
            players.playerCount();
            mustCallUNO = new boolean[players.playerCount()];
            for (boolean b : mustCallUNO) {
                b = false;
            }
            droppedCard = new boolean[players.playerCount()];
            for (boolean b : droppedCard) {
                b = false;
            }
            tradedCard = new boolean[players.playerCount()];
            for (boolean b : tradedCard) {
                b = false;
            }
          
            //tradedCard = new boolean[players.playerCount()];
            timestamps = new long[players.playerCount()];
        }

        deck.shuffle();
        drawHandCardsForPlayers();

        playTopCard();
    }

    //Drawing handcards for all players
    private void drawHandCardsForPlayers() {
        for (int j = 0; j < players.playerCount(); j++) {
            LinkedList<Card> handcards = new LinkedList<>();
            for (int i = 0; i < startingHand; i++) {
                handcards.add(deck.draw());
            }

            gA = new GameActions(GameActions.actions.DRAW_CARD, j, handcards);

            update();
        }
    }

    //<---------- Method for handling player actions ---------->
    //Method to decide what action has been taken
    public void callGameController(GameActions action) {
        switch (action.action) {
            case DRAW_CARD:
                drawCard(action.nextPlayerID);
                break;
            case DROP_CARD:
                dropCard(action.nextPlayerID);
                break;
            case TRADE_CARD:
                tradeCard(action.playerID, action.nextPlayerID, action.card, action.check);
                break;
            case PLAY_CARD:
                playCard(action.playerID, action.card, action.check);
                break;
            case WISH_COLOR:
                colorWish(action.playerID, action.colorWish);
                break;
            case CALL_UNO:
                callUno(action.nextPlayerID);
                break;
            case HOT_DROP:
                hotDrop(action.playerID, action.timestamp);
                break;
            case DUEL_START:
                startDuel(action);
                break;
            case DUEL_OPPONENT:
                endDuel(action);
                break;
            case CARD_SPIN:
                cardSpin2(action);
                break;
            case GIVE_Hand:
                saveGottenHands(action.playerID, action.cards);
                break;
            case DO_CardSpin:
                cardspincount++;
                doingCardSpin();
                break;
        }
    }

    //Method that updates all players
    public void update() {
        if (gA.action == GameActions.actions.NEXT_PLAYER) {
            resetCheats();
            resetCalledUno();
        }
        gA.gcSend = true;
        gvp.updateAllConnected(gA);
    }

    //Method for all Players to call to draw Cards form the Deck
    void drawCard(int playerID) {
        int aID = logic.activePlayer.getID();
        if (playerID == aID) {
            if (hasDrawn) {
                hasDrawn = false;
                logic.nextPlayer(logic.activePlayer);

                gA = new GameActions(GameActions.actions.NEXT_PLAYER, aID);
                update();
            } else {
                resetCalledUno();
                LinkedList<Card> cards = new LinkedList<>();
                if (deck.isEmptyDeck()) {
                    deck.replaceTakeDeck();
                }
                int count = logic.getCardDrawCount();
                for (int i = 0; i < count; i++) {
                    cards.add(deck.draw());
                }
                hasDrawn = true;

                gA = new GameActions(GameActions.actions.DRAW_CARD, playerID, cards);
                update();
            }
        }
    }

    void drawCardAsDuelLoser(int loserID){
        LinkedList<Card> cards = new LinkedList<>();
        if (deck.isEmptyDeck()) {
            deck.replaceTakeDeck();
        }
        int count = logic.getCardDrawCount();
        for (int i = 0; i < count; i++) {
            cards.add(deck.draw());
        }

        gA = new GameActions(GameActions.actions.DRAW_CARD, loserID, cards);
        update();

        logic.nextPlayer(logic.getActivePlayer());
    }

    //Method for playing cards
    void playCard(int player, Card card, boolean has1Card) {
        Player p = players.getPlayer(player);
        //Check if player is allowed to play the card
        if (logic.checkCard(card, p)) {
            resetCalledUno();
            mustCallUNO[player] = has1Card;
            hasDrawn = false;
            //Remove the played card from the players hand and update Players
            gA = new GameActions(GameActions.actions.PLAY_CARD, player, card, true);
            update();

            //Run game logic for the card that was played
            logic.runLogic(p, card);
        } else {
            gA = new GameActions(GameActions.actions.PLAY_CARD, player, false);
            update();
        }
    }

    //Method to change the color that has to be played next
    void colorWish(int ID, Card.colors color) {
        logic.wishColor(color);
        Player p = players.getPlayer(ID);
        if (p.equals(logic.activePlayer)) {
            logic.nextPlayer(p);

            gA = new GameActions(GameActions.actions.UPDATE, logic.getActivePlayer().getID(), new Card(logic.lastCardColor, logic.lastCardValue));
            update();
        }
    }

    //Method to cheat and drop a Card
    void dropCard(int player) {
        if (!droppedCard[player] && player != logic.activePlayer.getID()) {
            droppedCard[player] = true;
            gA = new GameActions(GameActions.actions.DROP_CARD, player, true);
            update();
        } else {
            gA = new GameActions(GameActions.actions.DROP_CARD, player, false);
            update();
            this.gvp.toastCantTrade();
        }
    }

    //Method to call Uno
    void callUno(int player) {
        if (!mustCallUNO[player]) {
            mustCallUNO[player] = true;
            Log.d("CALLUNO", player + " hat UNO gecalled");
            gA = new GameActions(GameActions.actions.CALL_UNO, player, true);
            update();
        } else {
            gA = new GameActions(GameActions.actions.CALL_UNO, player, false);
            update();
            drawCard(player);
        }

    }
  
    //Method to trade Card with other players
    void tradeCard(int traderID, int tradeTargetID, Card tradedCard, boolean accepted) {
        if (accepted) {
            Log.d("GC", "Got Trade-Action");
            gA = new GameActions(GameActions.actions.TRADE_CARD, traderID, tradeTargetID, tradedCard, accepted);
            update();
        } else {
            if (this.tradedCard[traderID] && traderID == logic.activePlayer.getID()) {
                gA = new GameActions(GameActions.actions.TRADE_CARD, traderID, traderID, tradedCard, true);
                update();
            } else {
                Log.d("GC", "Got Trade-Action");
                this.tradedCard[traderID] = true;
                gA = new GameActions(GameActions.actions.TRADE_CARD, traderID, tradeTargetID, tradedCard, accepted);
                update();
            }
        }
    }

    void hotDrop(int player, long timestamp){
        timestamps[player] = timestamp;

        for (int i = 0; i < timestamps.length; i++) {
            if(timestamps[i] == 0){
                return;
            }
        }

        int slowestPlayer = 0;
        for (int i = 1; i < timestamps.length; i++) {
            if(timestamps[i] > timestamps[slowestPlayer]){
                slowestPlayer = i;
            }
        }

        timestamps = new long[timestamps.length];

        logic.cardDrawCount = 2;
        drawCardAsDuelLoser(slowestPlayer);
    }

    //<---------- Method for other actions called from GameLogic, CardEffects, etc ---------->
    //Check if a player has won the game by playing his last card
    public Player getWinningPlayer() {
        for (Player p : players.getPlayers()) {
            if (p.getHandSize() == 0) {
                return p;
            }
        }
        return null;
    }

    //Playing the top card of the deck without GameLogic
    public void playTopCard() {
        Card topCard = deck.draw();
        while (topCard.color == Card.colors.WILD) {
            topCard = deck.draw();
        }
        logic.playTopCard(topCard);
    }

    /*
     * Method that times each Turn for Players
     * When at 0 draws a Card for the active Player
     * */
    private void runTimer() {
        //TODO implement
    }

    // resets the cheats
    void resetCheats() {
        droppedCard[logic.activePlayer.getID()] = false;
        tradedCard[logic.activePlayer.getID()] = false;
    }

    //resets calledUno
    void resetCalledUno() {
        for (int i = 0; i < mustCallUNO.length; i++) {
            mustCallUNO[i] = false;
        }
    }

    private void startDuel(GameActions action) {
        this.duelData = new DuelData(action.playerID, action.nextPlayerID, action.colorWish);
        gA = new GameActions(GameActions.actions.DUEL_OPPONENT, action.nextPlayerID, action.playerID);
        update();
    }

    private void endDuel(GameActions action) {
        int loserID = this.duelData.getDuelLoserID(action.colorWish);
        this.duelData = null;
        drawCardAsDuelLoser(loserID);

    }

    private void cardSpin(int id) {
        gA = new GameActions(GameActions.actions.CARD_SPIN, id);
        update();
    }



    void cardSpin2 (GameActions action) {
        if (action.check == null) {
            int playerID = action.playerID;
            LinkedList<Card> cards = action.cards;
            int resivingPlayerID;

            if (logic.reverse) {
                resivingPlayerID = players.getPrevious(players.getPlayer(playerID)).getID();
            } else {
                resivingPlayerID = players.getNext(players.getPlayer(playerID)).getID();
            }

            gA = new GameActions(GameActions.actions.CARD_SPIN, resivingPlayerID, cards);
            update();
        } else {
            logic.nextPlayer(players.getPlayer(action.playerID));
        }
    }

    private void saveGottenHands(int id, LinkedList<Card> cards) {
        gottenHandsCards.add(id, cards);

        gA = new GameActions(GameActions.actions.GOT_Hand, id);
        update();
    }

    private void doingCardSpin() {
        if (cardspincount==gottenHandsCards.size()){
            if (logic.checkifreversed()) {
                for (int i = 0; i < gottenHandsCards.size(); i++) {
                    LinkedList<Card> newCards = gottenHandsCards.get(i);
                    int id = i;
                    if (id == 0) {
                        id = gottenHandsCards.size() - 1;
                    } else {
                        id--;
                    }

                    gA = new GameActions(GameActions.actions.GET_NEWHand, id, newCards);
                    update();
                }
            } else {
                for (int i = 0; i < gottenHandsCards.size(); i++) {
                    LinkedList<Card> newCards = gottenHandsCards.get(i);
                    int id = i;
                    if (id == gottenHandsCards.size() - 1) {
                        id = 0;
                    } else {
                        id--;
                    }

                    gA = new GameActions(GameActions.actions.GET_NEWHand, id, newCards);
                    update();
                }
            }
        }
    }

    void accusePlayer(int accusingPlayerID, int accusedPlayerID) {
        if (droppedCard[accusedPlayerID] || tradedCard[accusedPlayerID]) {
            droppedCard[accusedPlayerID] = false;
            tradedCard[accusedPlayerID] = false;

            logic.cardDrawCount = 2;
            drawCard(accusedPlayerID);
            return;
        }
        if (mustCallUNO[accusedPlayerID]) {
            mustCallUNO[accusedPlayerID] = false;

            logic.cardDrawCount = 2;
            drawCard(accusedPlayerID);
            return;
        }

        drawCard(accusingPlayerID);
    }
    public LinkedList<LinkedList<Card>> getGottenHandsCards() {
        return gottenHandsCards;
    }

    public DuelData getDuelData() {
        return duelData;
    }
}