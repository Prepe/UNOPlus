package jop.hab.net;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.marti.unoplus.R;

public class GameView_devImpl extends AppCompatActivity  implements  ObserverInterface{


    TextView textView;
    EditText editTextSend;
    Button btnSend;

    String hostAdress;
    String mode;
    NetworkIOManager networkIOManager;


    //Hole aus Intent die Server adresse und andere Infos
    //Erzeuge Instanz von NetworkIOManager (Adresse übergebem)
    //NetworkIOManager Callt diese Avtivity wenn daten sich geändert haben



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Nicht optimal...todo!!!

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_game_view_dev_impl);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        textView = (TextView) findViewById(R.id.textView);
        editTextSend = (EditText)findViewById(R.id.txtSend);
        btnSend=(Button) findViewById(R.id.btnSend);

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
            }
        });
    }


    @Override
    public void dataChanged() {

        textView.setText(networkIOManager.getTestText());

    }
}
