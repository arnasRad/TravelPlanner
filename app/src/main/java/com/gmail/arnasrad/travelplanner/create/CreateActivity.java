package com.gmail.arnasrad.travelplanner.create;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gmail.arnasrad.travelplanner.R;

import static com.gmail.arnasrad.travelplanner.util.BaseActivity.addFragmentToActivity;

public class CreateActivity extends AppCompatActivity {
    private static final String CREATE_MAIN_FRAG = "CREATE_MAIN_FRAG";
    private static final String CREATE_MAP_FRAG = "CREATE_MAP_FRAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);


        FragmentManager manager = getSupportFragmentManager();

        //CreateFragment mapFragment = (CreateFragment) manager.findFragmentByTag(CREATE_MAP_FRAG);
        MainCreateFragment mainCreateFragment = (MainCreateFragment) manager.findFragmentByTag(CREATE_MAIN_FRAG);

        /*if (mapFragment == null) {
            mapFragment = CreateFragment.newInstance();
        }*/

        if (mainCreateFragment == null) {
            mainCreateFragment = MainCreateFragment.newInstance();
        }

        /*addFragmentToActivity(manager,
                mapFragment,
                R.id.rootActivityCreate,
                CREATE_MAP_FRAG
        );*/
        addFragmentToActivity(manager,
                mainCreateFragment,
                R.id.rootActivityCreate,
                CREATE_MAIN_FRAG
        );


    }
}
