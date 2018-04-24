package com.example.marti.unoplus.Screens;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marti.unoplus.R;
import com.example.marti.unoplus.players.Player;

import jop.hab.net.MainActivityTest;

public class GameScreen extends AppCompatActivity {
    public GameScreen() {
        super();
    }

    Button unoButton;


    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.game_screen);


        String[] players = {"Player 1", "Player2", "Player3", "Player4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_list_item_1, players);

        ListView lv = (ListView)findViewById(R.id.list);
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                final TextView mTextView = (TextView)view;
                switch (position) {
                    case 0:
                        //Toast.makeText(getApplicationContext(), "Player 1", Toast.LENGTH_SHORT).show();
                        //TO DO
                        if(Player.cheated == true){
                            //TODO
                        }
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "Player 2", Toast.LENGTH_SHORT).show();
                        //TO DO
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "Player 3", Toast.LENGTH_SHORT).show();
                        //TO DO
                        break;
                    case 3:
                        Toast.makeText(getApplicationContext(), "Plöayer 4", Toast.LENGTH_SHORT).show();
                        //TO DO
                        break;
                    default:
                        // Nothing do!
                }

            }
        });

        unoButton = (Button) findViewById(R.id.unounobutton);
        unoButton.setOnClickListener(handler);

        final TextView myCounter = (TextView) findViewById(R.id.countdown);
        new CountDownTimer(20000, 1000) {

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(), "ZEIT VORBEI! Karte gezogen", Toast.LENGTH_LONG).show();
                //timeUp(context);    eventuell so oder mit TOAST
                this.start();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                myCounter.setText("Verbleibende Zeit: "
                        + String.valueOf(millisUntilFinished / 1000));
            }
        }.start();


    }

    public void timeUp(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(
                GameScreen.this);
        builder.setTitle("Zeit vorbei!")
                .setMessage("Ziehe Karte")
                .setCancelable(false)
                .setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                GameScreen.this.getApplicationContext();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    View.OnClickListener handler = new View.OnClickListener() {
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.unounobutton:
                    Toast.makeText(getApplicationContext(), "Uno!!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
