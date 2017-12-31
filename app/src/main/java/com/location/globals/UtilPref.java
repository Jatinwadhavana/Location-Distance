package com.location.globals;

import android.content.Context;
import android.content.SharedPreferences;

public class UtilPref {
    private static String TEMP = "utilPref";
    private static String CURRENT_LOC = "current_loc";
    private static String START_LOC = "start_loc";
    private static String DIST = "DIST";
    private static String SESSION = "session";
    private static String HISTORY = "hist";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(context.getPackageName(), 0);
    }

    public static String getTemp(Context context) {
        return getPrefs(context).getString(TEMP, "");
    }

    public static void setTemp(Context context, String value) {
        getPrefs(context).edit().putString(TEMP, value).apply();
    }
    public static String getCurrentLoc(Context context) {
        return getPrefs(context).getString(CURRENT_LOC, "");
    }

    public static void setCurrentLoc(Context context, String value) {
        getPrefs(context).edit().putString(CURRENT_LOC, value).apply();
    }
    public static String getStartLoc(Context context) {
        return getPrefs(context).getString(START_LOC, "");
    }

    public static void setStartLoc(Context context, String value) {
        getPrefs(context).edit().putString(START_LOC, value).apply();
    }
    public static String getDIST(Context context) {
        return getPrefs(context).getString(DIST, "0");
    }

    public static void setDIST(Context context, String value) {
        getPrefs(context).edit().putString(DIST, value).apply();
    }
    public static boolean getSession(Context context) {
        return getPrefs(context).getBoolean(SESSION, false);
    }

    public static void setSession(Context context, boolean value) {
        getPrefs(context).edit().putBoolean(SESSION, value).apply();
    }
    public static String getHistory(Context context) {
        return getPrefs(context).getString(HISTORY, "[]");
    }

    public static void setHistory(Context context, String value) {
        getPrefs(context).edit().putString(HISTORY, value).apply();
    }
}
