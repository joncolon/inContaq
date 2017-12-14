package nyc.c4q.jonathancolon.inContaq.common.dagger;

import android.content.ContentResolver;

import dagger.Module;
import dagger.Provides;
import nyc.c4q.jonathancolon.inContaq.utlities.SmsUtils;


@Module
public class SmsHelperModule {

    @Provides
    SmsUtils providesSmsHelper(ContentResolver contentResolver){
        return new SmsUtils(contentResolver);
    }
}
