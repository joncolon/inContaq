package nyc.c4q.jonathancolon.studentcouncilapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    public static final String CONTACT_NAME_EXTRA = "Contact Name";
    public static final String CONTACT_NOTE_EXTRA = "Contact Note";
    public static final String CONTACT_IMAGE_EXTRA = "Contact Image";

    public static List<Contacts> contactsList = Arrays.asList(
            new Contacts("Jonathan"),
            new Contacts("Ashique"),
            new Contacts("Rafael"),
            new Contacts("Ridita"),
            new Contacts("Josiel")
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ContactsAdapter(contactsList, this));
    }
}
