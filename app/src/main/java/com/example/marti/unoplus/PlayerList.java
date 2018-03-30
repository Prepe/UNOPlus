package com.example.marti.unoplus;

import java.util.ArrayList;
import java.util.LinkedList;

public class PlayerList {

    LinkedList<Player> players;
    private Player player;
    private PlayerList next;
    private PlayerList previous;
    private PlayerList first;


    public void setPlayers(ArrayList<Player> players){
        this.players = new LinkedList<>(players);
    }

    public void removePlayer(String name){
        //TO DO
    }



    public Player getPlayer(String name){
        return this.player;
    }

    public void getNext(){
        //TO DO

    }

    public void getPrevious(){
        //TO DO

    }

    public void getFirst(){
        //TO DO
    }

}
