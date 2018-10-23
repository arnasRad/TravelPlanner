package com.gmail.arnasrad.travelplanner.create;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gmail.arnasrad.travelplanner.R;

import static com.gmail.arnasrad.travelplanner.util.BaseActivity.addFragmentToActivity;

public class CreateActivity extends AppCompatActivity {
    //private static final String CREATE_FRAG = "CREATE_FRAG";
    private static final String CREATE_MAIN_FRAG = "CREATE_MAIN_FRAG";
    private static final String GOOGLE_MAP_FRAG = "GOOGLE_MAP_FRAG";
    //private static final String CREATE_MAP_FRAG = "CREATE_MAP_FRAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        startMainCreateFragment();
    }

    /* checks current active fragment on back pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager fragmentManager = getSupportFragmentManager();

        MainCreateFragment myFragment = (MainCreateFragment)fragmentManager
                .findFragmentByTag(CREATE_MAIN_FRAG);
        if (myFragment != null && myFragment.isVisible()) {
            // pop backstack until target fragment is reached
            for (int i = fragmentManager.getBackStackEntryCount() - 1; i > 0; i--) {
                if (!fragmentManager.getBackStackEntryAt(i).getName()
                        .equalsIgnoreCase(CREATE_MAIN_FRAG)) {
                    fragmentManager.popBackStack();
                }
                else
                {
                    break;
                }
            }

        }
    }
    */

    private void startMainCreateFragment() {
        FragmentManager manager = getSupportFragmentManager();

        MainCreateFragment mainCreateFragment = (MainCreateFragment)
                manager.findFragmentByTag(CREATE_MAIN_FRAG);

        if (mainCreateFragment == null) {
            mainCreateFragment = MainCreateFragment.newInstance();
        }

        addFragmentToActivity(manager,
                mainCreateFragment,
                R.id.rootActivityCreate,
                CREATE_MAIN_FRAG
        );
    }

    private void startGoogleMapFragment() {

        FragmentManager manager = getSupportFragmentManager();

        GoogleMapFragment googleMapFragment = (GoogleMapFragment)
                manager.findFragmentByTag(GOOGLE_MAP_FRAG);

        if (googleMapFragment == null) {
            googleMapFragment = GoogleMapFragment.newInstance();
        }

        addFragmentToActivity(manager,
                googleMapFragment,
                R.id.rootActivityCreate,
                GOOGLE_MAP_FRAG
        );
    }
}
