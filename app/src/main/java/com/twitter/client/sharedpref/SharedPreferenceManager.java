package com.twitter.client.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.twitter.client.Program;


/**
 * @author MohamedSaleh on 4/22/2017.
 */

public class SharedPreferenceManager {

    private static SharedPreferenceManager sharedPreferenceManager;
    private static final String MY_PREF = "FamTreePreference";
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private SharedPreferenceManager() {
        sharedPreferences = Program.getContext().getSharedPreferences(MY_PREF,
                Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SharedPreferenceManager getInstance() {
        if (sharedPreferenceManager == null) {
            sharedPreferenceManager = new SharedPreferenceManager();
        }
        return sharedPreferenceManager;
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key, @Nullable String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void cleanData() {
        editor.clear().commit();
    }

}
