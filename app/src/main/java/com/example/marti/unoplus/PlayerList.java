package com.example.marti.unoplus;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class PlayerList {

    public static LinkedList<Player> players;
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

        if(getNext(p) == null){
            return getFirst();
        }

        return players.get(i+1);

    }

    public Player getPrevious(Player p){
        int i = players.indexOf(p);

        if(getPrevious(p) == null){
            return players.getLast();
        }

        return players.get(i-1);
    }
    

    public Player getFirst(){
        return players.getFirst();
    }

}
