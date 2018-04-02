package com.example.marti.unoplus;

import java.io.Serializable;

/**
 * Created by ekzhu on 31.03.2018.
 */

public class Card implements Serializable{

    //Create 4 different card colors + universal color (for all)
    public enum colors {
        RED,
        BLUE,
        GREEN,
        YELLOW,
        ALL
    }
    public colors color;

    //Create card values
    public enum values {
        ZERO,
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        SKIP,
        PLUS_TWO,
        TURN,
        PLUS_FOUR,
        COLOR_CHOOSE
        }

    public values value;

    //SpecialCard makes needs some additional actions, e.g. take 2 or 4 cards, choose color
    boolean	specialCard = false;

    public Card (Card.colors color, Card.values value) {
        this.color = color;
        this.value = value;
        if 	(this.color.equals(Card.colors.ALL) || this.value.equals(Card.values.SKIP)
                || this.value.equals(values.PLUS_TWO) || this.value.equals(Card.values.TURN)) {
            this.specialCard = true;
        } else {
            this.specialCard = false;
        }
    }

    public String get_name() {
        return ((this.color).toString() + "_" + (this.value).toString());
    }

    public boolean isSpecialCard() {
        return this.specialCard;
    }

    @Override
    public String toString() {
        return "Card{" + "color=" + color + ", value=" + value + ", specialCard=" + specialCard + '}';
    }

}
