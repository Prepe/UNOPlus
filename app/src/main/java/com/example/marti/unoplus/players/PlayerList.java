package com.example.marti.unoplus.players;

import com.example.marti.unoplus.Screens.GameViewProt;

import java.util.LinkedList;

public class PlayerList {
    LinkedList<Player> players;

    //String[] player1 = new String[players.size()];
    public void setPlayers(LinkedList<Player> players){
        this.players = players;
    }

    // returns the next player
    public Player getNext(Player p){
        int i = players.indexOf(p);

        if(i == players.indexOf(players.getLast())){
            return getFirst();
        }

        return players.get(i+1);

    }

    //returns the previous player
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
