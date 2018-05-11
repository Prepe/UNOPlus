package jop.hab.net.Redundant;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marti.unoplus.cards.Card;
import com.example.marti.unoplus.players.Player;
import com.example.marti.unoplus.R;

import jop.hab.net.NetworkIOManager;
import jop.hab.net.ObserverInterface;

public class GameView_devImpl extends AppCompatActivity implements ObserverInterface {


    TextView textView;
    EditText editTextSend;
    Button btnSend;
    Button btnUnoUno;
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

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Nicht optimal...todo!!!

        /*PlayerList PL = new PlayerList();
        ArrayList<Player> pl = new ArrayList<Player>();
        Player player = new Player(1);
        pl.add(player);
        player = new Player(2);
        pl.add(player);
        PL.setPlayers(pl);
        //@TODO player müssen noch korrekt in die Playerlist eingefügt werden, momentan nur zu Probezwecken
        Deck deck = new Deck();
        GameLogic GL = new GameLogic(PL, deck);
        GameController GC = new GameController();
        GC.setUpGame();*/

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.game_screen);

        String[] players = {"Player 1", "Player2", "Player3", "Player4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_list_item_1, players);

        ListView lv = (ListView)findViewById(R.id.list);
        lv.setAdapter(adapter);


        textView = (TextView) findViewById(R.id.textView);
        editTextSend = (EditText) findViewById(R.id.txtSend);

        btnSend = (Button) findViewById(R.id.btnSend);
        btnUnoUno = (Button) findViewById(R.id.unounobutton);
        btnb3 =
        btng7 = (ImageButton) findViewById(R.id.btn_g);
        btnr8 = (ImageButton) findViewById(R.id.btn_r);
        playCard = (ImageView) findViewById(R.id.img_playCard);


        hostAdress = getIntent().getStringExtra("adress");
        mode = getIntent().getStringExtra("mode");

        networkIOManager = new NetworkIOManager(this);
        networkIOManager.setMode(mode);
        networkIOManager.setHostAdress(hostAdress);
        networkIOManager.open();


        /*btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //networkIOManager.writeMsg(editTextSend.getText().toString());

                //networkIOManager.writeCard(card);


            }
        });*/

        btnUnoUno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //networkIOManager.writeMsg("unouno");

            }
        });

       /* btnr8.setOnClickListener(new View.OnClickListener() {
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

        */
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                final TextView mTextView = (TextView)view;
                switch (position) {
                    case 0:
                        //Toast.makeText(getApplicationContext(), "Player 1", Toast.LENGTH_SHORT).show();
                        //TO DO
                        if(true){
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
                        Toast.makeText(getApplicationContext(), "Player 4", Toast.LENGTH_SHORT).show();
                        //TO DO
                        break;
                    default:
                        // Nothing do!
                }

            }
        });
    }


    @Override
    public void dataChanged() {

        textView.setText(networkIOManager.getTestText());
        setCard(networkIOManager.getTestText());
        sayUno(networkIOManager.getTestText());

        //todo Card holen
        //card = networkIOManager.getCard();


    }

    @Override
    public void NIOReady() {

    }

    public void sayUno(String unoUno) {
        if (unoUno.equals("unouno")) {
            Toast.makeText(getApplicationContext(), "UNOOO", Toast.LENGTH_SHORT).show();
        }
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
