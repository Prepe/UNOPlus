package com.example.marti.unoplus;

import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;



import java.util.Random;

import jop.hab.net.NetworkIOManager;

/**
 * Created by ekzhu on 30.04.2018.
 */

public class GameStatics {

    public static AppCompatActivity currentActivity = null;

    public static boolean devMode = false; // for tests

    public static WifiManager wifiManager;
    public static NetworkIOManager NIOManager;

    //Cards
    public static boolean duel = true;
    public static boolean hotDrop = true;
    public static boolean cardSpin = true;

    //Cheats
    public static boolean dropCard = true;
    public static boolean tradeCard = true;

    //Other
    public static boolean cunterPlay = true;
    public static boolean quickPlay = true;

    //AppCompatActivity currentActivity;

    //Forbid instantiation, is a static class
    private GameStatics() {
    }

    public static void Initialize(boolean isServer) {
        GameStatics.random = new Random();
    }

    public static Random random = null;

    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = GameStatics.random.nextInt(clazz.getEnumConstants().length-2);
        return clazz.getEnumConstants()[x];
    }
}
