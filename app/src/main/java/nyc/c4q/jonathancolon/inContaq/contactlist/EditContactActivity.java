package nyc.c4q.jonathancolon.inContaq.contactlist;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import nyc.c4q.jonathancolon.inContaq.R;

public class EditContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        Activity EditContactActivity = this;
        ViewPager viewPager;


        if (savedInstanceState == null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.activity_edit_contact, new EditContactFragment());
            fragmentTransaction.commit();
        }



}

}
