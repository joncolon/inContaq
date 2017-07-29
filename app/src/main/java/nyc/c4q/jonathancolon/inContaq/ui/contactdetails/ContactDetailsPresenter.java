package nyc.c4q.jonathancolon.inContaq.ui.contactdetails;

import android.os.Bundle;

import javax.inject.Inject;

import nyc.c4q.jonathancolon.inContaq.common.base.Presenter;


public class ContactDetailsPresenter extends Presenter<ContactDetailsContract.View> implements
        ContactDetailsContract.Presenter {

    @Inject
    public ContactDetailsPresenter() {
    }

    @Override
    public void initialize(Bundle extras) {
        super.initialize(extras);
        getView().loadSelectedContact();
        getView().showViewPager();
    }
}
