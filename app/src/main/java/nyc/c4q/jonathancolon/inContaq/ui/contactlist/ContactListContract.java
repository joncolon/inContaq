package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import io.realm.RealmResults;
import nyc.c4q.jonathancolon.inContaq.model.Contact;
import nyc.c4q.jonathancolon.inContaq.ui.base.listeners.BaseView;

/**
 * Created by jonathancolon on 6/10/17.
 */

public class ContactListContract {

    interface View extends BaseView {

        void initializeMaterialTapPrompt(RealmResults<Contact> contactList);

        void checkService();

        void checkPermissions();

        void initializeContactList();

        void requestPermissions();

        void refreshContactList(RealmResults<Contact> list);

        void preLoadContactListImages();
;    }

    interface Presenter {
        RealmResults<Contact> retrieveContacts();
    }
}
