package com.gmail.arnasrad.travelplanner.create;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.arnasrad.travelplanner.R;
import com.gmail.arnasrad.travelplanner.RoomDemoApplication;
import com.gmail.arnasrad.travelplanner.data.Location;
import com.gmail.arnasrad.travelplanner.data.Person;
import com.gmail.arnasrad.travelplanner.data.Travel;
import com.gmail.arnasrad.travelplanner.list.ListActivity;
import com.gmail.arnasrad.travelplanner.util.ActiveAccSharedPreference;
import com.gmail.arnasrad.travelplanner.util.BaseActivity;
import com.gmail.arnasrad.travelplanner.viewmodel.NewLocationViewModel;
import com.gmail.arnasrad.travelplanner.viewmodel.NewPersonViewModel;
import com.gmail.arnasrad.travelplanner.viewmodel.NewTravelViewModel;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;
import static com.gmail.arnasrad.travelplanner.util.BaseActivity.addStackedFragmentToActivity;


public class MainCreateFragment extends Fragment implements
        AddPersonFragment.AddPersonDialogListener,
        DatePickerDialog.OnDateSetListener {
    private static final String GOOGLE_MAP_FRAG = "GOOGLE_MAP_FRAG";
    private static final String ADD_PERSON_FRAG = "ADD_PERSON_FRAG";

    PlacePicker.IntentBuilder builder;
    private int DESTINATION_PICKER_REQUEST = 1;
    private int LOCATION_PICKER_REQUEST = 2;
    private int ADD_PERSON_REQUEST = 3;

    DestinationAdapter destinationAdapter;
    LocationAdapter locationAdapter;
    PersonAdapter personAdapter;

    private List<Location> listOfDestination;
    private List<Location> listOfLocation;
    private List<Person> listOfPerson;

    private TextView dueDateTextView;
    private RecyclerView mainDestinationRecyclerView;
    private RecyclerView locationRecyclerView;
    private RecyclerView personRecyclerView;

    String currentDate;
    String dateFormatString;
    String altDateFormatString;
    Calendar dueDate;

    Button dueDateAddBtn;
    Button mainDestinationAddBtn;
    Button locationsAddBtn;
    Button peopleAddBtn;

    private LayoutInflater layoutInflater;

    NewLocationViewModel newLocationViewModel;
    NewPersonViewModel newPersonViewModel;
    NewTravelViewModel newTravelViewModel;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public MainCreateFragment() {
        // Required empty public constructor
    }

    public static MainCreateFragment newInstance() {
        return new MainCreateFragment();
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
        View v = inflater.inflate(R.layout.fragment_main_create, container, false);
        layoutInflater = getActivity().getLayoutInflater();
        initDate();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        dueDateAddBtn = v.findViewById(R.id.button_set_due_date);
        mainDestinationAddBtn = v.findViewById(R.id.button_add_main_destination);
        locationsAddBtn = v.findViewById(R.id.button_add_location);
        peopleAddBtn = v.findViewById(R.id.button_add_person);

        dueDateTextView = v.findViewById(R.id.due_date_lbl);
        mainDestinationRecyclerView = v.findViewById(R.id.detail_main_destination_rw);
        locationRecyclerView = v.findViewById(R.id.detail_locations_rw);
        personRecyclerView = v.findViewById(R.id.create_people_rw);

        initListData();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dueDate.set(Calendar.YEAR, year);
                dueDate.set(Calendar.MONTH, month);
                dueDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDueDateLabel();
            }
        };

        dueDateAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date,
                        dueDate.get(Calendar.YEAR),
                        dueDate.get(Calendar.MONTH),
                        dueDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mainDestinationAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new PlacePicker.IntentBuilder();
                executeMapPicker(DESTINATION_PICKER_REQUEST);
            }
        });

        locationsAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new PlacePicker.IntentBuilder();

                executeMapPicker(LOCATION_PICKER_REQUEST);
            }
        });

        peopleAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddPersonFragment();
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Set up and subscribe (observe) to the ViewModel
        newLocationViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(NewLocationViewModel.class);
        newPersonViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(NewPersonViewModel.class);
        newTravelViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(NewTravelViewModel.class);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DESTINATION_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Context context = getContext();
                Place place = PlacePicker.getPlace(context, data);

                if (destinationAdapter.getItemCount() == 0) {
                    listOfDestination.add(new Location(currentDate, place.getViewport(), place, true));
                    destinationAdapter.notifyItemInserted(listOfDestination.size());
                    mainDestinationAddBtn.setText(getResources().getString(R.string.create_edit_btn));
                } else {
                    listOfDestination.set(0, new Location(currentDate, place.getViewport(), place, true));
                    destinationAdapter.notifyItemChanged(0);
                }

                String toastMsg = String.format(getString(R.string.main_destination_set_successfully), place.getName());
                Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == LOCATION_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Context context = getContext();
                Place place = PlacePicker.getPlace(context, data);

                listOfLocation.add(new Location(currentDate, place.getViewport(), place, false));
                locationAdapter.notifyItemInserted(listOfLocation.size());

                String toastMsg = String.format(getString(R.string.location_added_successfully), place.getName());
                Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }


    /*------------------------------- Menu -------------------------------*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.done_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done:
                if (listOfDestination.size() != 0) {
                    Location tempMainDestination = listOfDestination.get(0);
                    newLocationViewModel.addNewLocationToDatabase(tempMainDestination);

                    for (Location location : listOfLocation) {
                        newLocationViewModel.addNewLocationToDatabase(location);
                    }

                    for (Person person : listOfPerson) {
                        newPersonViewModel.addNewPersonToDatabase(person);
                    }

                    newTravelViewModel.addNewTravelToDatabase(new Travel(currentDate,
                            ActiveAccSharedPreference.getActiveUserPreference(getContext()),
                            tempMainDestination.getPlaceName(), getDate(dateFormatString, dueDate),
                            getRandomDrawableResource()));

                    Toast.makeText(getContext(), getString(R.string.travel_created_successfully_message), Toast.LENGTH_LONG).show();

                    startListActivity();
                    return true;
                } else {
                    Toast.makeText(getContext(), getString(R.string.no_main_destination_error), Toast.LENGTH_LONG).show();

                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /*-------------------- Inflate Lists for RecyclerViews ----------------------*/
    public void initListData() {
        listOfDestination = new ArrayList<>();
        listOfLocation = new ArrayList<>();
        listOfPerson = new ArrayList<>();

        mainDestinationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        locationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        personRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        destinationAdapter = new DestinationAdapter();
        locationAdapter = new LocationAdapter();
        personAdapter = new PersonAdapter();

        mainDestinationRecyclerView.setAdapter(destinationAdapter);
        locationRecyclerView.setAdapter(locationAdapter);
        personRecyclerView.setAdapter(personAdapter);

        ItemTouchHelper destinationTouchHelper = new ItemTouchHelper(createDestinationHelperCallback());
        destinationTouchHelper.attachToRecyclerView(mainDestinationRecyclerView);
        ItemTouchHelper locationTouchHelper = new ItemTouchHelper(createLocationHelperCallback());
        locationTouchHelper.attachToRecyclerView(locationRecyclerView);
        ItemTouchHelper personTouchHelper = new ItemTouchHelper(createPersonHelperCallback());
        personTouchHelper.attachToRecyclerView(personRecyclerView);
    }

    private void initDate() {
        dueDate = Calendar.getInstance();
        dateFormatString = getString(R.string.date_format_string);
        altDateFormatString = getString(R.string.alternate_date_format_string);
        currentDate = getDate(altDateFormatString, dueDate);
    }

    private void executeMapPicker(int request) {
        try {
            startActivityForResult(builder.build(getActivity()), request);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void updateDueDateLabel() {
        dueDateTextView.setText(getDate(dateFormatString, dueDate));
    }

    public int getRandomDrawableResource (){
        int min = 0;
        int max = 4;

        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        switch (randomNum){
            case 0:
                return R.drawable.red_drawable;
            case 1:
                return R.drawable.blue_drawable;
            case 2:
                return R.drawable.green_drawable;
            case 3:
                return R.drawable.yellow_drawable;
            default:
                return 0;
        }
    }


    private void startGoogleMapFragment() {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        GoogleMapFragment mapFragment = (GoogleMapFragment) manager.findFragmentByTag(GOOGLE_MAP_FRAG);

        if (mapFragment == null) {
            mapFragment = GoogleMapFragment.newInstance();
        }

        addStackedFragmentToActivity(manager,
                mapFragment,
                R.id.rootActivityCreate,
                GOOGLE_MAP_FRAG
        );
    }

    private void startAddPersonFragment() {
/*
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        AddPersonFragment addPersonFragment= (AddPersonFragment)
                fragmentManager.findFragmentByTag(ADD_PERSON_FRAG);

        if (addPersonFragment == null) {
            addPersonFragment = AddPersonFragment.newInstance();
        }

        addPersonFragment.setTargetFragment(MainCreateFragment.this, ADD_PERSON_REQUEST);

        BaseActivity.showStackedFragmentToActivity(fragmentManager,
                addPersonFragment,
                R.id.rootActivityCreate,
                ADD_PERSON_FRAG
        );
*/


        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        AddPersonFragment addPersonFragment = AddPersonFragment.newInstance();
        addPersonFragment.setTargetFragment(MainCreateFragment.this, ADD_PERSON_REQUEST);
        addPersonFragment.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Material_Dialog);
        addPersonFragment.show(fragmentManager, "Add Person");

    }

    private void startListActivity() {
        startActivity(new Intent(getActivity(), ListActivity.class));
    }

    @Override
    public void onFinishAddDialog(String name, String surname, String email) {
        listOfPerson.add(new Person(currentDate, name, surname, email));
        personAdapter.notifyItemInserted(listOfPerson.size());

        String toastMsg = String.format(getString(R.string.person_added_successfully), name, surname);
        Toast.makeText(getContext(), toastMsg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
/*
    @Override
    public void onComplete(String name, String surname, String email) {
        this.listOfPerson.add(new Person(name, surname, email));
        this.personAdapter.notifyItemInserted(listOfPerson.size());
    }
*/
    /*-------------------- RecyclerView Boilerplate ----------------------*/


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
                /*
                Location listItem = listOfLocation.get(
                        this.getAdapterPosition()
                );

                startDetailActivity(listItem.getItemId(), v);
                */
            }
        }

    }

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
                /*
                Location listItem = listOfLocation.get(
                        this.getAdapterPosition()
                );

                startDetailActivity(listItem.getItemId(), v);
                */
            }
        }

    }

    private class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {
        @NonNull
        @Override
        public PersonAdapter.PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = layoutInflater.inflate(R.layout.person, parent, false);
            return new PersonViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull PersonAdapter.PersonViewHolder holder, int position) {
            Person currentItem = listOfPerson.get(position);

            //holder.coloredCircle.setImageResource(currentItem.getColorResource());

            holder.name.setText(getString(R.string.person_name_surname, currentItem.getName(), currentItem.getSurname()));
            holder.email.setText(currentItem.getEmail());
        }

        @Override
        public int getItemCount() {
            return listOfPerson.size();
        }

        class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            //private CircleImageView coloredCircle;
            private TextView name;
            private TextView email;
            private ViewGroup container;

            PersonViewHolder(View itemView) {
                super(itemView);
                //this.coloredCircle = itemView.findViewById(R.id.imvListItemCircle);
                this.name = itemView.findViewById(R.id.person_name_lbl);
                this.email = itemView.findViewById(R.id.person_email_lbl);

                this.container = itemView.findViewById(R.id.root_person_item);
                this.container.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                /*
                Person listItem = listOfPerson.get(
                        this.getAdapterPosition()
                );

                startDetailActivity(listItem.getItemId(), v);
                */
            }
        }

    }

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

                //ensure View is consistent with underlying data
                listOfLocation.remove(position);
                locationAdapter.notifyItemRemoved(position);


            }
        };
    }
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

    public String getDate(String format, Calendar date) {
        Date currentDate = date.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        return dateFormat.format(currentDate);
    }
}
