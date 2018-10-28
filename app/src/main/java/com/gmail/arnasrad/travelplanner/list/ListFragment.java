package com.gmail.arnasrad.travelplanner.list;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
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

import java.util.List;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;



public class ListFragment extends Fragment {
    private static final String DETAIL_FRAG = "DETAIL_FRAG";

    private View mView;
    private List<Travel> listOfTravel;

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

        /*
        //Set up and subscribe (observe) to the ViewModel
        listItemCollectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ListItemCollectionViewModel.class);

        listItemCollectionViewModel.getListItems().observe(this, new Observer<List<ListItem>>() {
            @Override
            public void onChanged(@Nullable List<ListItem> listItems) {
                //if (listOfData == null) {
                //    setListData(listItems);
                //}
            }
        });
*/
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
/*
        toolbar = v.findViewById(R.id.tlbListActivity);

        toolbar.setTitle(R.string.titleToolbar);
        toolbar.setLogo(R.drawable.ic_view_list_white_24dp);
        toolbar.setTitleMarginStart(72);
*/
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

/*
    public void startDetailFragment(String itemId, View viewRoot) {
        Activity container = getActivity();
        Intent i = new Intent(container, DetailActivity.class);
        i.putExtra(EXTRA_ITEM_ID, itemId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            assert container != null;
            container.getWindow().setEnterTransition(new Fade(Fade.IN));
            container.getWindow().setEnterTransition(new Fade(Fade.OUT));

            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(container,
                            new Pair<>(viewRoot.findViewById(R.id.imvListItemCircle),
                                    getString(R.string.transitionDrawable)),
                            new Pair<>(viewRoot.findViewById(R.id.lblMessage),
                                    getString(R.string.transitionMessage)),
                            new Pair<>(viewRoot.findViewById(R.id.lblDateAndTime),
                                    getString(R.string.transitionTimeAndDate)));

            startActivity(i, options.toBundle());

        } else {
            startActivity(i);
        }
    }

*/
/*
    public void setListData(List<ListItem> listOfData) {
        //this.listOfData = listOfData;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        //adapter = new CustomAdapter();
        //recyclerView.setAdapter(adapter);


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

    }*/


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

            holder.coloredCircle.setImageResource(currentItem.getColorResource());

            holder.message.setText(
                    currentItem.getMainDestination()
            );

            holder.dateAndTime.setText(
                    currentItem.getDueDate()
            );

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

                startDetailFragment(travel.getId(), travel.getDueDate());
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

}
