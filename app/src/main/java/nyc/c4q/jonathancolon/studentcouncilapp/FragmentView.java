package nyc.c4q.jonathancolon.studentcouncilapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class FragmentView extends AppCompatActivity {
    public static final String CONTACT = "extra.contact.name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_view);
        if (savedInstanceState == null) {
            String contact = getIntent().getStringExtra(CONTACT);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.activity_fragment_view, new ContactNoteEditor());
            fragmentTransaction.commit();
        }
    }
}
