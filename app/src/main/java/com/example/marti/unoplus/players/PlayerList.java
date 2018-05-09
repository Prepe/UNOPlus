package com.example.marti.unoplus.players;

import com.example.marti.unoplus.gameLogicImpl.GameViewProt;
import com.example.marti.unoplus.players.Player;

import java.util.ArrayList;
import java.util.LinkedList;

public class PlayerList {

    LinkedList<Player> players;
    private Player active_player;
    GameViewProt gameViewProt;


    //String[] player1 = new String[players.size()];
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

        if(i == players.indexOf(players.getLast())){
            return getFirst();
        }

        return players.get(i+1);

    }

    public Player getPrevious(Player p){
        int i = players.indexOf(p);

        if(i == players.indexOf(players.getFirst())){
            return players.getLast();
        }

        return players.get(i-1);
    }

    public Player getPlayer (int ID) {
        for (Player player : players) {
            if (player.getID() == ID) {
                return player;
            }
        }

        return null;
    }

    public int playerCount () {
        return players.size();
    }

    public Player getFirst(){
        return this.players.getFirst();
    }

    public LinkedList<Player> getPlayers() {
        return players;
    }
}
