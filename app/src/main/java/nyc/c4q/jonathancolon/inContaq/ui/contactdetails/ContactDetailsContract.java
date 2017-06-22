package nyc.c4q.jonathancolon.inContaq.ui.contactdetails;

import nyc.c4q.jonathancolon.inContaq.common.base.BaseView;

/**
 * Created by jonathancolon on 6/20/17.
 */

public class ContactDetailsContract {

    interface View extends BaseView {
        void loadSelectedContact();

        void showViewPager();
    }

    interface Presenter {
    }
}
