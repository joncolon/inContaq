package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import io.realm.RealmResults;
import nyc.c4q.jonathancolon.inContaq.common.base.BaseView;
import nyc.c4q.jonathancolon.inContaq.model.Contact;


public class ContactListContract {

    interface View extends BaseView {

        void selectContact();

        void addFirstContactPrompt();

        void showDeleteContactDialog(Contact contact);

        void checkService();

        void checkPermissions();

        void initializeRecyclerView();

        void requestPermissions();

        void navigateToContactDetailsActivity(Contact contact);

        void refreshContactList(RealmResults<Contact> list);

        void preLoadContactListImages();

        void showNoMobileNumberError();

    }

    interface Presenter {
        RealmResults<Contact> retrieveContacts();

        void onAddContactClicked();

        void onContactClicked(Contact contact);

        void addContactToDatabase(Contact contact);

        void onContactLongClicked(Contact contact);

        void onaddFirstContactPromptClicked();
    }
}
