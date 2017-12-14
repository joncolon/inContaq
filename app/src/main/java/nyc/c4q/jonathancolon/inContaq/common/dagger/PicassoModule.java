package nyc.c4q.jonathancolon.inContaq.common.dagger;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import nyc.c4q.jonathancolon.inContaq.utlities.PicassoUtils;

@Module
public class PicassoModule {

    @Provides
    PicassoUtils providesPicassoUtils(Context context){
        return new PicassoUtils(context);
    }
}
