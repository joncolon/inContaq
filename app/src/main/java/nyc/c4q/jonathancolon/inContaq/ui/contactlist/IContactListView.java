package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import java.util.concurrent.ExecutionException;

import io.realm.RealmResults;
import nyc.c4q.jonathancolon.inContaq.model.Contact;

/**
 * Created by jonathancolon on 6/9/17.
 */

interface IContactListView {
    void onContactsLoadedSuccess(RealmResults<Contact> list) throws ExecutionException, InterruptedException;
    void onContactsLoadedFailure(String string);
    void launchActivity(Contact contact);
}
