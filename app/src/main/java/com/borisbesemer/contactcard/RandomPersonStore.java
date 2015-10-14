package com.borisbesemer.contactcard;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by borisbesemer on 13-10-15.
 */
public class RandomPersonStore {

    private static RandomPersonStore mInstance = null;

    public ArrayList<Person> persons = new ArrayList<Person>();

    public static RandomPersonStore getInstance() {
        if(mInstance == null) {
            mInstance = new RandomPersonStore();
        }
        return mInstance;
    }
}
