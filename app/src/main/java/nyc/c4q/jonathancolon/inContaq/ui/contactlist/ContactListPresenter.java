package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import android.os.Bundle;

import javax.inject.Inject;

import io.realm.RealmResults;
import nyc.c4q.jonathancolon.inContaq.database.RealmService;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.common.base.Presenter;


public class ContactListPresenter extends Presenter<ContactListContract.View> implements
        ContactListContract.Presenter {

    private RealmService realmService;

    @Inject
    ContactListPresenter(RealmService realmService) {
        this.realmService = realmService;
    }

    @Override
    public void initialize(Bundle extras) {
        super.initialize(extras);
        getView().checkPermissions();
        getView().initializeRecyclerView();
        getView().preLoadContactListImages();
        getView().checkService();
        getView().initializeMaterialTapPrompt(retrieveContacts());
    }

    @Override
    public RealmResults<Contact> retrieveContacts() {
        return realmService.fetchAllContacts();
    }
}
