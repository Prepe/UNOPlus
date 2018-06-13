package com.example.marti.unoplus.cards;

import java.io.Serializable;

/**
 * Created by ekzhu on 31.03.2018.
 */

public class Card implements Serializable {

    //Create 4 different card colors + wild color (for all)
    public enum colors {
        RED(0),
        BLUE(1),
        GREEN(2),
        YELLOW(3),
        WILD(4);

        private int value;

        private colors(int value) {
            this.value = value;
        }
    }

    public colors color;

    //Create card values
    public enum values {
        ZERO(0),
        ONE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        SKIP(10),
        TURN(11),
        PLUS_TWO(12),
        CHOOSE_COLOR(13),
        PLUS_FOUR(14),
        HOT_DROP(15),
        CARD_SPIN(16),
        DUEL (17);

        private int value;

        private values(int value) {
            this.value = value;
        }
    }

    public values value;

    public Card(String color, String val) {
        this.color = colors.valueOf(color);
        this.value = values.valueOf(val);
    }

    public Card(Card.colors color, Card.values value) {
        this.color = color;
        this.value = value;
    }

    Card(int cardColor, int cardValue) {

        this.color = colors.values()[cardColor];
        this.value = values.values()[cardValue];

    }

    public boolean hasSameCardValueAs(Card otherCard) {
        return (this.value == otherCard.value && this.color == otherCard.color);
    }

    public colors getColor() {
        return this.color;
    }

    public values getValue() {
        return this.value;
    }

    public String get_name() {
        return ((this.color).toString().toLowerCase() + "_" + (this.value).toString().toLowerCase());
    }

    public boolean isWildCard() {
        return this.color == colors.values()[4];
    }

    public boolean isTakeTwo() {
        return this.value == values.values()[12];
    }

    public boolean isSkip() {
        return this.value == values.values()[10];
    }

    public boolean isTakeFour() {
        return this.value == values.values()[14];
    }

    public boolean isHotDrop(){ return this.value == values.values()[15];}

    public boolean isCardSpin() {
        return this.value == values.values()[16];
    }

    public boolean isDuel() {
        return this.value == values.values()[17];
    }

}
