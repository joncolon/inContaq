package nyc.c4q.jonathancolon.inContaq.contactlist.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import nyc.c4q.jonathancolon.inContaq.R;

public class DialogFragment extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    ImageButton takePicture, choosePicture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dialog, container, false);
        getDialog().setTitle("Simple Dialog");

        takePicture = (ImageButton) rootView.findViewById(R.id.take_picture);
        choosePicture = (ImageButton) rootView.findViewById(R.id.choose_picture);

        takePicture.setOnClickListener(this);
        choosePicture.setOnClickListener(this);

        return rootView;
    }

    public void initViews(View view) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.take_picture:
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
                break;

            case R.id.choose_picture:
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);

                break;

            default:
                break;
        }
    }
}