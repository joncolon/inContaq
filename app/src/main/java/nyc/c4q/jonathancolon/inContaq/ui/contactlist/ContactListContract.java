package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import io.realm.RealmResults;
import nyc.c4q.jonathancolon.inContaq.common.base.BaseView;
import nyc.c4q.jonathancolon.inContaq.model.ContactModel;


public class ContactListContract {

    interface View extends BaseView {

        void selectContact();

        void addFirstContactPrompt();

        void showDeleteContactDialog(ContactModel contactModel);

        void checkService();

        void checkPermissions();

        void initializeRecyclerView();

        void requestPermissions();

        void navigateToContactDetailsActivity(ContactModel contactModel);

        void refreshContactList(RealmResults<ContactModel> list);

        void preLoadContactListImages();

        void showNoMobileNumberError();

    }

    interface Presenter {
        RealmResults<ContactModel> retrieveContacts();

        void onAddContactClicked();

        void onContactClicked(ContactModel contactModel);

        void addContactToDatabase(ContactModel contactModel);

        void onContactLongClicked(ContactModel contactModel);

        void onaddFirstContactPromptClicked();
    }
}
