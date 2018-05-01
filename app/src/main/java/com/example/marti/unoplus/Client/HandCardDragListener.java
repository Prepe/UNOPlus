package com.example.marti.unoplus.Client;

import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.marti.unoplus.GameStatics;

/**
 * Created by ekzhu on 01.05.2018.
 */

public class HandCardDragListener implements View.OnDragListener {
    // Drawable enterShape = getResources().getDrawable(R.drawable.all_blue);
    //Drawable normalShape = getResources().getDrawable(R.drawable.all_red);

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
                HandCardView handcard = (HandCardView) droppedview.getTag();

                GameStatics.net.CLIENT_PlayCard(handcard.card);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                //            v.setBackgroundDrawable(normalShape);
            default:
                break;
        }
        return true;
    }
}
