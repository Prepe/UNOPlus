package com.example.marti.unoplus.cards;

import android.content.Context;
import android.widget.ImageView;

import com.example.marti.unoplus.R;
import com.example.marti.unoplus.Screens.GameViewProt;

/**
 * Created by ekzhu on 04.06.2018.
 */

public class ThrowAwayView {
    ImageView view;

    public ThrowAwayView(Context context, GameViewProt screen) {
        this.view = (ImageView) screen.findViewById(R.id.throwaway);
        this.view.setTag(this);
        this.view.setOnDragListener(new ThrowAwayDragListener(screen));
    }
}
