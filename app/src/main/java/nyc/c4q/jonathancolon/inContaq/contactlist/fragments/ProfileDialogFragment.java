package nyc.c4q.jonathancolon.inContaq.contactlist.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.Contact;
import nyc.c4q.jonathancolon.inContaq.contactlist.PicassoHelper;
import nyc.c4q.jonathancolon.inContaq.contactlist.activities.ContactListActivity;
import static nyc.c4q.jonathancolon.inContaq.utlities.sqlite.SqlHelper.saveToDatabase;

public class ProfileDialogFragment extends DialogFragment implements View.OnClickListener {

    interface Callback {
        void onFinished(Uri contactUri);
    }

    Context context;
    ImageButton takePicture, choosePicture;
    Contact contact;
    ImageView contactImageIV, backgroundImageIV;
    private List<Callback> callbacks = new ArrayList<>();

    private static final int RESULT_LOAD_BACKGROUND_IMG = 2;
    private static final int RESULT_LOAD_CONTACT_IMG = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_dialog, container, false);
        getDialog().setTitle("Choose Profile Picture");

        contact = Parcels.unwrap(getActivity().getIntent().
                getParcelableExtra(ContactListActivity.PARCELLED_CONTACT));

        contactImageIV = (ImageView) rootView.findViewById(R.id.contact_image);

        takePicture = (ImageButton) rootView.findViewById(R.id.take_picture);
        choosePicture = (ImageButton) rootView.findViewById(R.id.choose_picture);

        takePicture.setOnClickListener(this);
        choosePicture.setOnClickListener(this);

        return rootView;
    }

    public ProfileDialogFragment(){
    }

    public void addCallback(Callback callback) {
        callbacks.add(callback);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
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

                switch (requestCode) {

                    case 1:
                        Uri contactUri = data.getData();
                        for (Callback callback : callbacks) {
                            callback.onFinished(contactUri);
                        }

                        break;

                    case 2:
                        Uri backgroundUri = data.getData();
                        for (Callback callback : callbacks) {
                            callback.onFinished(backgroundUri);
                        }
                        break;
                }

            } else {
                Toast.makeText(context, R.string.error_message_photo_not_selected,
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, R.string.error_message_general, Toast.LENGTH_LONG)
                    .show();
        }
    }
}