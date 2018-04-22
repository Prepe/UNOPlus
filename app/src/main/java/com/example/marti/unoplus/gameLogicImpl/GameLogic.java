package com.example.marti.unoplus.gameLogicImpl;

import android.content.ClipData;
import android.content.Context;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.support.v7.widget.AppCompatImageView;

import com.example.marti.unoplus.R;
import com.example.marti.unoplus.Screens.GameScreen;
import com.example.marti.unoplus.cards.Card;
import com.example.marti.unoplus.cards.CardEffects;
import com.example.marti.unoplus.cards.CardViewAdapter;
import com.example.marti.unoplus.cards.Deck;
import com.example.marti.unoplus.players.Player;
import com.example.marti.unoplus.players.PlayerList;

import java.util.LinkedList;

public class GameLogic extends AppCompatImageView implements View.OnTouchListener{
    PlayerList playerList;      //reference to all Players
    Deck deck;                  //reference to the Deck that is used
    Player activePlayer;        //well active player (its his turn)
    CardEffects effects;        //used to call CardEffects
    int cardDrawCount = 1;      //the amount the next Player has to draw from the deck
    boolean reverse = false;    //is the game currently reversed or not
    boolean skip = false;       //is the next Player suspended or not
    Card.values lastCardValue;  //The value of the card that is on top of the discard pile
    Card.colors lastCardColor;  //The color of the card that is on top of the discard pile
    private ImageView cvaCurrentCard, cvaStackCard;  //view for current and stack cards
    private LinkedList<CardViewAdapter> cvaHandCards;
    private ViewGroup caCurrentCardParent;
    private int height, width;


    public GameLogic (Context context, PlayerList pL, Deck gameDeck) {
        super(context);
        playerList = pL;
        deck = gameDeck;

        activePlayer = playerList.getFirst();
    }



    //Basic GameLogic should only be called when the card is good to play or player has to draw a card (card == null)
    public Player runLogic (Player player, Card card) {
        if (card == null) {
            player.drawCard();
        } else {
            playCard(card);
            effects.cardEffect(player, card);
        }
        return nextPlayer(player);
    }

    //Return the amount of Cards the Player has to draw and set next draw to 1
    public int getCardDrawCount () {
        int amount = cardDrawCount;
        cardDrawCount = 1;
        return amount;
    }

    //Change the CardDrawCount to either the shown amount on Card or add to last amount
    public void changeCardDrawCount(int amount) {
        if (cardDrawCount == 1) {
            cardDrawCount = amount;
        } else {
            cardDrawCount = cardDrawCount + amount;
        }
    }

    //returns the activePlayer
    public Player getActivePlayer() {
        return activePlayer;
    }

    /*
    * But the lastCard into the discard Pile that gets reused when Deck is empty
    * Make the played card the lastCard and trigger its effect on the game
    * */
    private void playCard(Card card) {
        lastCardValue = card.value;
        lastCardColor = card.color;
    }

    protected void generateHandCards() {
        LinkedList<CardViewAdapter> temp = new LinkedList<CardViewAdapter>();


        for (Card c:this.activePlayer.getHand())
            //temp.add(new CardViewAdapter(this,c)); TODO Check it!!!

        this.cvaHandCards = temp;
    }

    /*
    * Return the next Player after checking the direction of the game
    * TODO fix nextPlayer when PlayerList is updated
    * */
    private Player nextPlayer (Player player) {
        if (reverse) {
            if (skip) {
                skip = false;
                //activePlayer = playerList.previousPlayer(playerList.previousPlayer(player));
            } else {
                //activePlayer = playerList.previousPlayer(player);
            }
        } else {
            if (skip) {
                skip = false;
                //activePlayer = playerList.nextPlayer(playerList.nextPlayer(player));
            } else {
                //activePlayer = playerList.nextPlayer(player);
            }
        }
        return activePlayer;
    }

    /*
    * Checks if the Card the Player wants to play can be played
    * */
    public boolean checkCard (Card card) {
        // Check if last card was a +2 and the card Effect is still active
        if (lastCardValue == Card.values.PLUS_TWO && cardDrawCount > 1) {
            if (checkValue(card) || card.value == Card.values.PLUS_FOUR) {
                return true;
            }
            // Check if last card was a +4 and the card Effect is still active
        } else if (lastCardValue == Card.values.PLUS_FOUR && cardDrawCount > 1) {
            if (checkValue(card) || card.color == lastCardColor) {
                return true;
            }
            // When no +2 or +4 Effect is active make normal Card Check
        } else {
            //Check card for right Value
            if (checkValue(card)) {
                return true;

                //Check card for right Color
            } else if (checkColor(card)) {
                return true;
            }
        }

        // If all checks fail return Card cannot be played
        return false;
    }

