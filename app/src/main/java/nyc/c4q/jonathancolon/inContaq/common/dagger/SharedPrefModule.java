package nyc.c4q.jonathancolon.inContaq.common.dagger;

import android.content.Context;
import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;
import nyc.c4q.jonathancolon.inContaq.utlities.SharedPrefsUtils;


@Module
public class SharedPrefModule {

    @Provides
    SharedPrefsUtils provideSharedPrefUtils(@NonNull Context context) {
        return new SharedPrefsUtils(context);
    }
}
