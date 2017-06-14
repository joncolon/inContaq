package nyc.c4q.jonathancolon.inContaq.di;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by jonathancolon on 6/10/17.
 */
@Module
public class MainModule {

    @Provides
    public CompositeDisposable provideCompositeSubscription() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        return compositeDisposable;
    }
}
