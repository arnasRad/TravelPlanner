package com.gmail.arnasrad.travelplanner.detail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.arnasrad.travelplanner.R;
import com.gmail.arnasrad.travelplanner.RoomDemoApplication;
import com.gmail.arnasrad.travelplanner.data.Location;
import com.gmail.arnasrad.travelplanner.data.Person;
import com.gmail.arnasrad.travelplanner.viewmodel.LocationCollectionViewModel;
import com.gmail.arnasrad.travelplanner.viewmodel.PersonCollectionViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class TravelDetail extends Fragment {
    private String travelId;
    private String dueDate;

    private LayoutInflater layoutInflater;

    private List<Location> listOfDestination;
    private List<Location> listOfLocation;
    private List<Person> listOfPerson;

    private DestinationAdapter destinationAdapter;
    private LocationAdapter locationAdapter;
    private PersonAdapter personAdapter;

    private TextView dueDateTextView;
    private RecyclerView mainDestinationRecyclerView;
    private RecyclerView locationRecyclerView;
    private RecyclerView personRecyclerView;

    PersonCollectionViewModel personCollectionViewModel;
    LocationCollectionViewModel locationCollectionViewModel;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public TravelDetail() {
        // Required empty public constructor
    }

    public static TravelDetail newInstance() {
        return new TravelDetail();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((RoomDemoApplication) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_travel_detail, container, false);

        travelId = getArguments().getString("travelId");
        dueDate = getArguments().getString("dueDate");
        layoutInflater = getActivity().getLayoutInflater();


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dueDateTextView = view.findViewById(R.id.detail_due_date);
        mainDestinationRecyclerView = view.findViewById(R.id.detail_main_destination_rw);
        locationRecyclerView = view.findViewById(R.id.detail_locations_rw);
        personRecyclerView = view.findViewById(R.id.detail_people_rw);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dueDateTextView.setText(dueDate);

        //Set up and subscribe (observe) to the ViewModel
        personCollectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(PersonCollectionViewModel.class);
        locationCollectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(LocationCollectionViewModel.class);

        personCollectionViewModel.getPersonList(travelId).observe(this, new Observer<List<Person>>() {
            @Override
            public void onChanged(@Nullable List<Person> people) {
                if (listOfPerson == null) {
                    setPersonData(people);
                }
            }
        });

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
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void setPersonData(List<Person> listOfPerson) {
        this.listOfPerson = listOfPerson;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        personRecyclerView.setLayoutManager(layoutManager);
        personAdapter = new PersonAdapter();
        personRecyclerView.setAdapter(personAdapter);
    }

    public void setLocationData(List<Location> listOfLocation) {
        this.listOfLocation = listOfLocation;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        locationRecyclerView.setLayoutManager(layoutManager);
        locationAdapter = new LocationAdapter();
        locationRecyclerView.setAdapter(locationAdapter);
    }

    public void setDestinationData(List<Location> listOfDestination) {
        this.listOfDestination = listOfDestination;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mainDestinationRecyclerView.setLayoutManager(layoutManager);
        destinationAdapter = new DestinationAdapter();
        mainDestinationRecyclerView.setAdapter(destinationAdapter);

    }

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
