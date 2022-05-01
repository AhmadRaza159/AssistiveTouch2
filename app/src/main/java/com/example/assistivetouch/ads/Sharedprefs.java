package com.example.assistivetouch.ads;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Sharedprefs extends Activity {
    boolean check_value;
    private SharedPreferences sharedPreferences;
    Context context;
    private int check_permission;

    public Sharedprefs(Context context) {
        this.context = context;
    }

    public void SavePreferences(boolean check) {
        sharedPreferences = context.getSharedPreferences("MyPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("NoAd", check);
        editor.apply();
    }

    public boolean showPreferences() {
        sharedPreferences = context.getSharedPreferences("MyPrefs",MODE_PRIVATE);
        check_value = sharedPreferences.getBoolean("NoAd", false);
        return check_value;
    }
    public void SavePreferences_permission(int check) {
        sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("CHECK_NUM", check);
        editor.apply();
    }

    public int showPreferences_permission() {
        sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        check_permission = sharedPreferences.getInt("CHECK_NUM", 0);
        return check_permission;
    }
}
