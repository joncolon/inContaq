package nyc.c4q.jonathancolon.inContaq.common.di;

import android.content.ContentResolver;

import dagger.Module;
import dagger.Provides;
import nyc.c4q.jonathancolon.inContaq.utlities.SmsHelper;

/**
 * Created by jonathancolon on 6/18/17.
 */

@Module
public class SmsHelperModule {

    @Provides
    SmsHelper providesSmsHelper(ContentResolver contentResolver){
        return new SmsHelper(contentResolver);
    }
}
