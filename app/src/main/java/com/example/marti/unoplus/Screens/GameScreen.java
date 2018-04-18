package com.example.marti.unoplus.Screens;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.marti.unoplus.R;


public class GameScreen extends AppCompatActivity {
    public GameScreen() {
        super();
    }

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


    }
}
