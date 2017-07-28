package nyc.c4q.jonathancolon.inContaq.common.di;

import android.content.ContentResolver;

import dagger.Module;
import dagger.Provides;
import nyc.c4q.jonathancolon.inContaq.utlities.SmsHelper;


@Module
public class SmsHelperModule {

    @Provides
    SmsHelper providesSmsHelper(ContentResolver contentResolver){
        return new SmsHelper(contentResolver);
    }
}