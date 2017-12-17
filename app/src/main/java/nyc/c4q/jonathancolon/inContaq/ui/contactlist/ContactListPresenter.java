package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import android.os.Bundle;

import javax.inject.Inject;

import io.realm.RealmResults;
import nyc.c4q.jonathancolon.inContaq.database.RealmService;
import nyc.c4q.jonathancolon.inContaq.model.ContactModel;
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
    public RealmResults<ContactModel> retrieveContacts() {
        return realmService.fetchAllContacts();
    }

    @Override
    public void onAddContactClicked() {
        getView().selectContact();
    }

    @Override
    public void onContactClicked(ContactModel contactModel) {
        if (!isEmpty(contactModel.getMobileNumber())) {
            getView().navigateToContactDetailsActivity(contactModel);
        } else {
            getView().showNoMobileNumberError();
        }
    }

    @Override
    public void addContactToDatabase(ContactModel contactModel) {
        realmService.addContactToRealmDB(contactModel);
    }

    @Override
    public void onContactLongClicked(ContactModel contactModel) {
        getView().showDeleteContactDialog(contactModel);
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
