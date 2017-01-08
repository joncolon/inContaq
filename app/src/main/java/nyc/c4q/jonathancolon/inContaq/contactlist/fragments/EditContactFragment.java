package nyc.c4q.jonathancolon.inContaq.contactlist.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fontometrics.Fontometrics;

import org.parceler.Parcels;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.Contact;
import nyc.c4q.jonathancolon.inContaq.utilities.bitmap.SetContactImageWorkerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditContactFragment extends Fragment {
    Contact contact;
    private  TextView name, mobilePhone, email, polaroidName;
    private  ImageView contactImageIV, backgroundImageIV;


    public static EditContactFragment newInstance(Contact contact) {

        EditContactFragment fragment = new EditContactFragment();
        Bundle b = new Bundle();

        fragment.setArguments(b);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_info, container, false);

        contact = Parcels.unwrap(getActivity().getIntent().getParcelableExtra("Parcelled Contact"));
        name = (TextView) view.findViewById(R.id.name);
        mobilePhone = (TextView) view.findViewById(R.id.mobile_phone);
        email = (TextView) view.findViewById(R.id.email);
        polaroidName = (TextView) view.findViewById(R.id.contact_name);
        contactImageIV = (ImageView) view.findViewById(R.id.contact_img);


//        displayContactInfo(contact);

        return view;
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

    private void displayContactInfo(Contact contact) {
        String nameValue = contact.getFirstName() + " " + contact.getLastName();

        //Asynchronously load bitmaps from Contact object
        if (contact.getBackgroundImage() != null) {
            setContactImage(contact.getBackgroundImage(), backgroundImageIV);
        }
        if (contact.getContactImage() != null) {
            setContactImage(contact.getContactImage(), contactImageIV);

        }
        if (contact.getCellPhoneNumber() != null){
            mobilePhone.setVisibility(View.VISIBLE);
            mobilePhone.setText(contact.getCellPhoneNumber());
        }
        if (contact.getEmail() != null){
            email.setVisibility(View.VISIBLE);
            email.setText(contact.getCellPhoneNumber());
        }
        polaroidName.setText(nameValue);
        name.setText(nameValue);
    }

    private void setContactImage(byte[] bytes, ImageView imageView){
        SetContactImageWorkerTask task = new SetContactImageWorkerTask(imageView);
        task.execute(bytes);
    }

    private String parsePhoneNumber(String input){
        return input.replaceAll("[()\\s-]+?!@#$%^&*()_", "");
    }
}
