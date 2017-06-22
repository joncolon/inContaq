package nyc.c4q.jonathancolon.inContaq.common.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import nyc.c4q.jonathancolon.inContaq.utlities.PicassoHelper;

@Module
public class PicassoModule {

    @Provides
    PicassoHelper providesPicassoUtils(Context context){
        return new PicassoHelper(context);
    }
}
