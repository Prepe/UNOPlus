package com.example.marti.unoplus.gameLogicImpl;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.marti.unoplus.GameActions;
import com.example.marti.unoplus.R;
import com.example.marti.unoplus.Screens.GameScreen;
import com.example.marti.unoplus.Server.ServerLogic;
import com.example.marti.unoplus.Server.TakeDeck;
import com.example.marti.unoplus.cards.Card;
import com.example.marti.unoplus.cards.Deck;
import com.example.marti.unoplus.players.Player;
import com.example.marti.unoplus.players.PlayerList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jop.hab.net.NetworkIOManager;
import jop.hab.net.ObserverInterface;

/**
 * Created by marti on 10.04.2018.
 */

//Der GC muss das ObserverInterface implementieren, wichtig für automatische Datenabrfrage (Observer Pattern)

public class GameControler extends AppCompatActivity implements ObserverInterface {
    PlayerList players;//reference to all Players in the Game
    Deck deck;              //reference to the Deck that is used
    GameLogic logic;        //reference to the GameLogic
    NetworkIOManager NIOmanager;
    int startingHand = 7;//Amount of Cards every Player gets at the start of the Game
    float turnTime;         //Turn Timer for the Game
    public GameActions gA;         //Object that gets send to all Players
    boolean[] calledUNO;    //
    boolean[] dropedCard;   //
    boolean[] tradedCard;   //
    String hostAdress;
    String mode;
    GameActions recievedGA;

    //Lokale GameAction für empfangene Spielzüge


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.game_screen);

        //Hier werden die IP und der Modus über den Intent aus der MainActivityTest abgefragt

        hostAdress = getIntent().getStringExtra("adress");
        mode = getIntent().getStringExtra("mode");

        //der NIOManager wird instanziert und die Parameter werden übergeben
        //Der NIO kann daten schreiben und empfangen.
        //Empfangen funktioniert automatisch über die dataChanged Mehtode..siehe unten
        //Schreiben der Daten geht über writeGameAction()... kann alles belieben geändert/erweitert werden

        NIOmanager = new NetworkIOManager(this);
        NIOmanager.setMode(mode);
        NIOmanager.setHostAdress(hostAdress);
        NIOmanager.open();

        setUpGame();
    }

    /*public GameControler(PlayerList playersList, Deck gameDeck, GameLogic gameLogic) {
        players = playersList;
        deck = gameDeck;
        logic = gameLogic;

        setUpGame();
    }*/

    //Method to Update all Players after GC and GL have finished
    public void updateAllPlayers() {

        if (gA.nextPlayerID != null) {
            calledUNO[gA.nextPlayerID] = false;
            dropedCard[gA.nextPlayerID] = false;
            tradedCard[gA.nextPlayerID] = false;
        }

        NIOmanager.writeGameaction(gA);
    }

    //Give all Players cards and Play the first Card
    private void setUpGame() {
        players = new PlayerList();
        ArrayList<Player> pl = new ArrayList<Player>();
        Player player = new Player(1);
        pl.add(player);
        player = new Player(2);
        pl.add(player);
        players.setPlayers(pl);
        //@TODO player müssen noch korrekt in die Playerlist eingefügt werden, momentan nur zu Probezwecken
        deck = new Deck();
        logic = new GameLogic(players, deck);

        deck.shuffle();
        drawHandCardsForPlayers();

        calledUNO = new boolean[players.playerCount()];
        dropedCard = new boolean[players.playerCount()];
        tradedCard = new boolean[players.playerCount()];

        gA = new GameActions(GameActions.actions.UPDATE,logic.playTopCard(),logic.activePlayer.getID());
        updateAllPlayers();
    }

    private void drawHandCardsForPlayers() {
        for (Player p : players.getPlayers()) {
            List<Card> handcards = new LinkedList<>();
            for (int i = 0; i < startingHand; i++) {
                handcards.add(deck.draw());
            }

            gA = new GameActions(GameActions.actions.DRAW_CARD,p.getID(),handcards);

            updateAllPlayers();
        }
    }

    //Method for all Players to call to draw Cards form the Deck
    public void drawCard(int playerID) {
        List<Card> cards = new LinkedList<>();
        if (deck.isEmptyDeck()) {
            deck.replaceTakeDeck();
        }
        for (int i = 0; i < logic.getCardDrawCount(); i++) {
            cards.add(deck.draw());
        }

        gA = new GameActions(GameActions.actions.DRAW_CARD,playerID,cards);

        updateAllPlayers();
    }

    //Method for playing cards
    public void playCard(int player, Card card) {
        Player p = players.getPlayer(player);
        //Check if player is allowed to play the card
        if (logic.checkCard(card, p)) {
            //Remove the played card from the players hand
            gA = new GameActions(GameActions.actions.PLAY_CARD,player,card);
            updateAllPlayers();

            //Run game logic for the card that was played
            logic.runLogic(p, card);
        }
    }

    /*
    * Method that times each Turn for Players
    * When at 0 draws a Card for the active Player
    * */
    private void runTimer() {
        //TODO implement
    }

    public void colorWish (int player, Card.colors color) {
        logic.wishColor(color);
        Player p = players.getPlayer(player);
        logic.nextPlayer(p);

        gA = new GameActions(GameActions.actions.UPDATE,new Card(logic.lastCardColor,logic.lastCardValue),logic.getActivePlayer().getID());
    }


    //Method to cheat and drop a Card
    public void dropCard(int player) {
        if (!dropedCard[player]) {
            dropedCard[player] = true;
            gA = new GameActions(GameActions.actions.DROP_CARD,player,true);
        }
    }

    //Method to cheat and trade a Card with a Player
    public Card tradeCard(Player player, Card card) {
        //TODO implement

        return card;
    }

    public Player getWinningPlayer() {
        for (Player p : players.getPlayers()) {
            if (p.getHandSize() == 0) {
                return p;
            }
        }
        return null;
    }

    public boolean won() {
        for (Player p : players.getPlayers()) {
            if (p.getHandSize() == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void dataChanged() {

        //des is des wichtigste
        //wenn etwas empfangen wird, dann wird diese Methode vom NIO gecallt (Übers ObserverINterface)

        recievedGA = NIOmanager.getGameAction();

        //von diesem Punkt weg, wisst ihr, dass neue Daten bereit sind und ihr die Änderungen zeichnen könnt

    }
}
