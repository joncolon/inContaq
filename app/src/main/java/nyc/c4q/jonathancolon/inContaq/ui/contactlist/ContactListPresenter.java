package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import android.os.Bundle;

import javax.inject.Inject;

import io.realm.RealmResults;
import nyc.c4q.jonathancolon.inContaq.db.RealmService;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.ui.base.Presenter;


public class ContactListPresenter extends Presenter<ContactListContract.View> implements
        ContactListContract.Presenter {

    @Inject
    public RealmService realmService;

    @Inject
    ContactListPresenter() {
    }

    @Override
    public void initialize(Bundle extras) {
        super.initialize(extras);
        getView().checkPermissions();
        getView().initializeContactList();
        getView().preLoadContactListImages();
        getView().checkService();
        getView().initializeMaterialTapPrompt(retrieveContacts());
    }

    @Override
    public RealmResults<Contact> retrieveContacts() {
        return realmService.fetchAllContacts();
    }
}
