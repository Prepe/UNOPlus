package com.example.marti.unoplus.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.marti.unoplus.GameStatics;
import com.example.marti.unoplus.R;
import com.example.marti.unoplus.sound.SoundManager;
import com.example.marti.unoplus.sound.Sounds;

public class LosingScreen extends AppCompatActivity {
    SoundManager soundManager;
    TextView losingText;
    Integer pID;

    public LosingScreen() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GameStatics.currentActivity = this;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.losing_screen);

        soundManager = new SoundManager(this);
        soundManager.playSound(Sounds.LOOSER);

        losingText = findViewById(R.id.losingText);

        if (getIntent().hasExtra("pID")) {
            pID = getIntent().getExtras().getInt("pID");
            losingText.setText("Vielleicht beim nächsten Mal ;) \n Spieler " + pID + " hat Gewonnen");

            Thread titlescreenthread = new Thread() {
                public void run() {
                    try {
                        sleep(8000);
                        Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                        startActivity(intent);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            titlescreenthread.start();
        }
    }
}
