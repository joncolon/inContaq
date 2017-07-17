package nyc.c4q.jonathancolon.inContaq.common.di;

import dagger.Module;
import dagger.Provides;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import nyc.c4q.jonathancolon.inContaq.utlities.PhoneNumberHelper;


@Module
public class PhoneNumberHelperModule {

    @Provides
    PhoneNumberHelper providesPhoneNumberHelper(PhoneNumberUtil phoneNumberUtil){
        return new PhoneNumberHelper(phoneNumberUtil);
    }
}
