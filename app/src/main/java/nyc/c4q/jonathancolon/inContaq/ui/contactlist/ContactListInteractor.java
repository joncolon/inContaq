package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import io.realm.RealmResults;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.utlities.Injector;
import nyc.c4q.jonathancolon.inContaq.utlities.RealmService;

/**
 * Created by jonathancolon on 6/9/17.
 */

public class ContactListInteractor {

    private OnContactInteractorFinishedListener listener;

    @Inject
    RealmService realmService;

    ContactListInteractor(OnContactInteractorFinishedListener listener) {
        this.listener = listener;
        Injector.getApplicationComponent().inject(this);
    }

    void retrieveContactList() throws ExecutionException, InterruptedException {
        RealmResults<Contact> contactList = realmService.fetchAllContacts();
        listener.onQuerySuccess(contactList);
    }

    void retrieveSingleContact(){
    }

    void handleError(){
        listener.onQueryFailure("error");
    }

}

