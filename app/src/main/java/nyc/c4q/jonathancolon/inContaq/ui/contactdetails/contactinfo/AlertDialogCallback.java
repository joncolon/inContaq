package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactinfo;


import java.util.concurrent.ExecutionException;

import nyc.c4q.jonathancolon.inContaq.model.Contact;

public interface AlertDialogCallback<T> {
    void alertDialogCallback(Integer ret, Contact contact) throws ExecutionException, InterruptedException;
}
