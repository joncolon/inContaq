package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactviewpager.contactinfo;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import android.widget.Toast;

import java.util.Objects;

import io.realm.Realm;
import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.ui.contactlist.AlertDialogCallback;
import nyc.c4q.jonathancolon.inContaq.utlities.AnimationHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.FontHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.PicassoHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.RealmDbHelper;

import static nyc.c4q.jonathancolon.inContaq.ui.contactlist.ContactListActivity.CONTACT_ID;

public class ContactInfoFragment extends Fragment implements AlertDialogCallback<Integer>, AdapterView.OnItemSelectedListener {

    private static final int RESULT_LOAD_BACKGROUND_IMG = 2;
    private static final int RESULT_LOAD_CONTACT_IMG = 1;

    private Contact contact;
    private TextView address, mobile, email, displayName, editOption;
    private ImageView contactImageIV, backgroundImageIV;

    private PicassoHelper picasso;

    private EditText editMobile, editEmail, editAddress;
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
        FragmentActivity context = getActivity();
        picasso = new PicassoHelper(context);

        isEditTextEnabled = false;
        initViews(view);
        setClickListeners();
        displayContactInfo(contact);
        return view;
    }

    private void initViews(View view) {
        mobile = (TextView) view.findViewById(R.id.mobile_phone);
        email = (TextView) view.findViewById(R.id.email);
        address = (TextView) view.findViewById(R.id.address);
        displayName = (TextView) view.findViewById(R.id.display_name);

        editMobile = (EditText) view.findViewById(R.id.edit_mobile_phone);
        editEmail = (EditText) view.findViewById(R.id.edit_email);
        editAddress = (EditText) view.findViewById(R.id.edit_address);

        saveButton = (FloatingActionButton) view.findViewById(R.id.save_button);
        editOption = (TextView) view.findViewById(R.id.edit_option);

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
        editOption.setOnClickListener(view12 -> ContactInfoFragment.this.enableEditContactMode());
        setClickListenersWhenInPortraitMode();

    }

    private void displayContactInfo(Contact contact) {
        String nameValue = contact.getFirstName() + " " + contact.getLastName();
        displayName.setText(nameValue);
        displayName.setText(nameValue);
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
    }

    synchronized private void showAddress() {
        if (isEditTextEnabled) {
            mobile.setVisibility(View.VISIBLE);
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


        realm.executeTransaction(realm1 -> {
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

    private void setClickListenersWhenInPortraitMode() {
        int value = getActivity().getResources().getConfiguration().orientation;

        if (value == Configuration.ORIENTATION_PORTRAIT) {
            FontHelper fontHelper = new FontHelper(getContext());
            fontHelper.applyFont(displayName);
            displayContactInfo(contact);

            contactImageIV.setOnClickListener(v -> {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                ContactInfoFragment.this.startActivityForResult(galleryIntent,
                        RESULT_LOAD_CONTACT_IMG);
            });

            backgroundImageIV.setOnClickListener(v -> {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                ContactInfoFragment.this.startActivityForResult(galleryIntent,
                        RESULT_LOAD_BACKGROUND_IMG);
            });
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {

                switch (requestCode) {
                    case 1:
                        updateContactImage(data, picasso);
                        break;
                    case 2:
                        setBackgroundImage(data, picasso);
                        break;
                }
            } else {
                Toast.makeText(getActivity(), R.string.error_message_photo_not_selected,
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), R.string.error_message_general, Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void updateContactImage(Intent data, PicassoHelper picasso) {
        Uri contactImgUri = data.getData();
        picasso.loadImageFromUri(contactImgUri, contactImageIV);
        realm.executeTransaction(realm1 -> contact.setContactImage(contactImgUri.toString()));
    }

    public void setBackgroundImage(Intent data, PicassoHelper ph) {
        Uri bgImgUri = data.getData();
        ph.loadImageFromUri(bgImgUri, backgroundImageIV);
        realm.executeTransaction(realm1 -> contact.setBackgroundImage(bgImgUri.toString()));
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


