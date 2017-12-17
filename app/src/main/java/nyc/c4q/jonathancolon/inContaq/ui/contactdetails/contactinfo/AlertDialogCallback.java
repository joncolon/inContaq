package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactinfo;


import java.util.concurrent.ExecutionException;

import nyc.c4q.jonathancolon.inContaq.model.ContactModel;

public interface AlertDialogCallback<T> {
    void alertDialogCallback(Integer ret, ContactModel contactModel) throws ExecutionException, InterruptedException;
}
