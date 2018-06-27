package com.example.marti.unoplus.gameLogicImpl;

import android.util.Log;

import com.example.marti.unoplus.GameActions;
import com.example.marti.unoplus.Screens.GameViewProt;
import com.example.marti.unoplus.cards.Card;
import com.example.marti.unoplus.cards.Deck;
import com.example.marti.unoplus.players.Player;
import com.example.marti.unoplus.players.PlayerList;

import java.util.LinkedList;

//import com.example.marti.unoplus.Screens.CardViewTest;

/**
 * Created by marti on 10.04.2018.
 */

//Der GC muss das ObserverInterface implementieren, wichtig für automatische Datenabrfrage (Observer Pattern)

public class GameController {
    //References
    GameViewProt gvp;       //reference to GameViewPrototype
    PlayerList players;     //reference to all Players in the Game
    Deck deck;              //reference to the Deck that is used
    public GameLogic logic; //reference to the GameLogic

    //In Game Data Variables
    int startingHand = 7;   //Amount of Cards every Player gets at the start of the Game
    public GameActions gA;  //Object that gets send to all Players
    boolean[] mustCallUNO;  //bool array for players who called uno
    boolean[] droppedCard;  //bool array for players who cheated with dropped card
    boolean[] tradedCard;   //bool array for players who cheated with trade card
    long[] timestamps;      //long array for hotDrop times
    boolean hasDrawn = false; //bool to check if player has already drawn a card
    public DuelData duelData; // holds information about a duel

    //<OPTIONS>
    boolean dropCardAllowed;            //enables players to drop cards
    int dropCardPunishment = 2;         //how many cards a player draws when punished
    boolean tradeCardAllowed;           //enables players to trade cards
    int tradeCardPunishment = 2;        //how many cards a player draws when punished
    boolean quickPlayAllowed;           //enables players to play cards anytime turn
    boolean counterAllowed;             //enables players to counter +2/4
    boolean hotDropEnabled;             //enables the HotDop Card
    int hotDropPunishment = 2;          //HotDrop looser draw amount
    boolean duelEnabled;                //enables the Duel Card
    int duelPunishment = 2;             //Duel looser draw amount
    boolean cardSpinEnabled;            //enables CardSpin Card
    int accusingPunishment = 1;         //amount of cards a player gets for wrong Call

    //Test Variables?
    LinkedList<LinkedList<Card>> gottenHandsCards = new LinkedList<>();

    //<---------- Method for setting up the Game ---------->
    public GameController(GameViewProt gvp, boolean[] options) {
        duelEnabled = options[0];
        hotDropEnabled = options[1];
        cardSpinEnabled = options[2];
        dropCardAllowed = options[3];
        tradeCardAllowed = options[4];
        counterAllowed = options[5];
        quickPlayAllowed = options[6];

        this.gvp = gvp;
        deck = new Deck(hotDropEnabled,duelEnabled,cardSpinEnabled);
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
        logic = new GameLogic(players, deck, this, quickPlayAllowed, counterAllowed);

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
                cardSpin(action);
                break;
            case BLAME_SB:
                blamePlayer(action.playerID, action.nextPlayerID);
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

    public void forcedCardDraw(int loserID){
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
        if (!droppedCard[player] && player != logic.activePlayer.getID() && dropCardAllowed) {
            droppedCard[player] = true;
            gA = new GameActions(GameActions.actions.DROP_CARD, player, true);
            update();
        } else {
            gA = new GameActions(GameActions.actions.DROP_CARD, player, false);
            update();
        }
    }

    //Method to call Uno
    void callUno(int player) {
        if (mustCallUNO[player]) {
            mustCallUNO[player] = false;
            Log.d("CALLUNO", player + " hat UNO gecalled");
            gA = new GameActions(GameActions.actions.CALL_UNO, player, true);
            update();
        } else {
            gA = new GameActions(GameActions.actions.CALL_UNO, player, false);
            update();
            int amount = logic.getCardDrawCount();
            forcedCardDraw(player);
            logic.changeCardDrawCount(amount);
        }

    }
  
    //Method to trade Card with other players
    void tradeCard(int traderID, int tradeTargetID, Card tradedCard, boolean accepted) {
        if (tradeCardAllowed) {
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
        } else {
            gA = new GameActions(GameActions.actions.TRADE_CARD, traderID, traderID, tradedCard, true);
            update();
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

        logic.changeCardDrawCount(hotDropPunishment);
        forcedCardDraw(slowestPlayer);
    }

    private void startDuel(GameActions action) {
        this.duelData = new DuelData(action.playerID, action.nextPlayerID, action.colorWish);
        gA = new GameActions(GameActions.actions.DUEL_OPPONENT, action.nextPlayerID, action.playerID);
        update();
    }

    private void endDuel(GameActions action) {
        int loserID = this.duelData.getDuelLoserID(action.colorWish);
        this.duelData = null;
        logic.changeCardDrawCount(duelPunishment);
        forcedCardDraw(loserID);
        logic.nextPlayer(logic.getActivePlayer());
    }

    void cardSpin(GameActions action) {
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

    void blamePlayer(int accusingPlayerID, int accusedPlayerID) {
        if (droppedCard[accusedPlayerID] || tradedCard[accusedPlayerID]) {
            droppedCard[accusedPlayerID] = false;
            tradedCard[accusedPlayerID] = false;

            int amount = logic.getCardDrawCount();
            logic.changeCardDrawCount(dropCardPunishment);
            forcedCardDraw(accusedPlayerID);
            logic.changeCardDrawCount(amount);
            return;
        }
        if (mustCallUNO[accusedPlayerID]) {
            mustCallUNO[accusedPlayerID] = false;

            int amount = logic.getCardDrawCount();
            logic.changeCardDrawCount(tradeCardPunishment);
            forcedCardDraw(accusedPlayerID);
            logic.changeCardDrawCount(amount);
            return;
        }

        int amount = logic.getCardDrawCount();
        logic.changeCardDrawCount(accusingPunishment);
        forcedCardDraw(accusingPlayerID);
        logic.changeCardDrawCount(amount);
    }

    public LinkedList<LinkedList<Card>> getGottenHandsCards() {
        return gottenHandsCards;
    }
}