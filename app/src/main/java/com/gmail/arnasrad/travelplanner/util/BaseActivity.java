package com.gmail.arnasrad.travelplanner.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    public static void addFragmentToActivity(FragmentManager fragmentManager,
                                             Fragment fragment,
                                             int frameId,
                                             String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment, tag);
        transaction.commit();
    }
    public static void addStackedFragmentToActivity(FragmentManager fragmentManager,
                                                    Fragment fragment,
                                                    int frameId,
                                                    String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public static void showStackedFragmentToActivity(FragmentManager fragmentManager,
                                                    Fragment fragment,
                                                    int frameId,
                                                    String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment, tag);
        transaction.addToBackStack(null);
        transaction.show(fragment);
    }
}
