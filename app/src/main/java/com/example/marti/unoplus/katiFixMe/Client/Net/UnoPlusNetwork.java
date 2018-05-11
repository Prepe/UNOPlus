package com.example.marti.unoplus.katiFixMe.Client.Net;

/**
 * Created by ekzhu on 30.04.2018.
 */

//General container for all network classes, is contained as static (globally accessible)
// object in GameStatics and gets created on create lobby/join lobby button press

//add relevant functions that relay function calls to network member objects
//For example, if player wants to play a card the call chain would be:
//1) Client CardViewTest drops a card on playdeck spot, calls CardViewTest.playCard(card)
//2) CardViewTest.playCard(card) calls GameStatics.net.playCard(cardobject) (this class here)
//3) This class here relays that play card request to the appropiate network member object,
//for example this.NetworkWifiBroadcaster.sendToServerIWantToPlayCard(card) to send to the server what card
//the client wants to play
//In summary the call chain would be:
//Client : CardViewTest.playCard(card) -> GameStatics.net.playCard(cardobject) -> this.NetworkWifiBroadcaster.sendToServerIWantToPlayCard(card)

import android.util.Log;

import com.example.marti.unoplus.GameStatics;
import com.example.marti.unoplus.Screens.CardViewTest;
import com.example.marti.unoplus.cards.Card;
import com.example.marti.unoplus.players.Player;

import java.util.ArrayList;

//That way we have a central and global accessible point through which all network-relevant
//data gets sent via function call chains

public class UnoPlusNetwork {
    //If button press is "create lobby as server" pass "true" as parameter
    //All clients that join via "join session" should pass false
    public UnoPlusNetwork(boolean isServer) {
        this.bIsServer = isServer;
        //create all network relevant objects here
    }

    ArrayList<Player> GetConnectedPlayers() {
        return null; //TODO : Get list from the network object where the player list is
    }

    //=================
    //Network messages
    //=================
    //Playing cards
    //=================

    //Called by Client
    //Called by network objects when a message from server is recieved to take a card in the owners hand
    public void CLIENT_GetNewCardForHand(int playerid, Card card) {
        //TODO :
        // Check if that playerid belongs to me
        // Get my player object and create card in hand
        // Update Gamescreen ui

        CardViewTest game = (CardViewTest) GameStatics.currentActivity;
        if(game == null)
        {

            return;
        }

        if(GameStatics.devMode)
        {
            Log.d("GameDebug", "CLIENT_GetNewCardForHand : " + playerid + "," + card.value.toString() + " " + card.color.toString());
        }

        //TODO : Check if it is my player id

        //if i am the player with that player id and am in game screen:
       // game.addCardToHand(card);
    }


    //Called by Client
    // called by CardViewTest if client wants to play a card by dropping the card to the playdeck spot
    public void CLIENT_PlayCard(Card cardToPlay) {
        //TODO : call internal network objects to send send message to Server : int myplayerid, card whichcardtoplay

        //For testing purposes and until the network is fully connected with the game we just pretend that we are calling the server
        if(GameStatics.devMode == true) {
            Log.d("GameDebug", "CLIENT_PlayCard : " + cardToPlay.value.toString() + " " + cardToPlay.color.toString());
            SERVER_OnClientMessage_PlayCard(0, cardToPlay);
        }
    }


    //Called by Server
    // called by network objects when the server network object recieves message that a client wants to play a card
    // This function is called, which checks if the card can be played and broadcasts any changes
    // resulting by the card play
    void SERVER_OnClientMessage_PlayCard(int playerid, Card cardToPlay) {
        //calling this function on clients is a no-no!
        if (!this.bIsServer) {
            return;
        }

        //TODO call game rules object and handle cardplay:
        // Check if its that players turn
        // Check if the player can play that card
        // If yes : Update server-side card arrays accordingly
        //          Broadcast end of turn
        //          Broadcast for that player to remove the card from his hand
        //          Broadcast to update currently played card
        //          Broadcast start of turn of next player
        // If no : ignore message

        //For testing we just assume the server responds with packets that activate the following functions on the client:
        if(GameStatics.devMode)
        {
            Log.d("GameDebug", "SERVER_OnClientMessage_PlayCard : " + playerid + "," + cardToPlay.value.toString() + " " + cardToPlay.color.toString());
            CLIENT_OnServerMessage_UpdatePlayedCard(cardToPlay);
            CLIENT_OnServerMessage_RemoveCardFromHand(0, cardToPlay);
        }
    }

    //Called by Client
    // called by network objects when the Clients network object recieves message from server to end the turn
    public void CLIENT_OnServerMessage_EndOfTurn() {
        //TODO : Disable all players turn
    }

    //Called by Client
    // called by network objects when the Clients network object recieves message from server to remove a card from the players hand
    public void CLIENT_OnServerMessage_RemoveCardFromHand(int playerid, Card cardtoRemove) {
        //TODO :
        // Check if i'm that player (via playerid)
        // If not : ignore message
        // If yes : remove card from my hand, update hand cards ui

        CardViewTest game = (CardViewTest) GameStatics.currentActivity;
        if(game == null)
        {
            return;
        }
        Log.d("GameDebug", "CLIENT_OnServerMessage_RemoveCardFromHand : " + playerid + "," + cardtoRemove.value.toString() + " " + cardtoRemove.color.toString());
        game.removeCardFromHand(cardtoRemove);
    }

    //Called by Client
    // Called by network objects when network objects recieve message to update the currently played card
    public void CLIENT_OnServerMessage_UpdatePlayedCard(Card newcard) {

        CardViewTest game = (CardViewTest) GameStatics.currentActivity;
        if(game == null)
        {
            return;
        }
        Log.d("GameDebug", "CLIENT_OnServerMessage_UpdatePlayedCard : " + newcard.value.toString() + " " + newcard.color.toString());
        game.UpdateCurrentPlayCard(newcard);

    }

    //Called by Client
    // Called by CardViewTest when a player wants to say Uno
    public void CLIENT_SayUno() {
        //TODO :
        // Call network objects to s end message to server that player wants to say uno
    }

    //Called by Server
    // Called by network objects when Server recieves message that a player wants to say uno
    public void SERVER_OnClientMessage_SayUno(int playerid) {
        //calling this function on clients is a no-no!
        if (!this.bIsServer) {
            return;
        }
        //TODO :
        // Check if it is that players turn
        // Check if that player is allowed to say uno
    }


    //TODO : more network functions (wishing colors, etc)


    public boolean bIsServer = false;

    //add all network relevant classes here as members


}
