package nyc.c4q.jonathancolon.inContaq.data;

/**
 * Created by jonathancolon on 5/10/17.
 */

public class OnSmsListLoaded {
    public String mMessage;


    public OnSmsListLoaded(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}
