package nyc.c4q.jonathancolon.inContaq.common.dagger;

import android.content.ContentResolver;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jonathancolon on 6/18/17.
 */

@Module
public class ContentResolverModule {

    @Singleton
    @Provides
    public ContentResolver providesContentResolver(Context context){
        return context.getContentResolver();
    }
}
