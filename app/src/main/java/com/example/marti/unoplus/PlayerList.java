package com.example.marti.unoplus;

import com.example.marti.unoplus.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

public class PlayerList {

    LinkedList<Player> players;
    private Player active_player;
    ListIterator<Player> player_i;


    public void setPlayers(ArrayList<Player> players){
        this.players = new LinkedList<>(players);
    }

    public void removePlayer(String name){
        //TO DO
    }
    
    public Player getPlayer(String name){
        return this.active_player;
    }

    public Player getNext(){
        return this.player_i.next();
    }

    public Player getPrevious(){
        return this.player_i.previous();
    }

    public Player getFirst(){
        return this.players.getFirst();
    }

    public LinkedList<Player> getPlayers() {
        return players;
    }
}
