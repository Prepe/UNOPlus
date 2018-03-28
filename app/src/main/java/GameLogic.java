import java.util.LinkedList;

public class GameLogic {
    LinkedList<String> playerList; //TODO Change Type to PlayerList when implemented
    String lastCard; // TODO Change Type to Card when Card is implemented
    int nextCardDraw;
    boolean reverse;

    public GameLogic (LinkedList<String> pL) {
        playerList = pL;
    }

    public String RunLogic (String aktivePlayer, String card) {
        if (card == null) {
            DrawCard(aktivePlayer);
        } else {
            TryToPlayCard(card);
        }
        return NextPlayer(aktivePlayer);
    }

    private void DrawCard (String aktivePlayer) {
        //TODO call the DrawCard method from Player and Change Type from String to Player
    }

    private void TryToPlayCard (String card) {
        boolean cardCheck = false;
        cardCheck = CheckCard(card);
    }

    private String NextPlayer (String aktivePlayer) {
        //TODO call the NextPlayer Method from the PlayerList class
        return "";
    }

    private boolean CheckCard (String card) {
        boolean check = false;

        if (CheckColor(card)) {
            check = true;
        } else if (CheckValue(card)) {
            check = true;
        }

        return check;
    }

    private boolean CheckColor(String card) {
        return (true); //TODO implement color check with last card
    }

    private boolean CheckValue (String card) {
        return (true); //TODO implement value check with last card
    }
}
