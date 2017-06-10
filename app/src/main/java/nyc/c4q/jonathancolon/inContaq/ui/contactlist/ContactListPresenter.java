package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import io.realm.RealmResults;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.utlities.RealmService;


class ContactListPresenter implements IContactListPresenter,
        OnContactInteractorFinishedListener {

    @Inject
    RealmService realmService;

    private IContactListView view;
    private ContactListInteractor interactor;

    ContactListPresenter(IContactListView view) {
        this.interactor = new ContactListInteractor(this);
        this.view = view;
    }

    @Override
    public void retrieveContacts() throws ExecutionException, InterruptedException {
        interactor.retrieveContactList();
    }

    @Override
    public void onContactClicked(int id) {
    }

    @Override
    public void onQuerySuccess(RealmResults<Contact> contactList) throws ExecutionException,
            InterruptedException {
        view.onContactsLoadedSuccess(contactList);
    }

    @Override
    public void onQueryFailure(String error) {
    }

    @Override
    public void setView(Object view) {
    }

    @Override
    public void clearView() {
    }

}
