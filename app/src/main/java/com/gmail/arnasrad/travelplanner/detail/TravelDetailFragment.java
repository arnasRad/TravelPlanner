package com.gmail.arnasrad.travelplanner.detail;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.gmail.arnasrad.travelplanner.R;
import com.gmail.arnasrad.travelplanner.RoomDemoApplication;
import com.gmail.arnasrad.travelplanner.data.Location;
import com.gmail.arnasrad.travelplanner.data.Person;
import com.gmail.arnasrad.travelplanner.util.BaseActivity;
import com.gmail.arnasrad.travelplanner.util.DirectionsJSONParser;
import com.gmail.arnasrad.travelplanner.util.PermissionUtils;
import com.gmail.arnasrad.travelplanner.viewmodel.LocationCollectionViewModel;
import com.gmail.arnasrad.travelplanner.viewmodel.PersonCollectionViewModel;
import com.gmail.arnasrad.travelplanner.viewmodel.TravelCollectionViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import static android.support.constraint.Constraints.TAG;

public class TravelDetailFragment extends Fragment
        implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMarkerClickListener {
    private static final String PEOPLE_FRAG = "PEOPLE_FRAG";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    static final int DEFAULT_ZOOM = 9;
    static final int FARTHER_ZOOM = 15;

    private String travelId;
    private String dueDate;

    private LayoutInflater layoutInflater;

    private List<Location> listOfDestination;
    private List<Location> listOfLocation;

    private LocationAdapter locationAdapter;

    //private TextView dueDateTextView;
    //private RecyclerView mainDestinationRecyclerView;
    private RecyclerView locationRecyclerView;

    LocationCollectionViewModel locationCollectionViewModel;
    PersonCollectionViewModel personCollectionViewModel;
    TravelCollectionViewModel travelCollectionViewModel;

    private boolean mPermissionDenied = false;
    private GoogleMap mGoogleMap;
    private MapView mMapView;
    ArrayList<Marker> markerPoints = new ArrayList();
    PolylineOptions polylineOptions;
    Polyline polyline;

    private View mView;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public TravelDetailFragment() {
        // Required empty public constructor
    }

    public static TravelDetailFragment newInstance() {
        return new TravelDetailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        ((RoomDemoApplication) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mView == null)
            mView = inflater.inflate(R.layout.fragment_travel_detail, container, false);

        travelId = getArguments().getString("travelId");
        dueDate = getArguments().getString("dueDate");
        layoutInflater = getActivity().getLayoutInflater();

        FloatingActionButton floatingActionButton = mView.findViewById(R.id.fab_detail_show_people);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPeopleDetailFragment(travelId);
            }
        });

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = view.findViewById(R.id.detail_map_view);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);

        //dueDateTextView = view.findViewById(R.id.detail_due_date);
        //mainDestinationRecyclerView = view.findViewById(R.id.detail_main_destination_rw);
        locationRecyclerView = view.findViewById(R.id.detail_locations_list_rw);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //dueDateTextView.setText(dueDate);

        //Set up and subscribe (observe) to the ViewModel
        locationCollectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(LocationCollectionViewModel.class);
        personCollectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(PersonCollectionViewModel.class);
        travelCollectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(TravelCollectionViewModel.class);

        locationCollectionViewModel.getLocationList(travelId).observe(this, new Observer<List<Location>>() {
            @Override
            public void onChanged(@Nullable List<Location> locations) {
                if (listOfLocation == null) {
                    setLocationData(locations);
                }
            }
        });

        locationCollectionViewModel.getDestinationList(travelId).observe(this, new Observer<List<Location>>() {
            @Override
            public void onChanged(@Nullable List<Location> locations) {
                if (listOfDestination == null) {
                    setDestinationData(locations);
                }
            }
        });


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_mark_destination:
                Location destination = listOfDestination.get(0);
                LatLng destLatLng = new LatLng(destination.getLatitude(), destination.getLongitude());
                moveCamera(destLatLng, DEFAULT_ZOOM);
                return super.onOptionsItemSelected(item);
            case R.id.menu_delete_detail:
                locationCollectionViewModel.deleteLocationsByTravelId(travelId);
                personCollectionViewModel.deletePersonListByTravelId(travelId);
                travelCollectionViewModel.deleteTravelById(travelId);

                listOfDestination.clear();
                listOfLocation.clear();

                getActivity().getSupportFragmentManager().popBackStack();
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void startPeopleDetailFragment(String travelId) {
        Bundle bundle = new Bundle();
        bundle.putString("travelId", travelId);

        FragmentManager manager = getActivity().getSupportFragmentManager();
        PeopleDetailFragment peopleDetailFragment = (PeopleDetailFragment) manager.findFragmentByTag(PEOPLE_FRAG);


        if (peopleDetailFragment == null) {
            peopleDetailFragment = PeopleDetailFragment.newInstance();
        }
        peopleDetailFragment.setArguments(bundle);

        BaseActivity.addStackedFragmentToActivity(manager,
                peopleDetailFragment,
                R.id.rootActivityList,
                PEOPLE_FRAG
        );
    }


    /*------------------------------- Map -------------------------------*/
    @Override
    public void onResume() {
        super.onResume();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mGoogleMap = googleMap;

        enableMyLocation();
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //showRouteBetweenTwoPoints(getCurrentLocation(), latLng);

                getDurationBetweenToPoints(getCurrentLocation(), latLng);
                //Toast.makeText(getContext(), String.valueOf(duration), Toast.LENGTH_LONG);

                /*
                // shows directions between origin and dest markers
                LatLng origin = getCurrentLocation();
                LatLng dest = latLng;
                //LatLng dest = new LatLng(listOfLocation.get(0).getLatitude(), listOfLocation.get(0).getLongitude());

                // Getting URL to the Google Directions API
                String url = getDirectionsUrl(origin, dest);

                DownloadTask downloadTask = new DownloadTask();

                // Start downloading json data from Google Directions API
                downloadTask.execute(url);
                */
            }
        });
    }
    //  TODO: finish time ordered location list for complete routes
