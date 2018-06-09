package com.example.marti.unoplus.cards;

import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.marti.unoplus.Screens.GameViewProt;

/**
 * Created by sebit on 09.06.2018.
 */

public class TradeCardDragListener implements View.OnDragListener {
    public GameViewProt gamescreen = null;
    public Card card;

    public TradeCardDragListener(GameViewProt screen)
    {
        this.gamescreen = screen;
    }
    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // do nothing
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                //          v.setBackgroundDrawable(enterShape);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                //        v.setBackgroundDrawable(normalShape);
                break;
            case DragEvent.ACTION_DROP:

                ImageView droppedview = (ImageView) event.getLocalState();
                HandCardView playedcard = (HandCardView) droppedview.getTag();
                this.gamescreen.choosePlayer();

                break;
            case DragEvent.ACTION_DRAG_ENDED:
                //            v.setBackgroundDrawable(normalShape);
            default:
                break;
        }
        return true;
    }
}
