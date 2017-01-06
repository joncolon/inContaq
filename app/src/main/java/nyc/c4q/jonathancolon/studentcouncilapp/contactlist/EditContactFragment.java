package nyc.c4q.jonathancolon.studentcouncilapp.contactlist;


import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import nyc.c4q.jonathancolon.studentcouncilapp.R;
import nyc.c4q.jonathancolon.studentcouncilapp.utilities.sms.Sms;
import nyc.c4q.jonathancolon.studentcouncilapp.utilities.sms.SmsAdapter;
import nyc.c4q.jonathancolon.studentcouncilapp.utilities.sms.SmsHelper;
import nyc.c4q.jonathancolon.studentcouncilapp.sqlite.ContactDatabaseHelper;
import nyc.c4q.jonathancolon.studentcouncilapp.utilities.bitmap.LoadScaledBitmapWorkerTask;
import nyc.c4q.jonathancolon.studentcouncilapp.utilities.bitmap.SetContactImageWorkerTask;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditContactFragment extends Fragment implements SmsAdapter.Listener{
    private static final int RESULT_LOAD_BACKGROUND_IMG = 2;
    String TAG = "SET TEXT REQUEST: ";
    private static TextView contactName, smsList;
    private static int RESULT_LOAD_CONTACT_IMG = 1;
    private static ImageView contactImageIV, backgroundImageIV;
    private static Contact contact;
    SmsAdapter adapter;
    ViewPager viewpager;

    private ArrayList<Sms> lstSms;

    private int mPage;
    private SQLiteDatabase db;
    private RecyclerView recyclerView;
    private RelativeLayout relativelayout;
    public static final String ARG_PAGE = "ARG_PAGE";


    public EditContactFragment() {
        //required empty public constructor
    }


    public static EditContactFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        EditContactFragment fragment = new EditContactFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View NotepadLayoutFragment = inflater.inflate(R.layout.fragment_edit_contact, container, false);
        lstSms = SmsHelper.getAllSms(getActivity());
        contact = Parcels.unwrap(getActivity().getIntent().getParcelableExtra("Parcelled Contact"));

        contactName = (TextView) NotepadLayoutFragment.findViewById(R.id.contact_name);
        contactImageIV = (ImageView) NotepadLayoutFragment.findViewById(R.id.contact_img);
        backgroundImageIV = (ImageView) NotepadLayoutFragment.findViewById(R.id.background_image);
        recyclerView = (RecyclerView) NotepadLayoutFragment.findViewById(R.id.recycler_view);
        displayContactInfo(contact);

        setupRecyclerView();
        refreshRecyclerView();
        scrollListToBottom();



        contactImageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_CONTACT_IMG);
            }
        });

        if (backgroundImageIV != null) {
            backgroundImageIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(galleryIntent, RESULT_LOAD_BACKGROUND_IMG);
                }
            });
        }
        return NotepadLayoutFragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (resultCode == Activity.RESULT_OK) {

                switch (requestCode) {
                    case 1:
                        // Get the Image from data
                        Uri selectedContactImage = data.getData();


                        // Asynchronously resize and load bitmap
                        loadScaledBitmap(selectedContactImage);
                        Bitmap contactBitmap = loadScaledBitmap(selectedContactImage);
                        contactImageIV.setImageBitmap(contactBitmap);

                        //SAVE TO DATABASE
                        ContactDatabaseHelper dbHelper = ContactDatabaseHelper.getInstance(getActivity());
                        db = dbHelper.getWritableDatabase();

                        // Convert bitmap to byte[]
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        contactBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte imageInByte[] = stream.toByteArray();
                        contact.setContactImage(imageInByte);
                        cupboard().withDatabase(db).put(contact);
                        break;

                    case 2:

                        Uri selectedBackgroundImage = data.getData();

                        loadScaledBitmap(selectedBackgroundImage);
                        Bitmap backgroundBitmap = loadScaledBitmap(selectedBackgroundImage);
                        backgroundImageIV.setImageBitmap(backgroundBitmap);

                        //SAVE TO DATABASE
                        ByteArrayOutputStream streamBG = new ByteArrayOutputStream();
                        backgroundBitmap.compress(Bitmap.CompressFormat.JPEG, 100, streamBG);
                        byte imageInByteBG[] = streamBG.toByteArray();

                        ContactDatabaseHelper dbHelperBG = ContactDatabaseHelper.getInstance(getActivity());
                        db = dbHelperBG.getWritableDatabase();
                        contact.setBackgroundImage(imageInByteBG);
                        cupboard().withDatabase(db).put(contact);
                        break;
                }
            } else {
                Toast.makeText(getActivity(), "Photo not selected", Toast.LENGTH_LONG)
                        .show();
            }
        } catch (Exception e) {
            Log.e(TAG, "onActivityResult: " + e.toString());

            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void displayContactInfo(Contact contact) {
        String nameValue = contact.getFirstName() + " " + contact.getLastName();

        //Asynchronously load bitmaps from Contact object
        if (contact.getBackgroundImage() != null) {
            setContactImage(contact.getBackgroundImage(), backgroundImageIV);
        }
        if (contact.getContactImage() != null) {
            setContactImage(contact.getContactImage(), contactImageIV);
        }
        contactName.setText(nameValue);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new SmsAdapter(this));
    }

    public void refreshRecyclerView() {
        adapter = (SmsAdapter) recyclerView.getAdapter();

        Collections.sort(lstSms);
        adapter.setData(lstSms);
        Log.d(TAG, "RefreshRV : " + lstSms.size());
        ;
    }

    @Override
    public void onContactClicked(Sms sms) {
        //TODO add functionality
    }

    @Override
    public void onContactLongClicked(Sms sms) {
        //TODO add functionality
    }

    private void scrollListToBottom() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);

            }
        });
    }

    private Bitmap loadScaledBitmap(Uri uri) throws ExecutionException, InterruptedException {
        LoadScaledBitmapWorkerTask task = new LoadScaledBitmapWorkerTask(uri, getActivity());
        Bitmap bitmap = task.execute(uri).get();
        return bitmap;
    }

    private void setContactImage(byte[] bytes, ImageView imageView){
        SetContactImageWorkerTask task = new SetContactImageWorkerTask(imageView);
        task.execute(bytes);
    }

    private PagerAdapter buildAdapter() {
        return(new SampleFragmentPagerAdapter(getChildFragmentManager(), getActivity()));
    }
}
