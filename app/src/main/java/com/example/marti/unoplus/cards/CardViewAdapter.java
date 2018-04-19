package com.example.marti.unoplus.cards;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.widget.ImageView;

import com.example.marti.unoplus.R;
import com.example.marti.unoplus.cards.Card;

/**
 * Created by ekzhu on 16.04.2018.
 */

public class CardViewAdapter extends AppCompatImageView {

    private Card card;

    public CardViewAdapter(Context context, Card card) {
        super(context);
        this.card = card;
        createImageView();
    }

    private void createImageView() {
        Drawable pic = getCardPicture(this.card, this);
        this.setBackground(pic);

    }

    public static Drawable getCardPicture(Card card, ImageView picture) {
        Drawable pic = picture.getResources().getDrawable(R.drawable.card_back);
        if (card.color == Card.colors.GREEN) {

            switch (card.value) {
                case ZERO:
                    pic = picture.getResources().getDrawable(R.drawable.green_0);
                    break;
                case ONE:
                    pic = picture.getResources().getDrawable(R.drawable.green_1);
                    break;
                case TWO:
                    pic = picture.getResources().getDrawable(R.drawable.green_2);
                    break;
                case THREE:
                    pic = picture.getResources().getDrawable(R.drawable.green_3);
                    break;
                case FOUR:
                    pic = picture.getResources().getDrawable(R.drawable.green_4);
                    break;
                case FIVE:
                    pic = picture.getResources().getDrawable(R.drawable.green_5);
                    break;
                case SIX:
                    pic = picture.getResources().getDrawable(R.drawable.green_6);
                    break;
                case SEVEN:
                    pic = picture.getResources().getDrawable(R.drawable.green_7);
                    break;
                case EIGHT:
                    pic = picture.getResources().getDrawable(R.drawable.green_8);
                    break;
                case NINE:
                    pic = picture.getResources().getDrawable(R.drawable.green_9);
                    break;
                case PLUS_TWO:
                    pic = picture.getResources().getDrawable(R.drawable.green_plus2);
                    break;
                case TURN:
                    pic = picture.getResources().getDrawable(R.drawable.green_turn);
                    break;
                case SKIP:
                    pic = picture.getResources().getDrawable(R.drawable.green_skip);
                    break;
                default:
                    pic = picture.getResources().getDrawable(R.drawable.card_back);
                    break;
            }
        } else if (card.color == Card.colors.BLUE) {
            switch (card.value) {
                case ZERO:
                    pic = picture.getResources().getDrawable(R.drawable.blue_0);
                    break;
                case ONE:
                    pic = picture.getResources().getDrawable(R.drawable.blue_1);
                    break;
                case TWO:
                    pic = picture.getResources().getDrawable(R.drawable.blue_2);
                    break;
                case THREE:
                    pic = picture.getResources().getDrawable(R.drawable.blue_3);
                    break;
                case FOUR:
                    pic = picture.getResources().getDrawable(R.drawable.blue_4);
                    break;
                case FIVE:
                    pic = picture.getResources().getDrawable(R.drawable.blue_5);
                    break;
                case SIX:
                    pic = picture.getResources().getDrawable(R.drawable.blue_6);
                    break;
                case SEVEN:
                    pic = picture.getResources().getDrawable(R.drawable.blue_7);
                    break;
                case EIGHT:
                    pic = picture.getResources().getDrawable(R.drawable.blue_8);
                    break;
                case NINE:
                    pic = picture.getResources().getDrawable(R.drawable.blue_9);
                    break;
                case PLUS_TWO:
                    pic = picture.getResources().getDrawable(R.drawable.blue_plus2);
                    break;
                case TURN:
                    pic = picture.getResources().getDrawable(R.drawable.blue_turn);
                    break;
                case SKIP:
                    pic = picture.getResources().getDrawable(R.drawable.blue_skip);
                    break;
                default:
                    pic = picture.getResources().getDrawable(R.drawable.card_back);
                    break;
            }
        } else if (card.color == Card.colors.RED) {
            switch (card.value) {
                case ZERO:
                    pic = picture.getResources().getDrawable(R.drawable.red_0);
                    break;
                case ONE:
                    pic = picture.getResources().getDrawable(R.drawable.red_1);
                    break;
                case TWO:
                    pic = picture.getResources().getDrawable(R.drawable.red_2);
                    break;
                case THREE:
                    pic = picture.getResources().getDrawable(R.drawable.red_3);
                    break;
                case FOUR:
                    pic = picture.getResources().getDrawable(R.drawable.red_4);
                    break;
                case FIVE:
                    pic = picture.getResources().getDrawable(R.drawable.red_5);
                    break;
                case SIX:
                    pic = picture.getResources().getDrawable(R.drawable.red_6);
                    break;
                case SEVEN:
                    pic = picture.getResources().getDrawable(R.drawable.red_7);
                    break;
                case EIGHT:
                    pic = picture.getResources().getDrawable(R.drawable.red_8);
                    break;
                case NINE:
                    pic = picture.getResources().getDrawable(R.drawable.red_9);
                    break;
                case PLUS_TWO:
                    pic = picture.getResources().getDrawable(R.drawable.red_plus2);
                    break;
                case TURN:
                    pic = picture.getResources().getDrawable(R.drawable.red_turn);
                    break;
                case SKIP:
                    pic = picture.getResources().getDrawable(R.drawable.red_skip);
                    break;
                default:
                    pic = picture.getResources().getDrawable(R.drawable.card_back);
                    break;
            }
        } else if (card.color == Card.colors.YELLOW) {
            switch (card.value) {
                case ZERO:
                    pic = picture.getResources().getDrawable(R.drawable.yellow_0);
                    break;
                case ONE:
                    pic = picture.getResources().getDrawable(R.drawable.yellow_1);
                    break;
                case TWO:
                    pic = picture.getResources().getDrawable(R.drawable.yellow_2);
                    break;
                case THREE:
                    pic = picture.getResources().getDrawable(R.drawable.yellow_3);
                    break;
                case FOUR:
                    pic = picture.getResources().getDrawable(R.drawable.yellow_4);
                    break;
                case FIVE:
                    pic = picture.getResources().getDrawable(R.drawable.yellow_5);
                    break;
                case SIX:
                    pic = picture.getResources().getDrawable(R.drawable.yellow_6);
                    break;
                case SEVEN:
                    pic = picture.getResources().getDrawable(R.drawable.yellow_7);
                    break;
                case EIGHT:
                    pic = picture.getResources().getDrawable(R.drawable.yellow_8);
                    break;
                case NINE:
                    pic = picture.getResources().getDrawable(R.drawable.yellow_9);
                    break;
                case PLUS_TWO:
                    pic = picture.getResources().getDrawable(R.drawable.yellow_plus2);
                    break;
                case TURN:
                    pic = picture.getResources().getDrawable(R.drawable.yellow_turn);
                    break;
                case SKIP:
                    pic = picture.getResources().getDrawable(R.drawable.yellow_skip);
                    break;
                default:
                    pic = picture.getResources().getDrawable(R.drawable.card_back);
                    break;
            }
        } else if (card.color == Card.colors.WILD) {
            switch (card.value) {
                case PLUS_FOUR:
                    pic = picture.getResources().getDrawable(R.drawable.all_plus4);
                    break;
                case CHOOSE_COLOR:
                    pic = picture.getResources().getDrawable(R.drawable.all_all);
                    break;
                default:
                    pic = picture.getResources().getDrawable(R.drawable.card_back);
                    break;
            }

        }
        return pic;
    }

    public Card getCard() {
        return this.card;
    }

    public String toString() {
        return this.getCard().get_name();
    }

}
