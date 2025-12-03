package com.example.hungryneat;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Locale;
import java.util.Random;

public class Utils {
    private static final String PREFS = "app_prefs";
    private static final String KEY_FIRST_RUN = "first_run";

    public static boolean isFirstRun(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_FIRST_RUN, true);
    }

    public static void setFirstRunDone(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_FIRST_RUN, false).apply();
    }

    public static String generateSixDigitCode() {
        Random r = new Random();
        int num = 100000 + r.nextInt(900000);
        return String.format(Locale.US, "%06d", num);
    }
}