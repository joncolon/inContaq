package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactinfo;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Objects;

import io.realm.Realm;
import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.ui.contactlist.AlertDialogCallback;
import nyc.c4q.jonathancolon.inContaq.utlities.AnimationHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.NameSplitter;
import nyc.c4q.jonathancolon.inContaq.utlities.PicassoHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.RealmDbHelper;

import static nyc.c4q.jonathancolon.inContaq.ui.contactlist.ContactListActivity.CONTACT_ID;

public class ContactInfoFragment extends Fragment implements AlertDialogCallback<Integer>, AdapterView.OnItemSelectedListener {

    private Contact contact;
    private TextView mobile, editButton, polaroidName, email;
    private ImageView contactImageIV, backgroundImageIV;
    private EditText editName, editMobile, editEmail, editAddress;
    private FloatingActionButton saveButton;
    private int selection;
    private AnimationHelper anim;
    private boolean isEditTextEnabled;
    private Realm realm;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_info, container, false);

        realm = Realm.getDefaultInstance();
        long contactId = getActivity().getIntent().getLongExtra(CONTACT_ID, -1);
        contact = RealmDbHelper.getByRealmID(realm, contactId);
        anim = new AnimationHelper(ContactInfoFragment.this.getActivity());
        isEditTextEnabled = false;
        initViews(view);
        setClickListeners();
        displayContactInfo(contact);
        return view;
    }

    private void initViews(View view) {
        polaroidName = (TextView) view.findViewById(R.id.contact_name);
        mobile = (TextView) view.findViewById(R.id.mobile_phone);
        email = (TextView) view.findViewById(R.id.email);

        editName = (EditText) view.findViewById(R.id.edit_name);
        editMobile = (EditText) view.findViewById(R.id.edit_mobile_phone);
        editEmail = (EditText) view.findViewById(R.id.edit_email);
        editAddress = (EditText) view.findViewById(R.id.edit_address);

        saveButton = (FloatingActionButton) view.findViewById(R.id.save_button);
        editButton = (TextView) view.findViewById(R.id.edit_option);

        contactImageIV = (ImageView) view.findViewById(R.id.contact_image);
        backgroundImageIV = (ImageView) view.findViewById(R.id.background_image);

        ArrayAdapter<CharSequence> spinnerArrayAdapter = ArrayAdapter.createFromResource(
                view.getContext(),
                R.array.date_spinner_array,
                R.layout.date_spinner_item);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.date_spinner_dropdown_item);
        Spinner dateSpinner = (Spinner) view.findViewById(R.id.date_spinner);
        dateSpinner.setAdapter(spinnerArrayAdapter);
        dateSpinner.setOnItemSelectedListener(this);
    }

    private void setClickListeners() {
        saveButton.setOnClickListener(view1 -> ContactInfoFragment.this.buildSaveEditDialog());
        editButton.setOnClickListener(view12 -> ContactInfoFragment.this.enableEditContactMode());
    }

    private void displayContactInfo(Contact contact) {
        String nameValue = contact.getFirstName() + " " + contact.getLastName();
        polaroidName.setText(nameValue);
        editName.setText(nameValue);
        editMobile.setText(contact.getMobileNumber());
        loadImages();
        showMobile();
        showEmail();
        showAddress();
    }

    private void buildSaveEditDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.save_changes);
        alertDialog.setMessage(R.string.are_you_sure);

        alertDialog.setPositiveButton(R.string.positive_button, (dialog, which) -> {
            selection = 1;
            ContactInfoFragment.this.alertDialogCallback(selection);
            anim.exitFab(saveButton);
            isEditTextEnabled = false;

        }).setNegativeButton(R.string.negative_button, (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }

    private void enableEditContactMode() {
        enableEditText();
        fadeInContactInfo();
        showFab();
    }

    private void loadImages() {
        PicassoHelper ph = new PicassoHelper(getActivity());
        if (contact.getBackgroundImage() != null) {
            ph.loadImageFromString(contact.getBackgroundImage(), backgroundImageIV);
        }
        if (contact.getContactImage() != null) {
            ph.loadImageFromString(contact.getContactImage(), contactImageIV);
        }
    }

    synchronized private void showMobile() {
        if (isEditTextEnabled) {
            mobile.setVisibility(View.VISIBLE);

        }
        if (contact.getMobileNumber() != null || Objects.equals(contact.getMobileNumber(), "")) {
            mobile.setVisibility(View.VISIBLE);
        }
    }

    synchronized private void showEmail() {
        if (isEditTextEnabled) {
            email.setVisibility(View.VISIBLE);

        }
        if (contact.getEmail() != null || Objects.equals(contact.getEmail(), "")) {
            email.setVisibility(View.VISIBLE);

        }
    }

    synchronized private void showAddress() {
        if (isEditTextEnabled) {
            mobile.setVisibility(View.VISIBLE);
        }
        if (contact.getAddress() != null || Objects.equals(contact.getAddress(), "")) {
        }
    }

    @Override
    public void alertDialogCallback(Integer ret) {
        ret = selection;
        if (ret == 1) {
            saveChanges();
        }
    }

    private void enableEditText() {
        isEditTextEnabled = true;
        editName.setEnabled(true);
        editName.setVisibility(View.VISIBLE);
        editMobile.setEnabled(true);
        editMobile.setVisibility(View.VISIBLE);
        editEmail.setEnabled(true);
        editEmail.setVisibility(View.VISIBLE);
        editAddress.setEnabled(true);
        editAddress.setVisibility(View.VISIBLE);
    }

    private void fadeInContactInfo() {
        showMobile();
        showEmail();
        showAddress();
    }

    private void showFab() {
        saveButton.setVisibility(View.VISIBLE);
        anim.enterFab(saveButton);
    }

    private void saveChanges() {
        String email = editEmail.getText().toString();
        String address = editAddress.getText().toString();
        String name = editName.getText().toString().trim();
        String[] splitName = NameSplitter.splitFirstAndLastName(name);


        realm.executeTransaction(realm1 -> {
            contact.setFirstName(splitName[0]);
            contact.setLastName(splitName[1]);
            contact.setEmail(email);
            contact.setAddress(address);
            contact.setMobileNumber(editMobile.getText().toString());
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        displayContactInfo(contact);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (String.valueOf(parent.getItemAtPosition(position))) {
            case "1 WEEK":
                realm.executeTransaction(realm1 -> {
                    contact.setReminderDuration(1);
                    contact.setReminderEnabled(true);
                });


            case "2 WEEKS":
                realm.executeTransaction(realm1 -> {
                    contact.setReminderDuration(2);
                    contact.setReminderEnabled(true);
                });
                break;
            case "3 WEEKS":
                realm.executeTransaction(realm1 -> {
                    contact.setReminderDuration(3);
                    contact.setReminderEnabled(true);
                });
                break;
            case "CANCEL":
                realm.executeTransaction(realm1 -> {
                    contact.setReminderDuration(0);
                    contact.setReminderEnabled(false);
                });
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RealmDbHelper.closeRealm(realm);
    }
}


