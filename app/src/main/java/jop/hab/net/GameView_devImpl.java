package jop.hab.net;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marti.unoplus.Card;
import com.example.marti.unoplus.R;

public class GameView_devImpl extends AppCompatActivity implements ObserverInterface {


    TextView textView;
    EditText editTextSend;
    Button btnSend;
    ImageButton btnr8;
    ImageButton btng7;
    ImageButton btnb3;
    ImageView playCard;
    String hostAdress;
    String mode;
    NetworkIOManager networkIOManager;
    Card card;

    //Hole aus Intent die Server adresse und andere Infos
    //Erzeuge Instanz von NetworkIOManager (Adresse übergebem)
    //NetworkIOManager Callt diese Avtivity wenn daten sich geändert haben


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Nicht optimal...todo!!!

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.game_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        textView = (TextView) findViewById(R.id.textView);
        editTextSend = (EditText) findViewById(R.id.txtSend);

        btnSend = (Button) findViewById(R.id.btnSend);
        btnb3 = (ImageButton) findViewById(R.id.btn_b);
        btng7 = (ImageButton) findViewById(R.id.btn_g);
        btnr8 = (ImageButton) findViewById(R.id.btn_r);
        playCard = (ImageView) findViewById(R.id.img_playCard);


        hostAdress = getIntent().getStringExtra("adress");
        mode = getIntent().getStringExtra("mode");

        networkIOManager = new NetworkIOManager(this);
        networkIOManager.setMode(mode);
        networkIOManager.setHostAdress(hostAdress);
        networkIOManager.open();


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                networkIOManager.writeMsg(editTextSend.getText().toString());

                //networkIOManager.writeCard(card);


            }
        });

        btnr8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                networkIOManager.writeMsg("r8");


            }
        });

        btnb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                networkIOManager.writeMsg("b3");


            }
        });

        btng7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                networkIOManager.writeMsg("g7");


            }
        });
    }


    @Override
    public void dataChanged() {

        textView.setText(networkIOManager.getTestText());
        setCard(networkIOManager.getTestText());

        //todo Card holen
        //card = networkIOManager.getCard();


    }

    // nur für Demo Zwecke
    public void setCard(String cardID) {

        if (cardID.equals("r8")) {
            playCard.setImageResource(R.drawable.red_8);
        } else if (cardID.equals("g7")) {
            playCard.setImageResource(R.drawable.green_7);
        } else if (cardID.equals("b3")) {
            playCard.setImageResource(R.drawable.blue_3);

        }

    }

}
