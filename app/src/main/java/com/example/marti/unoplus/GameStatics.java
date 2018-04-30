package com.example.marti.unoplus;

import com.example.marti.unoplus.Net.UnoPlusNetwork;

/**
 * Created by ekzhu on 30.04.2018.
 */

public class GameStatics {

    public static boolean devMode = false; // for tests

    //AppCompatActivity currentActivity;

    //Forbid instantiation, is a static class
    private GameStatics()
    {}

    public static void Initialize(boolean isServer)
    {
        GameStatics.net = new UnoPlusNetwork(isServer);
    }

    public static UnoPlusNetwork net = null;
}
