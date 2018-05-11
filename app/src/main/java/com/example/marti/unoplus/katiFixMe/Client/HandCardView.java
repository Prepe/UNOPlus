package com.example.marti.unoplus.katiFixMe.Client;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.example.marti.unoplus.R;
//import com.example.marti.unoplus.Screens.CardViewTest;
import com.example.marti.unoplus.cards.Card;
import com.example.marti.unoplus.gameLogicImpl.GameViewProt;

/**
 * Created by ekzhu on 30.04.2018.
 */

public class HandCardView {
    public Card card = null;
    public ImageView view;


    public HandCardView(Context context, GameViewProt screen, Card card) {
        this.card = card;
        this.view = new ImageView(context);
        this.view.setTag(this);
        this.updateCardPicture();
        if (this.view != null) {
            this.view.setOnTouchListener(new HandCardTouchListener());
        }

    }

    public void updateCardPicture() {
        if (this.card == null) {
            this.view.setBackground(this.view.getResources().getDrawable(R.drawable.card_back));
            return;
        }
        Drawable pic = this.view.getResources().getDrawable(R.drawable.card_back);
        if (this.card.color == Card.colors.GREEN) {

            switch (this.card.value) {
                case ZERO:
                    pic = this.view.getResources().getDrawable(R.drawable.green_0);
                    break;
                case ONE:
                    pic = this.view.getResources().getDrawable(R.drawable.green_1);
                    break;
                case TWO:
                    pic = this.view.getResources().getDrawable(R.drawable.green_2);
                    break;
                case THREE:
                    pic = this.view.getResources().getDrawable(R.drawable.green_3);
                    break;
                case FOUR:
                    pic = this.view.getResources().getDrawable(R.drawable.green_4);
                    break;
                case FIVE:
                    pic = this.view.getResources().getDrawable(R.drawable.green_5);
                    break;
                case SIX:
                    pic = this.view.getResources().getDrawable(R.drawable.green_6);
                    break;
                case SEVEN:
                    pic = this.view.getResources().getDrawable(R.drawable.green_7);
                    break;
                case EIGHT:
                    pic = this.view.getResources().getDrawable(R.drawable.green_8);
                    break;
                case NINE:
                    pic = this.view.getResources().getDrawable(R.drawable.green_9);
                    break;
                case PLUS_TWO:
                    pic = this.view.getResources().getDrawable(R.drawable.green_plus2);
                    break;
                case TURN:
                    pic = this.view.getResources().getDrawable(R.drawable.green_turn);
                    break;
                case SKIP:
                    pic = this.view.getResources().getDrawable(R.drawable.green_skip);
                    break;
                case CHOOSE_COLOR:
                    pic = this.view.getResources().getDrawable(R.drawable.all_green);
                    break;
                default:
                    pic = this.view.getResources().getDrawable(R.drawable.card_back);
                    break;
            }
        } else if (this.card.color == Card.colors.BLUE) {
            switch (this.card.value) {
                case ZERO:
                    pic = this.view.getResources().getDrawable(R.drawable.blue_0);
                    break;
                case ONE:
                    pic = this.view.getResources().getDrawable(R.drawable.blue_1);
                    break;
                case TWO:
                    pic = this.view.getResources().getDrawable(R.drawable.blue_2);
                    break;
                case THREE:
                    pic = this.view.getResources().getDrawable(R.drawable.blue_3);
                    break;
                case FOUR:
                    pic = this.view.getResources().getDrawable(R.drawable.blue_4);
                    break;
                case FIVE:
                    pic = this.view.getResources().getDrawable(R.drawable.blue_5);
                    break;
                case SIX:
                    pic = this.view.getResources().getDrawable(R.drawable.blue_6);
                    break;
                case SEVEN:
                    pic = this.view.getResources().getDrawable(R.drawable.blue_7);
                    break;
                case EIGHT:
                    pic = this.view.getResources().getDrawable(R.drawable.blue_8);
                    break;
                case NINE:
                    pic = this.view.getResources().getDrawable(R.drawable.blue_9);
                    break;
                case PLUS_TWO:
                    pic = this.view.getResources().getDrawable(R.drawable.blue_plus2);
                    break;
                case TURN:
                    pic = this.view.getResources().getDrawable(R.drawable.blue_turn);
                    break;
                case SKIP:
                    pic = this.view.getResources().getDrawable(R.drawable.blue_skip);
                    break;
                case CHOOSE_COLOR:
                    pic = this.view.getResources().getDrawable(R.drawable.all_blue);
                    break;
                default:
                    pic = this.view.getResources().getDrawable(R.drawable.card_back);
                    break;
            }
        } else if (this.card.color == Card.colors.RED) {
            switch (this.card.value) {
                case ZERO:
                    pic = this.view.getResources().getDrawable(R.drawable.red_0);
                    break;
                case ONE:
                    pic = this.view.getResources().getDrawable(R.drawable.red_1);
                    break;
                case TWO:
                    pic = this.view.getResources().getDrawable(R.drawable.red_2);
                    break;
                case THREE:
                    pic = this.view.getResources().getDrawable(R.drawable.red_3);
                    break;
                case FOUR:
                    pic = this.view.getResources().getDrawable(R.drawable.red_4);
                    break;
                case FIVE:
                    pic = this.view.getResources().getDrawable(R.drawable.red_5);
                    break;
                case SIX:
                    pic = this.view.getResources().getDrawable(R.drawable.red_6);
                    break;
                case SEVEN:
                    pic = this.view.getResources().getDrawable(R.drawable.red_7);
                    break;
                case EIGHT:
                    pic = this.view.getResources().getDrawable(R.drawable.red_8);
                    break;
                case NINE:
                    pic = this.view.getResources().getDrawable(R.drawable.red_9);
                    break;
                case PLUS_TWO:
                    pic = this.view.getResources().getDrawable(R.drawable.red_plus2);
                    break;
                case TURN:
                    pic = this.view.getResources().getDrawable(R.drawable.red_turn);
                    break;
                case SKIP:
                    pic = this.view.getResources().getDrawable(R.drawable.red_skip);
                    break;
                case CHOOSE_COLOR:
                    pic = this.view.getResources().getDrawable(R.drawable.all_red);
                    break;
                default:
                    pic = this.view.getResources().getDrawable(R.drawable.card_back);
                    break;
            }
        } else if (this.card.color == Card.colors.YELLOW) {
            switch (this.card.value) {
                case ZERO:
                    pic = this.view.getResources().getDrawable(R.drawable.yellow_0);
                    break;
                case ONE:
                    pic = this.view.getResources().getDrawable(R.drawable.yellow_1);
                    break;
                case TWO:
                    pic = this.view.getResources().getDrawable(R.drawable.yellow_2);
                    break;
                case THREE:
                    pic = this.view.getResources().getDrawable(R.drawable.yellow_3);
                    break;
                case FOUR:
                    pic = this.view.getResources().getDrawable(R.drawable.yellow_4);
                    break;
                case FIVE:
                    pic = this.view.getResources().getDrawable(R.drawable.yellow_5);
                    break;
                case SIX:
                    pic = this.view.getResources().getDrawable(R.drawable.yellow_6);
                    break;
                case SEVEN:
                    pic = this.view.getResources().getDrawable(R.drawable.yellow_7);
                    break;
                case EIGHT:
                    pic = this.view.getResources().getDrawable(R.drawable.yellow_8);
                    break;
                case NINE:
                    pic = this.view.getResources().getDrawable(R.drawable.yellow_9);
                    break;
                case PLUS_TWO:
                    pic = this.view.getResources().getDrawable(R.drawable.yellow_plus2);
                    break;
                case TURN:
                    pic = this.view.getResources().getDrawable(R.drawable.yellow_turn);
                    break;
                case SKIP:
                    pic = this.view.getResources().getDrawable(R.drawable.yellow_skip);
                    break;
                case CHOOSE_COLOR:
                    pic = this.view.getResources().getDrawable(R.drawable.all_yellow);
                    break;
                default:
                    pic = this.view.getResources().getDrawable(R.drawable.card_back);
                    break;
            }
        } else if (this.card.color == Card.colors.WILD) {
            switch (this.card.value) {
                case PLUS_FOUR:
                    pic = this.view.getResources().getDrawable(R.drawable.all_plus4);
                    break;
                case CHOOSE_COLOR:
                    pic = this.view.getResources().getDrawable(R.drawable.all_all);
                    break;
                default:
                    pic = this.view.getResources().getDrawable(R.drawable.card_back);
                    break;
            }

        }
        this.view.setBackground(pic);
    }

}
