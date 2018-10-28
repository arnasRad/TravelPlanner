package com.gmail.arnasrad.travelplanner.create;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.gmail.arnasrad.travelplanner.R;

public class AddPersonFragment extends DialogFragment implements TextView.OnEditorActionListener {
    private TextView nameTextView;
    private TextView surnameTextView;
    private TextView emailTextView;

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }

    public interface AddPersonDialogListener {
        void onFinishAddDialog(String name, String surname, String email);

    }

    public AddPersonFragment() {
        // Required empty public constructor
    }

    public static AddPersonFragment newInstance() {
        return new AddPersonFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_person, container, false);
    }
*/
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.add_person_title);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View newFileView = inflater.inflate(R.layout.fragment_add_person, null);
        alertDialogBuilder.setView(newFileView);

        nameTextView = newFileView.findViewById(R.id.add_person_name);
        surnameTextView = newFileView.findViewById(R.id.add_person_surname);
        emailTextView = newFileView.findViewById(R.id.add_person_email);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return alertDialogBuilder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog alertDialog = (AlertDialog) getDialog();

        if(alertDialog != null) {
            Button positiveButton = alertDialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = nameTextView.getText().toString();
                    String surname = surnameTextView.getText().toString();
                    String email = emailTextView.getText().toString();
                    boolean canAdd = true;

                    if(name.trim().length() == 0) {
                        nameTextView.setError(getString(R.string.blank_person_name_error));
                        canAdd = false;
                    }
                    if(surname.trim().length() == 0) {
                        surnameTextView.setError(getString(R.string.blank_person_surname_error));
                        canAdd = false;
                    }

                    if (canAdd)
                        sendBackResult(name, surname, email);

                }
            });
        }
    }

    private void sendBackResult(String name, String surname, String email) {
        AddPersonDialogListener listener = (AddPersonDialogListener) getTargetFragment();
        listener.onFinishAddDialog(name, surname, email);
        dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
