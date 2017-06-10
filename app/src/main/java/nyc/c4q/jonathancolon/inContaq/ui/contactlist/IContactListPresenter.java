package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

import java.util.concurrent.ExecutionException;

/**
 * Created by jonathancolon on 6/9/17.
 */

interface IContactListPresenter extends IBasePresenter {
    void retrieveContacts() throws ExecutionException, InterruptedException;
    void onContactClicked(int id);
}
