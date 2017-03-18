package nyc.c4q.jonathancolon.inContaq.contactlist.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import org.parceler.Parcels;
import java.util.Objects;
import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.AlertDialogCallback;
import nyc.c4q.jonathancolon.inContaq.contactlist.Animations;
import nyc.c4q.jonathancolon.inContaq.contactlist.Contact;
import nyc.c4q.jonathancolon.inContaq.contactlist.PicassoHelper;
import nyc.c4q.jonathancolon.inContaq.contactlist.activities.ContactListActivity;
import static android.app.Activity.RESULT_OK;
import static nyc.c4q.jonathancolon.inContaq.utlities.sqlite.SqlHelper.saveToDatabase;

public class ContactInfoFragment extends Fragment implements AlertDialogCallback<Integer> {

    private Contact contact;
    private TextView mobile, email, polaroidName, address, editButton;
    private ImageView contactImageIV, backgroundImageIV;
    private EditText editName, editMobile, editEmail, editAddress;
    private FloatingActionButton saveButton;
    private int selection;
    private Animations anim;
    private boolean isEditTextEnabled;
    private ImageButton takePicture, choosePicture;

    private static final int RESULT_LOAD_BACKGROUND_IMG = 2;
    private static final int RESULT_LOAD_CONTACT_IMG = 1;

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
        address = (TextView) view.findViewById(R.id.address);

        editName = (EditText) view.findViewById(R.id.edit_name);
        editMobile = (EditText) view.findViewById(R.id.edit_mobile_phone);
        editEmail = (EditText) view.findViewById(R.id.edit_email);
        editAddress = (EditText) view.findViewById(R.id.edit_address);

        saveButton = (FloatingActionButton) view.findViewById(R.id.save_button);
        editButton = (TextView) view.findViewById(R.id.edit_option);

        contactImageIV = (ImageView) view.findViewById(R.id.contact_image);
        backgroundImageIV = (ImageView) view.findViewById(R.id.background_image);

        takePicture = (ImageButton) view.findViewById(R.id.take_picture);
        choosePicture = (ImageButton) view.findViewById(R.id.choose_picture);

    }

    private void setClickListeners() {

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                ContactInfoFragment.this.buildSaveEditDialog();

            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view12) {
                ContactInfoFragment.this.enableEditContactMode();
            }
        });

        contactImageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DialogFragment dialogFragment = new DialogFragment ();
                dialogFragment.show(fm, "Choose Picture");
            }
        });

        backgroundImageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DialogFragment dialogFragment = new DialogFragment ();
                dialogFragment.show(fm, "Choose Picture");
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    contactImageIV.setImageURI(selectedImage);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    contactImageIV.setImageURI(selectedImage);
                }
                break;
        }
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

        alertDialog.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selection = 1;
                ContactInfoFragment.this.alertDialogCallback(selection);
                anim.exitFab(saveButton);
                isEditTextEnabled = false;

            }
        });

        alertDialog.setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
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
        if (isEditTextEnabled == true) {
            mobile.setVisibility(View.VISIBLE);

        }

        if (contact.getCellPhoneNumber() != null || Objects.equals(contact.getCellPhoneNumber(), "")) {
            mobile.setVisibility(View.VISIBLE);
        }
    }

    synchronized private void showEmail() {
        if (isEditTextEnabled == true) {
            email.setVisibility(View.VISIBLE);

        }
        if (contact.getEmail() != null || Objects.equals(contact.getEmail(), "")) {
            email.setVisibility(View.VISIBLE);

        }
    }

    synchronized private void showAddress() {
        if (isEditTextEnabled == true) {
            mobile.setVisibility(View.VISIBLE);
        }
        if (contact.getAddress() != null || Objects.equals(contact.getAddress(), "")) {
        }
    }

    @Override
    public void alertDialogCallback(Integer ret) {
        ret = selection;
        if (ret == 1) {
            String email = editEmail.getText().toString();
            String address = editAddress.getText().toString();
            String name = editName.getText().toString().trim();
            splitFirstAndLastName(name);

            contact.setEmail(email);
            contact.setAddress(address);
            contact.setCellPhoneNumber(editMobile.getText().toString());

            saveToDatabase(contact, getActivity());
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

    private void splitFirstAndLastName(String input) {
        if (input.trim().length() > 0) {
            String lastName = "";
            String firstName;
            if (input.split("\\w+").length > 1) {

                lastName = input.substring(input.lastIndexOf(" ") + 1);
                firstName = input.substring(0, input.lastIndexOf(' '));
            } else {
                firstName = input;
            }
            contact.setFirstName(firstName);
            contact.setLastName(lastName);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        displayContactInfo(contact);
    }
}