    //Check for the colour of the card
    private boolean checkColor(Card card) {
        /*
        * Check for wild Card
        * Wild Cards should be playable no matter what
        * */
        if (card.getColor() == Card.colors.WILD) {
            return true;
        }
        /*
        * Check for matching Colour
        * */
        if (card.getColor() == lastCardColor) {
            return true;
        }

        /*
        * If the last Card was a Wild Card (Card.color == WILD) any Card can be played
        * */
        if (lastCardColor == Card.colors.WILD) {
            return true;
        }

        return false;
    }

    //Check for the Value of the card
    private boolean checkValue(Card card) {
        //Check for matching Value
        if (card.getValue() == lastCardValue) {
            return true;
        }

        return false;
    }

    //Plays 1 Card from the Deck without logic Checks
    public Card playTopCard() {
        Card card = deck.draw();
        lastCardValue = card.value;
        lastCardColor = card.color;
        effects.cardEffect(null, card);
        return this.playTopCard();
    }


    //Tels the game to skip the next player
    public void skipNext() {
        skip = true;
    }

    //Reverses the turn order when called
    public void toggleReverse() {
        if (reverse) {
            reverse = false;
        } else {
            reverse = true;
        }
    }

    //Sets the color requirement for next card
    public void wishColor(Card.colors colorWish) {
        lastCardColor = colorWish;
    }

    //TODO Change sizes
    private void initcvaStackCard() {
        cvaStackCard = (ImageView)findViewById(R.id.unostack);
        int cardSize = height / 3;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(cardSize, cardSize);
        int puffer = width / 2 - cardSize;

        cvaStackCard.setX(puffer);
        cvaStackCard.setY((height - cardSize) * .48f);

        cvaStackCard.setLayoutParams(lp);
        cvaStackCard.setBackground(this.getResources().getDrawable(R.drawable.card_back));
        //cvaStackCard.setOnTouchListener(this);

    }

    //TODO Change sizes
    private void initcvaCurrentCard() {
        cvaCurrentCard = (ImageView) findViewById(R.id.currentCard);
        int cardSize = height / 3;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(cardSize, cardSize);
        int puffer = width / 2;

        cvaCurrentCard.setX(puffer);
        cvaCurrentCard.setY((height - cardSize) * .48f);

        caCurrentCardParent = (ViewGroup) cvaCurrentCard.getParent();
        cvaCurrentCard.setLayoutParams(lp);
        //cvaCurrentCard.setOnDragListener(this);
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    private void removeAllViews() {
        removeHandCards();
    }

    private void removeHandCards() {
        if (cvaHandCards != null)
            for (CardViewAdapter cva : cvaHandCards) {
                ViewGroup parent = (ViewGroup) cva.getParent();
                parent.removeView(cva);
            }
    }

    private void renderAllViews() {
        renderHandCards();
        renderCurrentCard();
    }

    private void renderCurrentCard() {
        cvaCurrentCard.setBackground(CardViewAdapter.getCardPicture(this.playTopCard(),
                cvaCurrentCard));
    }

    //TODO Change sizes
    private void renderHandCards() {
        generateHandCards();

        int cardSize = getCardSize();
        int pufferLeft = (int) Math.floor((width - (cardSize * .30f * cvaHandCards.size() + cardSize * .70f)) / 2);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(cardSize, cardSize);

        //generate the ImageViewCard for the handcards
        for (CardViewAdapter cva : this.cvaHandCards) {
            //horizontal position set
            cva.setX(Math.round(pufferLeft + cvaHandCards.indexOf(cva) * cardSize * .30f));
            // vertical position set
            cva.setY((height - cardSize) * .99f);


            cva.setLayoutParams(lp);
            cva.setBaselineAlignBottom(true);

            //TODO Solve this problem, may be change location
            //GameScreen.addContentView(cva, cva.getLayoutParams());


            //cva.setOnTouchListener(this);
            cva.getBackground().clearColorFilter();}

    }

    //TODO Change sizes
    private int getCardSize() {
        int cardSizeH = (int) Math.floor(height * .94f / 3);
        int cardSizeW = (int) Math.floor(width  *2.9f / cvaHandCards.size());
        int cardSize = ((cardSizeH*.3f*cvaHandCards.size()>width*.90f) ? cardSizeW : cardSizeH);
        return cardSize;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == event.ACTION_DOWN) {
            if (v.getId() != cvaStackCard.getId()) {
                ClipData cd = ClipData.newPlainText("", "");
                // draged image
                View.DragShadowBuilder sb = new View.DragShadowBuilder(v);
                v.startDrag(cd, sb, v, 0);
            } else if (v.getId() == cvaStackCard.getId()) {
                this.activePlayer.drawCard();
                removeHandCards();
                renderHandCards();
            }
        }
        return false;
    }

}
