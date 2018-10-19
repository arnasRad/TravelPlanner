package com.gmail.arnasrad.travelplanner.create;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gmail.arnasrad.travelplanner.R;
import com.gmail.arnasrad.travelplanner.data.Location;
import com.gmail.arnasrad.travelplanner.data.Person;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainCreateFragment extends Fragment {
    private Button mainDestinationAddBtn;
    private TextView mainDestinationLbl;
    private Button locationAddBtn;

    private List<Location> listOfLocation;
    private List<Person> listOfPerson;

    private RecyclerView locationRecyclerView;
    private RecyclerView personRecyclerView;

    private LayoutInflater layoutInflater;
    private LocationAdapter locationAdapter;
    private PersonAdapter personAdapter;

    public MainCreateFragment() {
        // Required empty public constructor
    }

    public static MainCreateFragment newInstance() {
        return new MainCreateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_create, container, false);
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

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /*-------------------- Inflate Lists for RecyclerViews ----------------------*/
    public void setListData(List<Location> listOfLocation, List<Person> listOfPerson) {
        this.listOfLocation = listOfLocation;
        this.listOfPerson = listOfPerson;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        locationRecyclerView.setLayoutManager(layoutManager);
        personRecyclerView.setLayoutManager(layoutManager);
        locationAdapter = new LocationAdapter();
        personAdapter = new PersonAdapter();

        locationRecyclerView.setAdapter(locationAdapter);
        personRecyclerView.setAdapter(personAdapter);

        /*
        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                layoutManager.getOrientation()
        );

        itemDecoration.setDrawable(
                ContextCompat.getDrawable(
                        getActivity(),
                        R.drawable.divider_gray
                )
        );

        recyclerView.addItemDecoration(
                itemDecoration
        );
*/
/*
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);*/
    }

    /*-------------------- RecyclerView Boilerplate ----------------------*/

    private class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

        @NonNull
        @Override
        public LocationAdapter.LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = layoutInflater.inflate(R.layout.location, parent, false);
            return new LocationViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull LocationAdapter.LocationViewHolder holder, int position) {
            Location currentItem = listOfLocation.get(position);

            //holder.coloredCircle.setImageResource(currentItem.getColorResource());

            holder.locationMessage.setText(
                    currentItem.getAdministrativeAreaLevel3()
            );

            holder.locationDateAndTime.setText(
                    currentItem.getDate()
            );

            holder.loading.setVisibility(View.INVISIBLE);
        }

        @Override
        public int getItemCount() {
            return listOfLocation.size();
        }

        class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            //private CircleImageView coloredCircle;
            private TextView locationDateAndTime;
            private TextView locationMessage;
            private ViewGroup container;
            private ProgressBar loading;

            LocationViewHolder(View itemView) {
                super(itemView);
                //this.coloredCircle = itemView.findViewById(R.id.imvListItemCircle);
                this.locationDateAndTime = itemView.findViewById(R.id.lblDateAndTime);
                this.locationMessage = itemView.findViewById(R.id.lblMessage);
                this.loading = itemView.findViewById(R.id.proLocation);

                this.container = itemView.findViewById(R.id.rootListItem);

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
            View v = layoutInflater.inflate(R.layout.location, parent, false);
            return new PersonViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull PersonAdapter.PersonViewHolder holder, int position) {
            Person currentItem = listOfPerson.get(position);

            //holder.coloredCircle.setImageResource(currentItem.getColorResource());

            holder.name.setText(
                    currentItem.getName()
            );

            holder.surname.setText(
                    currentItem.getSurname()
            );

            holder.loading.setVisibility(View.INVISIBLE);
        }

        @Override
        public int getItemCount() {
            return listOfPerson.size();
        }

        class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            //private CircleImageView coloredCircle;
            private TextView name;
            private TextView surname;
            private ViewGroup container;
            private ProgressBar loading;

            PersonViewHolder(View itemView) {
                super(itemView);
                //this.coloredCircle = itemView.findViewById(R.id.imvListItemCircle);
                this.name = itemView.findViewById(R.id.lblDateAndTime);
                this.surname = itemView.findViewById(R.id.lblMessage);
                this.loading = itemView.findViewById(R.id.proItemData);

                this.container = itemView.findViewById(R.id.rootListItem);

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
}
