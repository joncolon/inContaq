package nyc.c4q.jonathancolon.inContaq.common.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;


@Module
public class PhoneNumberUtilModule {

    @Provides
    public PhoneNumberUtil providesPhoneNumberUtil(Context context){
        return PhoneNumberUtil.createInstance(context);
    }
}
