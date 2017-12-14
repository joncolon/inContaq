package nyc.c4q.jonathancolon.inContaq.common.base;


import android.os.Bundle;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;


public abstract class Presenter<T extends BaseView> {

    private WeakReference<T> view;

    private AtomicBoolean isViewAlive = new AtomicBoolean();

    public T getView() {
        return view.get();
    }

    public void setView(T view) {
        this.view = new WeakReference<>(view);
    }

    public void initialize(Bundle extras) {}

    public void start() {
        isViewAlive.set(true);
    }

    public void finalizeView() {
        isViewAlive.set(false);
    }

}
