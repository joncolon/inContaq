package nyc.c4q.jonathancolon.inContaq.contactlist.fragments;


import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fontometrics.Fontometrics;

import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.Contact;
import nyc.c4q.jonathancolon.inContaq.contactlist.activities.ContactListActivity;
import nyc.c4q.jonathancolon.inContaq.contactlist.adapters.SmsAdapter;
import nyc.c4q.jonathancolon.inContaq.sqlite.ContactDatabaseHelper;
import nyc.c4q.jonathancolon.inContaq.utilities.bitmap.LoadScaledBitmapWorkerTask;
import nyc.c4q.jonathancolon.inContaq.utilities.bitmap.SetContactImageWorkerTask;
import nyc.c4q.jonathancolon.inContaq.utilities.sms.Sms;
import nyc.c4q.jonathancolon.inContaq.utilities.sms.SmsHelper;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class ContactSmsFragment extends Fragment implements SmsAdapter.Listener {

    private static final int RESULT_LOAD_BACKGROUND_IMG = 2;
    private static final int RESULT_LOAD_CONTACT_IMG = 1;
    private static TextView contactName;
    private static ImageView contactImageIV, backgroundImageIV;
    private static Contact contact;
    private String TAG = "SET TEXT REQUEST: ";
    private SmsAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Sms> lstSms;

    public ContactSmsFragment() {
        //required empty public constructor
    }

    public static ContactSmsFragment newInstance() {
        ContactSmsFragment fragment = new ContactSmsFragment();
        Bundle b = new Bundle();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_contact_sms, container, false);
        contact = Parcels.unwrap(getActivity().getIntent().getParcelableExtra(ContactListActivity.PARCELLED_CONTACT));

        initViews(view);
        displayContactInfo(contact);
        lstSms = SmsHelper.getAllSms(getActivity(), contact);
        setupRecyclerView(contact);
        refreshRecyclerView();
        scrollListToBottom();
        showRecyclerView();

        return view;
    }

    private void initViews(View view) {
        contactName = (TextView) view.findViewById(R.id.name);
        contactImageIV = (ImageView) view.findViewById(R.id.contact_img);
        backgroundImageIV = (ImageView) view.findViewById(R.id.background_image);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        contactName.setTypeface(Fontometrics.amatic_bold(getActivity()));

        contactImageIV.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_LOAD_CONTACT_IMG);
        });
        if (backgroundImageIV != null) {
            backgroundImageIV.setOnClickListener(v -> {

                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(galleryIntent, RESULT_LOAD_BACKGROUND_IMG);
            });
        }
    }

    synchronized private void displayContactInfo(Contact contact) {
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

    private void setupRecyclerView(Contact contact) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new SmsAdapter(this, contact));
    }

    synchronized public void refreshRecyclerView() {
        adapter = (SmsAdapter) recyclerView.getAdapter();

        Collections.sort(lstSms);
        adapter.setData(lstSms);
        Log.d(TAG, "RefreshRV : " + lstSms.size());
        ;
    }

    synchronized private void scrollListToBottom() {
        recyclerView.post(() -> recyclerView.scrollToPosition(adapter.getItemCount() - 1));
    }

    private void setContactImage(byte[] bytes, ImageView imageView) {
        SetContactImageWorkerTask task = new SetContactImageWorkerTask(imageView);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, bytes);
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

                        // Convert bitmap to byte[]
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        contactBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                        //SAVE TO DATABASE
                        ContactDatabaseHelper dbHelper = ContactDatabaseHelper.getInstance(getActivity());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        byte imageInByte[] = stream.toByteArray();
                        contact.setContactImage(imageInByte);
                        cupboard().withDatabase(db).put(contact);
                        break;

                    case 2:
                        Uri selectedBackgroundImage = data.getData();

                        loadScaledBitmap(selectedBackgroundImage);
                        Bitmap backgroundBitmap = loadScaledBitmap(selectedBackgroundImage);
                        backgroundImageIV.setImageBitmap(backgroundBitmap);

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
                Toast.makeText(getActivity(), R.string.error_message_photo_not_selected,
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "onActivityResult: " + e.toString());

            Toast.makeText(getActivity(), R.string.error_message_general, Toast.LENGTH_LONG)
                    .show();
        }
    }

    private Bitmap loadScaledBitmap(Uri uri) throws ExecutionException, InterruptedException {
        LoadScaledBitmapWorkerTask task = new LoadScaledBitmapWorkerTask(uri, getActivity());
        Bitmap bitmap = task.execute(uri).get();
        return bitmap;
    }

    synchronized private void showRecyclerView(){
        Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        recyclerView.startAnimation(fadeIn);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onContactClicked(Sms sms) {
        //TODO add functionality
    }

    @Override
    public void onContactLongClicked(Sms sms) {
        //TODO add functionality
    }
}
