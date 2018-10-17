package com.gmail.arnasrad.travelplanner.list;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.gmail.arnasrad.travelplanner.R;
import com.gmail.arnasrad.travelplanner.util.BaseActivity;

public class ListActivity extends BaseActivity {
    private static final String LIST_FRAG = "LIST_FRAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        FragmentManager manager = getSupportFragmentManager();

        ListFragment fragment = (ListFragment) manager.findFragmentByTag(LIST_FRAG);

        if (fragment == null) {
            fragment = ListFragment.newInstance();
        }

        addFragmentToActivity(manager,
                fragment,
                R.id.rootActivityList,
                LIST_FRAG
        );
    }
}
