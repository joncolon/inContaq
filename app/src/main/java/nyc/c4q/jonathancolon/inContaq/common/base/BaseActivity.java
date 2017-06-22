package nyc.c4q.jonathancolon.inContaq.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static nyc.c4q.jonathancolon.inContaq.utlities.ObjectUtils.isNull;


public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    protected Presenter presenter;

    private Unbinder unbinder;

    protected abstract void initializeDagger();
    protected abstract void initializePresenter();
    public abstract int getLayoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        initializeDagger();
        initializePresenter();

        if (!isNull(presenter)) {
            presenter.initialize(getIntent().getExtras());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isNull(presenter)) {
            presenter.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isNull(presenter)) {
            presenter.finalizeView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
