package com.gmail.arnasrad.travelplanner.list;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gmail.arnasrad.travelplanner.R;
import com.gmail.arnasrad.travelplanner.RoomDemoApplication;
import com.gmail.arnasrad.travelplanner.data.Travel;
import com.gmail.arnasrad.travelplanner.detail.TravelDetailFragment;
import com.gmail.arnasrad.travelplanner.util.BaseActivity;
import com.gmail.arnasrad.travelplanner.viewmodel.LocationCollectionViewModel;
import com.gmail.arnasrad.travelplanner.viewmodel.PersonCollectionViewModel;
import com.gmail.arnasrad.travelplanner.viewmodel.TravelCollectionViewModel;

import java.util.List;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

public class OutdatedListFragment extends Fragment {
    private static final String ARCHIVE_DETAIL_FRAG = "ARCHIVE_DETAIL_FRAG";

    private String currentUsername;
    private LayoutInflater layoutInflater;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;

    private View mView;
    private List<Travel> listOfTravel;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    LocationCollectionViewModel locationCollectionViewModel;
    PersonCollectionViewModel personCollectionViewModel;
    TravelCollectionViewModel travelCollectionViewModel;

    public OutdatedListFragment() {
        // Required empty public constructor
    }

    public static OutdatedListFragment newInstance() {
        return new OutdatedListFragment();
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
        if (mView == null)
            mView = inflater.inflate(R.layout.fragment_outdated_list, container, false);

        recyclerView = mView.findViewById(R.id.outdated_list_rw);
        layoutInflater = getActivity().getLayoutInflater();

        assert getArguments() != null;
        listOfTravel = (List<Travel>) getArguments().getSerializable("archive_list");

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        locationCollectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(LocationCollectionViewModel.class);
        personCollectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(PersonCollectionViewModel.class);

        travelCollectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(TravelCollectionViewModel.class);

        setTravelData();
    }

    public void setTravelData() {
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

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

        @NonNull
        @Override
        public CustomAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = layoutInflater.inflate(R.layout.item_data, parent, false);
            return new CustomViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
            Travel currentItem = listOfTravel.get(position);
            String tempDateString = currentItem.getStartDate() + " - " + currentItem.getEndDate();

            holder.coloredCircle.setImageResource(currentItem.getColorResource());
            holder.message.setText(
                    currentItem.getMainDestination()
            );
            holder.dateAndTime.setText(
                    tempDateString
            );

            holder.dateAndTime.setTextColor(Color.RED);

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

                listOfTravel.remove(position);
                adapter.notifyItemRemoved(position);
            }
        };
    }


    private void startDetailFragment(String travelId, String dueDate) {
        Bundle bundle = new Bundle();
        bundle.putString("travelId", travelId);
        bundle.putString("dueDate", dueDate);

        FragmentManager manager = getActivity().getSupportFragmentManager();
        TravelDetailFragment travelDetailFragment = (TravelDetailFragment) manager.findFragmentByTag(ARCHIVE_DETAIL_FRAG);


        if (travelDetailFragment == null) {
            travelDetailFragment = TravelDetailFragment.newInstance();
        }
        travelDetailFragment.setArguments(bundle);

        BaseActivity.addStackedFragmentToActivity(manager,
                travelDetailFragment,
                R.id.rootActivityList,
                ARCHIVE_DETAIL_FRAG
        );
    }
}
