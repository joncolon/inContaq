package nyc.c4q.jonathancolon.inContaq.common.dagger;

import android.content.ContentResolver;

import dagger.Module;
import dagger.Provides;
import nyc.c4q.jonathancolon.inContaq.database.RealmService;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactsms.data.SmsReader;
import nyc.c4q.jonathancolon.inContaq.utlities.PhoneNumberFormatter;

@Module
public class GetAllSmsModule {

    @Provides
    SmsReader providesGetAllSms(ContentResolver contentResolver, RealmService realmService,
                                PhoneNumberFormatter phoneNumberFormatter){
        return new SmsReader(contentResolver, realmService, phoneNumberFormatter);
    }
}
