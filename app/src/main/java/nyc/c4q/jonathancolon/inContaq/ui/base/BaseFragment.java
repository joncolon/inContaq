package nyc.c4q.jonathancolon.inContaq.ui.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import nyc.c4q.jonathancolon.inContaq.ui.base.listeners.BaseView;

import static nyc.c4q.jonathancolon.inContaq.utlities.ObjectUtils.isNull;


public abstract class BaseFragment extends Fragment implements BaseView {

    protected FragmentManager fragmentManager;

    protected Presenter presenter;

    protected abstract void initializeDagger();

    protected abstract void initializePresenter();

    public abstract int getLayoutId();

    private View view;

    private Unbinder unbinder;

    private String toolbarTitleKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getActivity().getSupportFragmentManager();
        initializeDagger();
        initializePresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        if (isNull(presenter)) {
            presenter.initialize(getArguments());
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isNull(presenter)) {
            presenter.start();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isNull(presenter)) {
            presenter.finalizeView();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
