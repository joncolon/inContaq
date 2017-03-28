package nyc.c4q.jonathancolon.inContaq.contactlist.fragments;


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

import org.parceler.Parcels;

import java.util.Objects;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.AlertDialogCallback;
import nyc.c4q.jonathancolon.inContaq.utlities.Animations;
import nyc.c4q.jonathancolon.inContaq.utlities.PicassoHelper;
import nyc.c4q.jonathancolon.inContaq.contactlist.activities.ContactListActivity;
import nyc.c4q.jonathancolon.inContaq.contactlist.model.Contact;
import nyc.c4q.jonathancolon.inContaq.notifications.ContactNotificationService;
import nyc.c4q.jonathancolon.inContaq.utlities.NameSplitter;
import nyc.c4q.jonathancolon.inContaq.utlities.sqlite.SqlHelper;

public class ContactInfoFragment extends Fragment implements AlertDialogCallback<Integer>, AdapterView.OnItemSelectedListener {

    private Contact contact;
    private TextView mobile;
    private TextView email;
    private TextView polaroidName;
    private TextView editButton;
    private ImageView contactImageIV, backgroundImageIV;
    private EditText editName, editMobile, editEmail, editAddress;
    private FloatingActionButton saveButton;
    private int selection;
    private Animations anim;
    private boolean isEditTextEnabled;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_info, container, false);
        contact = Parcels.unwrap(getActivity().getIntent().
                getParcelableExtra(ContactListActivity.PARCELLED_CONTACT));
        anim = new Animations(ContactInfoFragment.this.getActivity());
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

        Spinner dateSpinner = (Spinner) view.findViewById(R.id.date_spinner);
        ArrayAdapter<CharSequence> spinnerArrayAdapter = ArrayAdapter.createFromResource(
                view.getContext(),
                R.array.date_spinner_array,
                R.layout.date_spinner_item);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.date_spinner_dropdown_item);
        dateSpinner = (Spinner) view.findViewById(R.id.date_spinner);
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
        editMobile.setText(contact.getCellPhoneNumber());
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
        if (contact.getCellPhoneNumber() != null || Objects.equals(contact.getCellPhoneNumber(), "")) {
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

    private void saveChanges() {
        String email = editEmail.getText().toString();
        String address = editAddress.getText().toString();
        String name = editName.getText().toString().trim();
        String[] splitName = NameSplitter.splitFirstAndLastName(name);

        contact.setFirstName(splitName[0]);
        contact.setLastName(splitName[1]);
        contact.setEmail(email);
        contact.setAddress(address);
        contact.setCellPhoneNumber(editMobile.getText().toString());

        SqlHelper.saveToDatabase(contact, getActivity());
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

    @Override
    public void onResume() {
        super.onResume();
        displayContactInfo(contact);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//        Contact contact = unwrapParcelledContact();

        switch (String.valueOf(parent.getItemAtPosition(position))) {

            case "DAILY":
                // TODO: 3/8/17 if last sent text == to 7 days + last sent text date then, notification
                break;
            case "WEEKLY":
                break;
            case "2 WEEKS":
                // TODO: 3/8/17 if last sent text == to 21 days + last sent text date then, notification
                ContactNotificationService mContactNotificationService = new ContactNotificationService();
                mContactNotificationService.startNotification(contact, getContext());
                break;
            case "MONTHLY":
                // TODO: 3/8/17 if last sent text == to 30 days + last sent text date then, notification
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}


