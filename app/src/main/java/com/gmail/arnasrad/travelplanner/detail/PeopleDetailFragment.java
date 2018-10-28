package com.gmail.arnasrad.travelplanner.detail;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.arnasrad.travelplanner.R;
import com.gmail.arnasrad.travelplanner.RoomDemoApplication;
import com.gmail.arnasrad.travelplanner.create.AddPersonFragment;
import com.gmail.arnasrad.travelplanner.create.MainCreateFragment;
import com.gmail.arnasrad.travelplanner.data.Person;
import com.gmail.arnasrad.travelplanner.viewmodel.NewPersonViewModel;
import com.gmail.arnasrad.travelplanner.viewmodel.PersonCollectionViewModel;

import java.util.List;

import javax.inject.Inject;


public class PeopleDetailFragment extends Fragment implements
        AddPersonFragment.AddPersonDialogListener {
    private int ADD_PERSON_REQUEST = 3;
    private String travelId;

    private List<Person> listOfPerson;
    private RecyclerView personRecyclerView;

    PersonCollectionViewModel personCollectionViewModel;
    NewPersonViewModel newPersonViewModel;

    private PersonAdapter personAdapter;
    private LayoutInflater layoutInflater;

    View mView;
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public PeopleDetailFragment() {
        // Required empty public constructor
    }

    public static PeopleDetailFragment newInstance() {
        return new PeopleDetailFragment();
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
            mView = inflater.inflate(R.layout.fragment_people_detail, container, false);

        travelId = getArguments().getString("travelId");
        layoutInflater = getActivity().getLayoutInflater();

        FloatingActionButton floatingActionButton = mView.findViewById(R.id.fab_detail_add_new_person);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddPersonFragment();
            }
        });

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        personRecyclerView = view.findViewById(R.id.people_detail_rw);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        personCollectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(PersonCollectionViewModel.class);

        newPersonViewModel= ViewModelProviders.of(this, viewModelFactory)
                .get(NewPersonViewModel.class);

        personCollectionViewModel.getPersonList(travelId).observe(this, new Observer<List<Person>>() {
            @Override
            public void onChanged(@Nullable List<Person> people) {
                if (listOfPerson == null) {
                    setPersonData(people);
                }
            }
        });
    }


    public void setPersonData(List<Person> listOfPerson) {
        this.listOfPerson = listOfPerson;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        personRecyclerView.setLayoutManager(layoutManager);
        personAdapter = new PersonAdapter();
        personRecyclerView.setAdapter(personAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createPersonHelperCallback());
        itemTouchHelper.attachToRecyclerView(personRecyclerView);
    }

    private void startAddPersonFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        AddPersonFragment addPersonFragment = AddPersonFragment.newInstance();
        addPersonFragment.setTargetFragment(PeopleDetailFragment.this, ADD_PERSON_REQUEST);
        addPersonFragment.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Material_Dialog);
        addPersonFragment.show(fragmentManager, "Add Person");

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
                personCollectionViewModel.deletePerson(listOfPerson.get(position));
                listOfPerson.remove(position);
                personAdapter.notifyItemRemoved(position);
            }
        };
    }

    @Override
    public void onFinishAddDialog(String name, String surname, String email) {
        Person person = new Person(travelId, name, surname, email);

        newPersonViewModel.addNewPersonToDatabase(person);

        listOfPerson.add(person);
        personAdapter.notifyItemInserted(listOfPerson.size());
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

                Person travel = listOfPerson.get(position);

                personCollectionViewModel.deletePersonListByTravelId(travelId);

                //ensure View is consistent with underlying data
                listOfPerson.remove(position);
                personAdapter.notifyItemRemoved(position);
            }
        };
    }
}
