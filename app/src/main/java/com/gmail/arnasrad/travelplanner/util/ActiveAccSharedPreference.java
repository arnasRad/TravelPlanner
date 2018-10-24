package com.gmail.arnasrad.travelplanner.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.BaseAdapter;

import com.gmail.arnasrad.travelplanner.MainEmptyActivity;
import com.gmail.arnasrad.travelplanner.R;
import com.gmail.arnasrad.travelplanner.RoomDemoApplication;

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;

public class ActiveAccSharedPreference {


    public static String getActiveUserPreference(Context context) {
        Context appContext = context.getApplicationContext();
        String sharedPreferenceName = appContext.getString(R.string.active_user_preference_name);
        String sharedPreferenceKey = appContext.getString(R.string.active_user_key);
        SharedPreferences sharedPref = appContext.getSharedPreferences(sharedPreferenceName, MODE_PRIVATE);
        if (sharedPref != null)
            return sharedPref.getString(sharedPreferenceKey, null);
        return null;
    }

    public static void setActiveUserPreference(Context context, String username) {
        Context appContext = context.getApplicationContext();
        String sharedPreferenceName = appContext.getString(R.string.active_user_preference_name);
        String sharedPreferenceKey = appContext.getString(R.string.active_user_key);

        SharedPreferences.Editor editor = appContext.getSharedPreferences(sharedPreferenceName, MODE_PRIVATE).edit();
        if (editor != null) {
            editor.putString(sharedPreferenceKey, username);
            editor.apply();
        }
    }

    public static void removeActiveUserPreference(Context context) {
        Context appContext = context.getApplicationContext();
        String sharedPreferenceName = appContext.getString(R.string.active_user_preference_name);
        SharedPreferences.Editor editor = appContext.getSharedPreferences(sharedPreferenceName, MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }
}
