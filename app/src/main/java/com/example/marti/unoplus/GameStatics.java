package com.example.marti.unoplus;

import android.support.v7.app.AppCompatActivity;



import java.util.Random;

/**
 * Created by ekzhu on 30.04.2018.
 */

public class GameStatics {

    public static AppCompatActivity currentActivity = null;

    public static boolean devMode = false; // for tests

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