/*
    private List<Location> getTimeOrderedLocationList(List<Location> locList) {
        List<Location> orderedLocationList = new ArrayList<Location>();
        List<Location> tempLocationList = new ArrayList<Location>(locList);

        LatLng currentLocationPosition = getCurrentLocation();
        int position = 0;
        int i = 0;
        LatLng tempLatLng;
        double minDuration = 0;
        double duration;
//http://www.akexorcist.com/2015/12/google-direction-library-for-android-en.html
        for (Location location: tempLocationList) {
            tempLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            duration = getDurationBetweenToPoints(currentLocationPosition, tempLatLng);
            if (minDuration != 0 && minDuration > duration) {
                minDuration = duration;
                position = i;
            }
            ++i;
        }
        orderedLocationList.add(locList.get(position));
        tempLocationList.remove(position);



        return orderedLocationList;
    }
    */

    private double getDurationBetweenToPoints(LatLng startPoint, LatLng endPoint) {
        final Info[] info = new Info[1];
        GoogleDirection.withServerKey(getString(R.string.google_maps_key))
                .from(startPoint)
                .to(endPoint)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (polyline != null)
                            polyline.remove();
                        Route route = direction.getRouteList().get(0);
                        Leg leg = route.getLegList().get(0);
                        info[0] = leg.getDuration();
                        double duration = Double.parseDouble(info[0].getValue());
                        Toast.makeText(getContext(), String.valueOf(duration), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        System.out.println("EROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOR");
                        Log.d(TAG, "EROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOR");
                        info[0] = null;
                    }
                });
        return 0;
    }

    private void showRouteBetweenTwoPoints(LatLng startPoint, LatLng endPoint) {
        GoogleDirection.withServerKey(getString(R.string.google_maps_key))
                .from(startPoint)
                .to(endPoint)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (polyline != null)
                            polyline.remove();
                        Route route = direction.getRouteList().get(0);
                        Leg leg = route.getLegList().get(0);
                        ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                        int colorValue = ResourcesCompat.getColor(getResources(), R.color.colorAccent, null);
                        polylineOptions = DirectionConverter
                                .createPolyline(getContext(), directionPositionList,
                                            5, colorValue);
                        polyline = mGoogleMap.addPolyline(polylineOptions);
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {

                    }
                });
    }


    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getActivity().getSupportFragmentManager(), "dialog");
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        if (markerPoints.size() > 0) {
            markerPoints.get(0).remove();
            markerPoints.clear();
        }
        markerPoints.add(mGoogleMap.addMarker(new MarkerOptions().position(latLng)));
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom), 1500, null);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    public double getDistanceBetweenLocations(LatLng startLoc, LatLng destLoc) {
        float distance[] = new float[10];

        android.location.Location.distanceBetween(startLoc.latitude, startLoc.longitude,
                destLoc.latitude, destLoc.longitude, distance);

        return distance[0];
    }

    public LatLng getCurrentLocation() {
        LocationManager locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();


        if (mPermissionDenied) {
            Context context = getContext();
            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            }
        }

        android.location.Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        return new LatLng(latitude, longitude);
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(((AppCompatActivity) getActivity()), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mGoogleMap != null) {
            // Access to the location has been granted to the app.
            mGoogleMap.setMyLocationEnabled(true);
            setLocationButtonBottomPosition(48, 48);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    public void setLocationButtonBottomPosition(int marginRight, int marginBottom) {
        View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        //rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        //rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        //rlp.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        //rlp.setMargins(0,0,marginRight,marginBottom);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getActivity(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull android.location.Location location) {
        Toast.makeText(getActivity(), "Current location:\n" + location, Toast.LENGTH_LONG).show();

        /*
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        moveCamera(latLng, DEFAULT_ZOOM);
        */
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setLocationData(List<Location> listOfLocation) {
        this.listOfLocation = listOfLocation;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        locationRecyclerView.setLayoutManager(layoutManager);
        locationAdapter = new LocationAdapter();
        locationRecyclerView.setAdapter(locationAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createLocationHelperCallback());
        itemTouchHelper.attachToRecyclerView(locationRecyclerView);
    }

    public void setDestinationData(List<Location> listOfDestination) {
        this.listOfDestination = listOfDestination;

        /*
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mainDestinationRecyclerView.setLayoutManager(layoutManager);
        destinationAdapter = new DestinationAdapter();
        mainDestinationRecyclerView.setAdapter(destinationAdapter);
        */
    }

    /*
    private class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder> {
        @NonNull
        @Override
        public DestinationAdapter.DestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = layoutInflater.inflate(R.layout.location_create, parent, false);
            return new DestinationViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull DestinationAdapter.DestinationViewHolder holder, int position) {
            Location currentItem = listOfDestination.get(position);

            holder.destinationName.setText(currentItem.getPlaceName());
            holder.destinationAddress.setText(currentItem.getPlaceAddress());
        }

        @Override
        public int getItemCount() {
            return listOfDestination.size();
        }

        class DestinationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            //private CircleImageView coloredCircle;
            private TextView destinationName;
            private TextView destinationAddress;
            private ViewGroup container;

            DestinationViewHolder(View itemView) {
                super(itemView);
                //this.coloredCircle = itemView.findViewById(R.id.imvListItemCircle);
                this.destinationName =  itemView.findViewById(R.id.location_name_lbl);
                this.destinationAddress = itemView.findViewById(R.id.location_address_lbl);

                this.container = itemView.findViewById(R.id.root_location_create_item);
                this.container.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

            }
        }

    }
    */

    private class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
        @NonNull
        @Override
        public LocationAdapter.LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = layoutInflater.inflate(R.layout.location_create, parent, false);
            return new LocationViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull LocationAdapter.LocationViewHolder holder, int position) {
            Location currentItem = listOfLocation.get(position);

            holder.locationName.setText(currentItem.getPlaceName());
            holder.locationAddress.setText(currentItem.getPlaceAddress());
        }

        @Override
        public int getItemCount() {
            return listOfLocation.size();
        }

        class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            //private CircleImageView coloredCircle;
            private TextView locationName;
            private TextView locationAddress;
            private ViewGroup container;

            LocationViewHolder(View itemView) {
                super(itemView);
                //this.coloredCircle = itemView.findViewById(R.id.imvListItemCircle);
                this.locationName =  itemView.findViewById(R.id.location_name_lbl);
                this.locationAddress = itemView.findViewById(R.id.location_address_lbl);

                this.container = itemView.findViewById(R.id.root_location_create_item);
                this.container.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Location location = listOfLocation.get(
                        this.getAdapterPosition()
                );

                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                moveCamera(latLng, DEFAULT_ZOOM);
            }
        }

    }
    /*
    private ItemTouchHelper.Callback createDestinationHelperCallback() {
        return new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {


            //not used, as the first parameter above is 0
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView1, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();

                //ensure View is consistent with underlying data
                listOfDestination.remove(position);
                destinationAdapter.notifyItemRemoved(position);
            }
        };
    }
    */
    private ItemTouchHelper.Callback createLocationHelperCallback() {
        return new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {


            //not used, as the first parameter above is 0
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView1, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();

                locationCollectionViewModel.deleteLocation(listOfLocation.get(position));

                //ensure View is consistent with underlying data
                listOfLocation.remove(position);
                locationAdapter.notifyItemRemoved(position);
            }
        };
    }
    /*
    private ItemTouchHelper.Callback createPersonHelperCallback() {
        return new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            //not used, as the first parameter above is 0
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView1, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();

                //ensure View is consistent with underlying data
                listOfPerson.remove(position);
                personAdapter.notifyItemRemoved(position);
            }
        };
    }
    */

    public String getDate(String format, Calendar date) {
        Date currentDate = date.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        return dateFormat.format(currentDate);
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap point = path.get(j);

                    double lat = Double.parseDouble((String) point.get("lat"));
                    double lng = Double.parseDouble((String) point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }
            // Drawing polyline in the Google Map for the i-th route
            mGoogleMap.addPolyline(lineOptions);
        }
    }


    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

}