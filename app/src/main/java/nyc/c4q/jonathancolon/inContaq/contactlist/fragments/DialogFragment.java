package nyc.c4q.jonathancolon.inContaq.contactlist.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.Contact;
import nyc.c4q.jonathancolon.inContaq.contactlist.PicassoHelper;

import static android.app.Activity.RESULT_OK;
import static nyc.c4q.jonathancolon.inContaq.utlities.sqlite.SqlHelper.saveToDatabase;

public class DialogFragment extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    ImageButton takePicture, choosePicture;
    Contact contact;
    ImageView contactImageIV, backgroundImageIV;

    private static final int RESULT_LOAD_BACKGROUND_IMG = 2;
    private static final int RESULT_LOAD_CONTACT_IMG = 1;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_dialog, container, false);
        getDialog().setTitle("Choose Profile Picture");

        takePicture = (ImageButton) rootView.findViewById(R.id.take_picture);
        choosePicture = (ImageButton) rootView.findViewById(R.id.choose_picture);

        takePicture.setOnClickListener(this);
        choosePicture.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.take_picture:
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, RESULT_LOAD_CONTACT_IMG);
                break;

            case R.id.choose_picture:
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, RESULT_LOAD_CONTACT_IMG);
                break;

            default:
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == Activity.RESULT_OK) {

                PicassoHelper ph = new PicassoHelper(getActivity());
                switch (requestCode) {

                    case 1:
                        Uri contactUri = data.getData();
                        ph.loadImageFromUri(contactUri, contactImageIV);
                        contact.setContactImage(contactUri.toString());
                        saveToDatabase(contact, getActivity());
                        break;

                    case 2:
                        Uri backgroundUri = data.getData();
                        ph.loadImageFromUri(backgroundUri, backgroundImageIV);
                        contact.setBackgroundImage(backgroundUri.toString());
                        saveToDatabase(contact, getActivity());
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

        loadImages();
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

}