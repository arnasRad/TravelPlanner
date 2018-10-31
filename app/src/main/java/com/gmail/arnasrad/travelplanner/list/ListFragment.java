package com.gmail.arnasrad.travelplanner.list;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gmail.arnasrad.travelplanner.R;
import com.gmail.arnasrad.travelplanner.RoomDemoApplication;
import com.gmail.arnasrad.travelplanner.create.CreateActivity;
import com.gmail.arnasrad.travelplanner.data.ListItem;
import com.gmail.arnasrad.travelplanner.data.Travel;
import com.gmail.arnasrad.travelplanner.detail.TravelDetailFragment;
import com.gmail.arnasrad.travelplanner.login.LoginActivity;
import com.gmail.arnasrad.travelplanner.util.ActiveAccSharedPreference;
import com.gmail.arnasrad.travelplanner.util.BaseActivity;
import com.gmail.arnasrad.travelplanner.viewmodel.ListItemCollectionViewModel;
import com.gmail.arnasrad.travelplanner.viewmodel.LocationCollectionViewModel;
import com.gmail.arnasrad.travelplanner.viewmodel.PersonCollectionViewModel;
import com.gmail.arnasrad.travelplanner.viewmodel.TravelCollectionViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;



public class ListFragment extends Fragment {
    private static final String DETAIL_FRAG = "DETAIL_FRAG";

    private static final int PAST_TRAVEL = 50000;
    private static final int CURRENT_TRAVEL = 50001;
    private static final int DUE_TRAVEL = 50002;

    private View mView;
    private List<Travel> listOfTravel;

    String currentDate;

    private String currentUsername;
    private LayoutInflater layoutInflater;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    LocationCollectionViewModel locationCollectionViewModel;
    PersonCollectionViewModel personCollectionViewModel;
    TravelCollectionViewModel travelCollectionViewModel;

    public ListFragment() {
    }

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    /*------------------------------- Lifecycle -------------------------------*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentDate = getCurrentDate(getString(R.string.date_format_string));

        setHasOptionsMenu(true);
        currentUsername = ActiveAccSharedPreference.getActiveUserPreference(getContext());


        ((RoomDemoApplication) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        locationCollectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(LocationCollectionViewModel.class);
        personCollectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(PersonCollectionViewModel.class);

        travelCollectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(TravelCollectionViewModel.class);
        travelCollectionViewModel.getTravels(currentUsername).observe(this, new Observer<List<Travel>>() {
            @Override
            public void onChanged(@Nullable List<Travel> travels) {
                if (listOfTravel == null) {
                    setTravelData(travels);
                } else if (travels.size() != listOfTravel.size()) {
                    setTravelData(travels);
                }
            }
        });
    }

    /*------------------------------- Menu -------------------------------*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.sign_out_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                ActiveAccSharedPreference.removeActiveUserPreference(getContext());
                startLoginActivity();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null)
            mView = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = mView.findViewById(R.id.recListActivity);
        layoutInflater = getActivity().getLayoutInflater();

        FloatingActionButton fabulous = mView.findViewById(R.id.fabCreateNewItem);

        fabulous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCreateActivity();
            }
        });

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void startLoginActivity() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    public void startCreateActivity() {
        startActivity(new Intent(getActivity(), CreateActivity.class));
    }

    private void startDetailFragment(String travelId, String dueDate) {
        Bundle bundle = new Bundle();
        bundle.putString("travelId", travelId);
        bundle.putString("dueDate", dueDate);

        FragmentManager manager = getActivity().getSupportFragmentManager();
        TravelDetailFragment travelDetailFragment = (TravelDetailFragment) manager.findFragmentByTag(DETAIL_FRAG);


        if (travelDetailFragment == null) {
            travelDetailFragment = TravelDetailFragment.newInstance();
        }
        travelDetailFragment.setArguments(bundle);

        BaseActivity.addStackedFragmentToActivity(manager,
                travelDetailFragment,
                R.id.rootActivityList,
                DETAIL_FRAG
        );
    }


    public void setTravelData(List<Travel> listOfTravel) {
        this.listOfTravel = listOfTravel;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);


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


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }


    /*-------------------- RecyclerView Boilerplate ----------------------*/

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

        @NonNull
        @Override
        public CustomAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = layoutInflater.inflate(R.layout.item_data, parent, false);
            return new CustomViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomAdapter.CustomViewHolder holder, int position) {
            Travel currentItem = listOfTravel.get(position);
            String tempDateString = currentItem.getStartDate() + " - " + currentItem.getEndDate();

            holder.coloredCircle.setImageResource(currentItem.getColorResource());
            holder.message.setText(
                    currentItem.getMainDestination()
            );
            holder.dateAndTime.setText(
                    tempDateString
            );

            String startDate = currentItem.getStartDate();
            String endDate = currentItem.getEndDate();
            try {
                int travelType = getTravelType(startDate, endDate);

                switch (travelType) {
                    case PAST_TRAVEL:
                        holder.dateAndTime.setTextColor(Color.RED);
                        break;
                    case CURRENT_TRAVEL:
                        holder.dateAndTime.setTextColor(Color.GREEN);
                        break;
                    case DUE_TRAVEL:
                        break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.loading.setVisibility(View.INVISIBLE);
        }

        @Override
        public int getItemCount() {
            return listOfTravel.size();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private CircleImageView coloredCircle;
            private TextView dateAndTime;
            private TextView message;
            private ViewGroup container;
            private ProgressBar loading;

            CustomViewHolder(View itemView) {
                super(itemView);
                this.coloredCircle = itemView.findViewById(R.id.imvListItemCircle);
                this.dateAndTime = itemView.findViewById(R.id.lblDateAndTime);
                this.message = itemView.findViewById(R.id.lblMessage);
                this.loading = itemView.findViewById(R.id.proItemData);

                this.container = itemView.findViewById(R.id.rootListItem);

                this.container.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Travel travel = listOfTravel.get(
                        this.getAdapterPosition()
                );
                String tempDateString = travel.getStartDate() + " - " + travel.getEndDate();

                startDetailFragment(travel.getId(), tempDateString);
            }
        }

    }


    private ItemTouchHelper.Callback createHelperCallback() {
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

                Travel travel = listOfTravel.get(position);
                String travelId = travel.getId();

                locationCollectionViewModel.deleteLocationsByTravelId(travelId);
                personCollectionViewModel.deletePersonListByTravelId(travelId);

                travelCollectionViewModel.deleteTravel(travel);

                //ensure View is consistent with underlying data

                listOfTravel.remove(position);
                adapter.notifyItemRemoved(position);
            }
        };
    }

    private String getCurrentDate(String formatString) {
        Calendar calCurrDate = Calendar.getInstance();
        Date dateCurrDate = calCurrDate.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat(formatString);

        return dateFormat.format(dateCurrDate);
    }

    // checks whether current date is in given date bounds
    private int getTravelType(String startDate, String endDate) throws ParseException {
        String dateFormat = getString(R.string.date_format_string);
        long currentTime = convertStringToDate(currentDate, dateFormat).getTime();
        long startTime = convertStringToDate(startDate, dateFormat).getTime();
        long endTime = convertStringToDate(endDate, dateFormat).getTime();

        if (currentTime < startTime)
            return DUE_TRAVEL;
        else if (currentTime <= endTime)
            return CURRENT_TRAVEL;
        else
            return PAST_TRAVEL;
    }

    private Date convertStringToDate(String dateString, String formatString) throws ParseException {
        DateFormat format = new SimpleDateFormat(formatString, Locale.ENGLISH);
        Date date = format.parse(dateString);
        System.out.println(date);
        return date;
    }
}
