package nyc.c4q.jonathancolon.inContaq.common.dagger;

import dagger.Module;
import dagger.Provides;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import nyc.c4q.jonathancolon.inContaq.utlities.PhoneNumberFormatter;


@Module
public class PhoneNumberHelperModule {

    @Provides
    PhoneNumberFormatter providesPhoneNumberHelper(PhoneNumberUtil phoneNumberUtil){
        return new PhoneNumberFormatter(phoneNumberUtil);
    }
}
