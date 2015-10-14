package com.borisbesemer.contactcard;

import android.graphics.Bitmap;

import java.net.URL;

/**
 * Created by borisbesemer on 09-09-15.
 */
public class Person {
    public String name;
    public int age;
    public String email;
    public String first;
    public String last;
    public String title;
    public String imageUrl;
    public Bitmap bitmap;

    public String getFullname() {
        return capitalize(first) + " " + capitalize(last);
    }

    // capitalize helper function
    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

}
