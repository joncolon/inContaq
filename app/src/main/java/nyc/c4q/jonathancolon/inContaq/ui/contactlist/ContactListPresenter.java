package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import android.os.Bundle;

import javax.inject.Inject;

import io.realm.RealmResults;
import nyc.c4q.jonathancolon.inContaq.database.RealmService;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.common.base.Presenter;
import nyc.c4q.jonathancolon.inContaq.utlities.SharedPrefsUtils;

import static nyc.c4q.jonathancolon.inContaq.utlities.ObjectUtils.isEmpty;


public class ContactListPresenter extends Presenter<ContactListContract.View> implements
        ContactListContract.Presenter {

    private RealmService realmService;
    private SharedPrefsUtils prefs;

    @Inject
    ContactListPresenter(RealmService realmService, SharedPrefsUtils prefs) {
        this.realmService = realmService;
        this.prefs = prefs;
    }

    @Override
    public void initialize(Bundle extras) {
        super.initialize(extras);
        getView().checkPermissions();
        getView().initializeRecyclerView();
        getView().preLoadContactListImages();
        getView().checkService();
        checkIfIsReturningUser();
    }

    @Override
    public RealmResults<Contact> retrieveContacts() {
        return realmService.fetchAllContacts();
    }

    @Override
    public void onAddContactClicked() {
        getView().selectContact();
    }

    @Override
    public void onContactClicked(Contact contact) {
        if (!isEmpty(contact.getMobileNumber())) {
            getView().navigateToContactDetailsActivity(contact);
        } else {
            getView().showNoMobileNumberError();
        }
    }

    @Override
    public void addContactToDatabase(Contact contact) {
        realmService.addContactToRealmDB(contact);
    }

    @Override
    public void onContactLongClicked(Contact contact) {
        getView().showDeleteContactDialog(contact);
    }

    @Override
    public void onaddFirstContactPromptClicked() {
        prefs.setPreviouslyOpenedApp(true);
    }

    private void checkIfIsReturningUser(){
        if (!prefs.hasPreviouslyOpenedApp()){
            getView().addFirstContactPrompt();
        }
    }
}
