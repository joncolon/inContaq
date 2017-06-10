package nyc.c4q.jonathancolon.inContaq.ui.contactlist;

/**
 * Created by jonathancolon on 6/9/17.
 */

public interface IBasePresenter<T> {
    void setView(T view);
    void clearView();
}
