package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactinfo;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetMenuDialog;

import java.util.Objects;

import javax.inject.Inject;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.db.RealmService;
import nyc.c4q.jonathancolon.inContaq.di.Injector;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.utlities.AnimationHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.FontHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.PicassoHelper;

import static nyc.c4q.jonathancolon.inContaq.ui.contactlist.ContactListActivity.CONTACT_ID;

public class ContactInfoFragment extends Fragment implements AlertDialogCallback<Integer>,
        AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener,
        DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {

    private static final int RESULT_LOAD_BACKGROUND_IMG = 2;
    private static final int RESULT_LOAD_CONTACT_IMG = 1;

    private Contact contact;
    private TextView mobile, email, displayName, editOption;
    private ImageView contactImageIV, backgroundImageIV, sendMessageIV;

    private PicassoHelper picasso;

    private EditText editMobile, editEmail, editAddress;
    private FloatingActionButton saveButton;
    private int selection;
    private AnimationHelper anim;
    private boolean isEditTextEnabled;
    private MessengerAppLauncher appLauncher;
    private Spinner dateSpinner;
    private Switch reminderSwitch;
    private int bottomSheetMenu;
    private BottomSheetMenuDialog dialog;

    @Inject
    RealmService realmService;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_info, container, false);


        Injector.getApplicationComponent().inject(this);
        long contactId = getActivity().getIntent().getLongExtra(CONTACT_ID, -1);
        contact = realmService.getByRealmID(contactId);
        anim = new AnimationHelper(ContactInfoFragment.this.getActivity());
        picasso = new PicassoHelper();
        appLauncher = new MessengerAppLauncher(getActivity(), contact.getMobileNumber());

        isEditTextEnabled = false;
        initViews(view);
        setClickListeners();
        displayContactInfo(contact);
        return view;
    }

    private BottomSheetMenuDialog initBottomSheetDialog(int menuId) {
        BottomSheetMenuDialog dialog = new BottomSheetBuilder(getActivity(),
                R.style.AppTheme_BottomSheetDialog)
                .setMode(BottomSheetBuilder.MODE_GRID)
                .setMenu(menuId)
                .setItemClickListener(item -> {
                    int i = item.getItemId();
                    if (i == R.id.sms_icon) {
                        appLauncher.openDefaultSms();
                    } else if (i == R.id.whatsapp_icon) {
                        appLauncher.openWhatsApp();

                    } else if (i == R.id.fb_icon) {
                        appLauncher.openFacebookMessenger();

                    } else if (i == R.id.slack_icon) {
                        appLauncher.openSlack();

                    } else if (i == R.id.linked_in) {
                        appLauncher.openLinkedIn();

                    } else if (i == R.id.hangouts_icon) {
                        appLauncher.openHangouts();

                    } else if (i == R.id.snapchat_icon) {
                        appLauncher.openSnapChat();

                    } else if (i == R.id.skype_icon) {
                        appLauncher.openSkype();
                    }
                })
                .createDialog();
        dialog.setOnCancelListener(this);
        dialog.setOnDismissListener(this);
        return dialog;
    }

    private void initViews(View view) {
        mobile = (TextView) view.findViewById(R.id.mobile_phone);
        email = (TextView) view.findViewById(R.id.email);
        displayName = (TextView) view.findViewById(R.id.display_name);

        editMobile = (EditText) view.findViewById(R.id.edit_mobile_phone);
        editEmail = (EditText) view.findViewById(R.id.edit_email);
        editAddress = (EditText) view.findViewById(R.id.edit_address);

        saveButton = (FloatingActionButton) view.findViewById(R.id.save_button);
        editOption = (TextView) view.findViewById(R.id.edit_option);

        contactImageIV = (ImageView) view.findViewById(R.id.contact_image);
        backgroundImageIV = (ImageView) view.findViewById(R.id.background_image);
        sendMessageIV = (ImageView) view.findViewById(R.id.send_message);

        bottomSheetMenu = R.menu.menu_bottom_sheet_all;


        dialog = initBottomSheetDialog(bottomSheetMenu);
        initSpinner(view);
        initSwitch(view);

        sendMessageIV.setOnClickListener(v -> {
            dialog.show();
        });
    }

    private void initSwitch(View view) {
        reminderSwitch = (Switch) view.findViewById(R.id.reminder_switch);
        if (reminderSwitch != null) {
            if (contact.isReminderEnabled()) {
                dateSpinner.setVisibility(View.VISIBLE);
            }
            reminderSwitch.setChecked(contact.isReminderEnabled());
            reminderSwitch.setOnCheckedChangeListener(this);
        }
    }

    private void initSpinner(View view) {
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
        editOption.setOnClickListener(view12 -> ContactInfoFragment.this.enableEditContactMode());
        setClickListenersWhenInPortraitMode();
    }

    private void buildSaveEditDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.save_changes);
        alertDialog.setMessage(R.string.are_you_sure);

        alertDialog.setPositiveButton(R.string.positive_button, (dialog, which) -> {
            selection = 1;
            ContactInfoFragment.this.alertDialogCallback(selection);
            anim.exitFab(saveButton);
            saveButton.setVisibility(View.GONE);
            isEditTextEnabled = false;

        }).setNegativeButton(R.string.negative_button, (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }

    private void enableEditContactMode() {
        enableEditText();
        fadeInContactInfo();
        showFab();
    }

    @Override
    public void alertDialogCallback(Integer ret) {
        ret = selection;
        if (ret == 1) {
            saveChanges();
        }
    }

    private void disableEditText() {
        isEditTextEnabled = false;
        editMobile.setEnabled(false);
        editEmail.setEnabled(false);
        editAddress.setEnabled(false);
    }

    private void enableEditText() {
        isEditTextEnabled = true;
        editMobile.setEnabled(true);
        editEmail.setEnabled(true);
        editAddress.setEnabled(true);
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

        realmService.getInstance().executeTransaction(realm1 -> {
            contact.setEmail(email);
            contact.setAddress(address);
            contact.setMobileNumber(editMobile.getText().toString());
        });

        disableEditText();
    }

    @Override
    public void onResume() {
        super.onResume();
        displayContactInfo(contact);
    }

    private void displayContactInfo(Contact contact) {
        String nameValue = contact.getFirstName() + " " + contact.getLastName();
        displayName.setText(nameValue);
        editEmail.setText(contact.getEmail());
        editAddress.setText(contact.getAddress());
        editMobile.setText(contact.getMobileNumber());

        loadImages();
        showMobile();
        showEmail();
        showAddress();
    }

    private void loadImages() {
        PicassoHelper ph = new PicassoHelper();
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (String.valueOf(parent.getItemAtPosition(position))) {
            case "1 week has passed":
                realmService.getInstance().executeTransaction(realm1 -> {
                    contact.setReminderDuration(1);
                    contact.setReminderEnabled(true);
                });
            case "2 weeks have passed":
                realmService.getInstance().executeTransaction(realm1 -> {
                    contact.setReminderDuration(2);
                    contact.setReminderEnabled(true);
                });
                break;
            case "3 weeks have passed":
                realmService.getInstance().executeTransaction(realm1 -> {
                    contact.setReminderDuration(3);
                    contact.setReminderEnabled(true);
                });
                break;
            case "CANCEL":
                realmService.getInstance().executeTransaction(realm1 -> {
                });
                break;
        }
    }

    private void setClickListenersWhenInPortraitMode() {
        int value = getActivity().getResources().getConfiguration().orientation;

        if (value == Configuration.ORIENTATION_PORTRAIT) {
            FontHelper fontHelper = new FontHelper();
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
        realmService.getInstance().executeTransaction(
                realm1 -> contact.setContactImage(contactImgUri.toString()));
    }

    public void setBackgroundImage(Intent data, PicassoHelper ph) {
        Uri bgImgUri = data.getData();
        ph.loadImageFromUri(bgImgUri, backgroundImageIV);
        realmService.getInstance().executeTransaction(
                realm1 -> contact.setBackgroundImage(bgImgUri.toString()));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realmService.closeRealm();
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Toast.makeText(getActivity(), "Reminders " + (isChecked ? "enabled" : "disabled"),
                Toast.LENGTH_SHORT).show();
        if (isChecked) {
            realmService.toggleReminder(contact, true);
            dateSpinner.setVisibility(View.VISIBLE);
        } else {
            realmService.toggleReminder(contact, false);
            dateSpinner.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        this.dialog = initBottomSheetDialog(bottomSheetMenu);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        this.dialog = initBottomSheetDialog(bottomSheetMenu);
    }
}


