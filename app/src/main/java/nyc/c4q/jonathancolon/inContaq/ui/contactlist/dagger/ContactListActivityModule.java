package nyc.c4q.jonathancolon.inContaq.ui.contactlist.dagger;


import nyc.c4q.jonathancolon.inContaq.ui.contactlist.ContactListActivity;

public class ContactListActivityModule {
    private final ContactListActivity activity;

    public ContactListActivityModule(ContactListActivity activity) {
        this.activity = activity;
    }
}
