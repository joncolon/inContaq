package nyc.c4q.jonathancolon.inContaq.contactlist.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fontometrics.Fontometrics;

import org.parceler.Parcels;

import java.util.Objects;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.AlertDialogCallback;
import nyc.c4q.jonathancolon.inContaq.contactlist.Contact;
import nyc.c4q.jonathancolon.inContaq.contactlist.activities.ContactListActivity;
import nyc.c4q.jonathancolon.inContaq.sqlite.ContactDatabaseHelper;
import nyc.c4q.jonathancolon.inContaq.utilities.bitmap.SetContactImageWorkerTask;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactInfoFragment extends Fragment implements AlertDialogCallback<Integer> {

    Contact contact;
    private TextView name, mobile, email, polaroidName, address, editButton;
    private ImageView contactImageIV, backgroundImageIV;
    private EditText editName, editMobile, editEmail, editAddress;
    private FloatingActionButton saveButton;
    private CardView infoCard;
    private SQLiteDatabase db;
    private int selection;


    public static ContactInfoFragment newInstance() {

        ContactInfoFragment fragment = new ContactInfoFragment();
        Bundle b = new Bundle();

        fragment.setArguments(b);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact_info, container, false);
        contact = Parcels.unwrap(getActivity().getIntent().getParcelableExtra(ContactListActivity.PARCELLED_CONTACT));

        initViews(view);
        saveButton.setOnClickListener(view1 -> buildSaveEditDialog());
        editButton.setOnClickListener(view12 -> enableEditContactMode(contact));
        displayContactInfo(contact);

        return view;
    }

    private void initViews(View view) {
        infoCard = (CardView) view.findViewById(R.id.contact_info_card);

        polaroidName = (TextView) view.findViewById(R.id.contact_name);
        name = (TextView) view.findViewById(R.id.name);
        mobile = (TextView) view.findViewById(R.id.mobile_phone);
        email = (TextView) view.findViewById(R.id.email);
        address = (TextView) view.findViewById(R.id.address);

        editName = (EditText) view.findViewById(R.id.edit_name);
        editMobile = (EditText) view.findViewById(R.id.edit_mobile_phone);
        editEmail = (EditText) view.findViewById(R.id.edit_email);
        editAddress = (EditText) view.findViewById(R.id.edit_address);

        saveButton = (FloatingActionButton) view.findViewById(R.id.save_button);
        editButton = (TextView) view.findViewById(R.id.edit_option);

        contactImageIV = (ImageView) view.findViewById(R.id.contact_img);
        backgroundImageIV = (ImageView) view.findViewById(R.id.background_image);
    }

    public void buildSaveEditDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.save_changes);
        alertDialog.setMessage(R.string.are_you_sure);

        alertDialog.setPositiveButton(R.string.positive_button, (dialog, which) -> {
            selection = 1;
            alertDialogCallback(selection);
        });

        alertDialog.setNegativeButton(R.string.negative_button, (dialog, which) -> {
            dialog.cancel();
        });

        alertDialog.show();
    }

    private void enableEditContactMode(final Contact contact) {
        infoCard.animate()
                .alpha(1.0f)
                .setDuration(3000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        infoCard.getLayoutParams();
                        super.onAnimationStart(animation);
                        showContactInfo();
                        fadeInContactInfo();
                        enableEditText();
                        saveButton.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });
    }

    private void displayContactInfo(Contact contact) {
        String nameValue = contact.getFirstName() + " " + contact.getLastName();
        ContactDatabaseHelper dbHelper = ContactDatabaseHelper.getInstance(getActivity());
        db = dbHelper.getWritableDatabase();
        polaroidName.setText(nameValue);
        editName.setText(nameValue);
        loadImages();

        if (contact.getCellPhoneNumber() != null || !Objects.equals(contact.getCellPhoneNumber(), "")) {
            mobile.setVisibility(View.VISIBLE);
            editMobile.setVisibility(View.VISIBLE);
            editMobile.setText(contact.getCellPhoneNumber());
        }
        if (contact.getEmail() != null || !Objects.equals(contact.getEmail(), "")) {
            email.setVisibility(View.VISIBLE);
            editEmail.setVisibility(View.VISIBLE);
            editEmail.setText(contact.getEmail());
        }
        if (contact.getAddress() != null || !Objects.equals(contact.getAddress(), "")) {
            address.setVisibility(View.VISIBLE);
            editAddress.setVisibility(View.VISIBLE);
            editAddress.setText(contact.getAddress());
        }
    }

    @Override
    public void alertDialogCallback(Integer ret) {
        ret = selection;
        if (ret == 1) {
            String mText = editName.getText().toString().trim();

            if (mText.trim().length() > 0) {
                String lastName = "";
                String firstName;
                if (mText.split("\\w+").length > 1) {

                    lastName = mText.substring(mText.lastIndexOf(" ") + 1);
                    firstName = mText.substring(0, mText.lastIndexOf(' '));
                } else {
                    firstName = mText;
                }
                contact.setFirstName(firstName);
                contact.setLastName(lastName);
            }

            contact.setCellPhoneNumber(editMobile.getText().toString());
            String email = editEmail.getText().toString();
            String address = editAddress.getText().toString();
            contact.setEmail(email);
            contact.setAddress(address);

            cupboard().withDatabase(db).put(contact);
        }
    }

    private void showContactInfo() {
        name.setVisibility(View.VISIBLE);
        mobile.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        address.setVisibility(View.VISIBLE);

        editName.setVisibility(View.VISIBLE);
        editMobile.setVisibility(View.VISIBLE);
        editEmail.setVisibility(View.VISIBLE);
        editAddress.setVisibility(View.VISIBLE);

        if (contact.getEmail() == null) {
            email.setAlpha(0);
            editEmail.setAlpha(0);
        }
        if (contact.getAddress() == null) {
            address.setAlpha(0);
            editAddress.setAlpha(0);
        }
        if (contact.getCellPhoneNumber() == null) {
            mobile.setAlpha(0);
            editMobile.setAlpha(0);
        }
    }

    private void fadeInContactInfo() {
        mobile.animate().alpha(1.0f).setDuration(300);
        email.animate().alpha(1.0f).setDuration(500);
        address.animate().alpha(1.0f).setDuration(700);
        editMobile.animate().alpha(1.0f).setDuration(300);
        editEmail.animate().alpha(1.0f).setDuration(500);
        editAddress.animate().alpha(1.0f).setDuration(700);
    }

    private void enableEditText() {
        editName.setEnabled(true);
        editMobile.setEnabled(true);
        editEmail.setEnabled(true);
        editAddress.setEnabled(true);
    }

    private void loadImages() {
        //Asynchronously load bitmaps from Contact object
        if (contact.getBackgroundImage() != null) {
            setContactImage(contact.getBackgroundImage(), backgroundImageIV);
        }
        if (contact.getContactImage() != null) {
            setContactImage(contact.getContactImage(), contactImageIV);

        }
    }

    private void setContactImage(byte[] bytes, ImageView imageView) {
        SetContactImageWorkerTask task = new SetContactImageWorkerTask(imageView);
        task.execute(bytes);
    }

    @Override
    public void onResume() {
        super.onResume();
        displayContactInfo(contact);
        polaroidName.setTypeface(Fontometrics.amatic_bold(getActivity()));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}


