package com.example.marti.unoplus.sound;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.example.marti.unoplus.R;

/**
 * Created by sebit on 05.05.2018.
 */

public class SoundManager {
    private final MediaPlayer playerTurn;
    private final MediaPlayer dropCard;
    private final MediaPlayer winner;
    private final MediaPlayer looser;
    private final MediaPlayer theme;
    Context context;

    public SoundManager(Context mContext) {
        this.context = context;
        playerTurn = MediaPlayer.create(mContext, R.raw.playerturn);
        dropCard = MediaPlayer.create(mContext, R.raw.dropcard);
        winner = MediaPlayer.create(mContext, R.raw.winner);
        looser = MediaPlayer.create(mContext, R.raw.looser);
        theme = MediaPlayer.create(mContext, R.raw.unotheme);

    }

    public void playSound(Sounds sound) {
        switch (sound) {
            case DROPCARD:
                dropCard.start();
                break;

            case PLAYERTURN:
                playerTurn.start();
                break;

            case WINNER:
                winner.start();
                break;

            case LOOSER:
                looser.start();
                break;

            case THEMESTART:
                theme.start();
                theme.setLooping(true);
                break;

            case THEMESTOP:
                theme.pause();
                //theme.release();
                break;

            default:
                break;
        }
    }
}
