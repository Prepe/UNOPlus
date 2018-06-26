package com.example.marti.unoplus.cards;

import android.content.Context;
import android.widget.ImageView;

import com.example.marti.unoplus.R;
import com.example.marti.unoplus.Screens.GameViewProt;

/**
 * Created by sebit on 09.06.2018.
 */

public class TradeCardView {
    private ImageView view;

    public TradeCardView(Context context, GameViewProt screen) {
        this.view = (ImageView) screen.findViewById(R.id.tradeCard);
        this.view.setTag(this);
        this.view.setOnDragListener(new TradeCardDragListener(screen));
    }
}
