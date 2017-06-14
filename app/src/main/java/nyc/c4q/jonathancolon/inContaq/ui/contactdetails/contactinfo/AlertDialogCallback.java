package nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactinfo;


import java.util.concurrent.ExecutionException;

public interface AlertDialogCallback<T> {
    void alertDialogCallback(T ret) throws ExecutionException, InterruptedException;
}
