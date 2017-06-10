package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import java.util.concurrent.ExecutionException;

import io.realm.RealmResults;
import nyc.c4q.jonathancolon.inContaq.model.Contact;

/**
 * Created by jonathancolon on 6/9/17.
 */

interface OnContactInteractorFinishedListener {
    void onQuerySuccess(RealmResults<Contact> contactList) throws ExecutionException, InterruptedException;
    void onQueryFailure(String error);
}
