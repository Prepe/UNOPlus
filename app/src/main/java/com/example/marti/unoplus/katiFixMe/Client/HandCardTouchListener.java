package com.example.marti.unoplus.katiFixMe.Client;

import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ekzhu on 01.05.2018.
 */

public class HandCardTouchListener implements View.OnTouchListener {

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            //view.setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }
    }
}
