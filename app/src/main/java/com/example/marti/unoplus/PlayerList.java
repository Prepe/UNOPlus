package com.example.marti.unoplus;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class PlayerList {

    LinkedList<Player> players;
    private Player active_player;


    public void setPlayers(ArrayList<Player> players){
        this.players = new LinkedList<>(players);
    }

    public void removePlayer(String name){
        //TO DO
    }
    
    public Player getPlayer(String name){
        return this.active_player;
    }


    public Player getNext(Player p){
        int i = players.indexOf(p);
        return players.get(i+1);

    }

    public Player getPrevious(Player p){
        int i = players.indexOf(p);
        return players.get(i-1);
    }
    

    public Player getFirst(){
        return this.players.getFirst();
    }

}
