package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactinfo;


import android.app.Activity;
import android.content.Context;
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
import nyc.c4q.jonathancolon.inContaq.common.dagger.Injector;
import nyc.c4q.jonathancolon.inContaq.database.RealmService;
import nyc.c4q.jonathancolon.inContaq.model.ContactModel;
import nyc.c4q.jonathancolon.inContaq.utlities.AnimationHelper;
import nyc.c4q.jonathancolon.inContaq.utlities.FontUtils;
import nyc.c4q.jonathancolon.inContaq.utlities.PicassoUtils;

import static android.content.DialogInterface.OnCancelListener;
import static android.content.DialogInterface.OnDismissListener;
import static android.view.View.*;
import static android.widget.AdapterView.OnItemSelectedListener;
import static android.widget.CompoundButton.OnCheckedChangeListener;
import static nyc.c4q.jonathancolon.inContaq.ui.contactlist.ContactListActivity.CONTACT_KEY;

public class ContactInfoFragment extends Fragment implements AlertDialogCallback<Integer>,
        OnItemSelectedListener, OnCheckedChangeListener,
        OnCancelListener, OnDismissListener {

    private static final int RESULT_LOAD_BACKGROUND_IMG = 2;
    private static final int RESULT_LOAD_CONTACT_IMG = 1;
    private static final String ONE_WEEK = "1 week has passed";
    private static final String TWO_WEEKS = "2 weeks have passed";
    private static final String THREE_WEEKS = "3 weeks have passed";
    private static final String CANCEL = "CANCEL";

    @Inject
    Context context;
    @Inject
    FontUtils fontUtils;
    @Inject
    PicassoUtils pUtils;
    @Inject
    RealmService realmService;

    private ContactModel contactModel;
    private TextView mobile, email, displayName, editOption;
    private ImageView contactImageIV, backgroundImageIV, sendMessageIV;
    private PicassoUtils picasso;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_info, container, false);


        Injector.getApplicationComponent().inject(this);
        long contactId = getActivity().getIntent().getLongExtra(CONTACT_KEY, -1);
        contactModel = realmService.getByRealmID(contactId);
        anim = new AnimationHelper(ContactInfoFragment.this.getActivity());
        picasso = new PicassoUtils(context);
        appLauncher = new MessengerAppLauncher(getActivity(), contactModel.getMobileNumber());

        isEditTextEnabled = false;
        initViews(view);
        setClickListeners();
        displayContactInfo(contactModel);
        return view;
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

        sendMessageIV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    private void initSwitch(View view) {
        reminderSwitch = (Switch) view.findViewById(R.id.reminder_switch);
        if (reminderSwitch != null) {
            if (contactModel.isReminderEnabled()) {
                dateSpinner.setVisibility(VISIBLE);
            }
            reminderSwitch.setChecked(contactModel.isReminderEnabled());
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
        dateSpinner.setSelection(contactModel.getReminderDuration());
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
            ContactInfoFragment.this.alertDialogCallback(selection, contactModel);
            anim.exitFab(saveButton);
            saveButton.setVisibility(GONE);
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
    public void alertDialogCallback(Integer ret, ContactModel contactModel) {
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
        saveButton.setVisibility(VISIBLE);
        anim.enterFab(saveButton);
    }

    private void saveChanges() {
        String email = editEmail.getText().toString();
        String address = editAddress.getText().toString();

        realmService.getInstance().executeTransaction(realm1 -> {
            contactModel.setEmail(email);
            contactModel.setAddress(address);
            contactModel.setMobileNumber(editMobile.getText().toString());
        });

        disableEditText();
    }

    @Override
    public void onResume() {
        super.onResume();
        displayContactInfo(contactModel);
    }

    private void displayContactInfo(ContactModel contactModel) {
        String nameValue = contactModel.getFirstName() + " " + contactModel.getLastName();
        displayName.setText(nameValue);
        editEmail.setText(contactModel.getEmail());
        editAddress.setText(contactModel.getAddress());
        editMobile.setText(contactModel.getMobileNumber());

        loadImages();
        showMobile();
        showEmail();
        showAddress();
    }

    private void loadImages() {
        if (contactModel.getBackgroundImage() != null) {
            pUtils.loadImageFromString(contactModel.getBackgroundImage(), backgroundImageIV);
        }
        if (contactModel.getContactImage() != null) {
            pUtils.loadImageFromString(contactModel.getContactImage(), contactImageIV);
        }
    }

    synchronized private void showMobile() {
        if (isEditTextEnabled) {
            mobile.setVisibility(VISIBLE);

        }
        if (contactModel.getMobileNumber() != null || Objects.equals(contactModel.getMobileNumber(), "")) {
            mobile.setVisibility(VISIBLE);
        }
    }

    synchronized private void showEmail() {
        if (isEditTextEnabled) {
            email.setVisibility(VISIBLE);
        }
    }

    synchronized private void showAddress() {
        if (isEditTextEnabled) {
            mobile.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (String.valueOf(parent.getItemAtPosition(position))) {
            case ONE_WEEK:
                realmService.getInstance().executeTransaction(realm1 -> {
                    contactModel.setReminderDuration(1);
                    contactModel.setReminderEnabled(true);
                });
                break;
            case TWO_WEEKS:
                realmService.getInstance().executeTransaction(realm1 -> {
                    contactModel.setReminderDuration(2);
                    contactModel.setReminderEnabled(true);
                });
                break;
            case THREE_WEEKS:
                realmService.getInstance().executeTransaction(realm1 -> {
                    contactModel.setReminderDuration(3);
                    contactModel.setReminderEnabled(true);
                });
                break;
            case CANCEL:
                realmService.getInstance().executeTransaction(realm1 -> {
                });
                break;
        }
    }

    private void setClickListenersWhenInPortraitMode() {
        int value = getActivity().getResources().getConfiguration().orientation;

        if (value == Configuration.ORIENTATION_PORTRAIT) {
            fontUtils.applyFont(displayName);
            displayContactInfo(contactModel);

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
                    case RESULT_LOAD_CONTACT_IMG:
                        updateContactImage(data, picasso);
                        break;
                    case RESULT_LOAD_BACKGROUND_IMG:
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

    private void updateContactImage(Intent data, PicassoUtils picasso) {
        Uri contactImgUri = data.getData();
        picasso.loadImageFromUri(contactImgUri, contactImageIV);
        realmService.getInstance().executeTransaction(
                realm1 -> contactModel.setContactImage(contactImgUri.toString()));
    }

    public void setBackgroundImage(Intent data, PicassoUtils ph) {
        Uri bgImgUri = data.getData();
        ph.loadImageFromUri(bgImgUri, backgroundImageIV);
        realmService.getInstance().executeTransaction(
                realm1 -> contactModel.setBackgroundImage(bgImgUri.toString()));
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
            realmService.toggleReminder(contactModel, true);
            dateSpinner.setVisibility(VISIBLE);
        } else {
            realmService.toggleReminder(contactModel, false);
            dateSpinner.setVisibility(INVISIBLE);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        this.dialog = initBottomSheetDialog(bottomSheetMenu);
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

    @Override
    public void onDismiss(DialogInterface dialog) {
        this.dialog = initBottomSheetDialog(bottomSheetMenu);
    }
}


