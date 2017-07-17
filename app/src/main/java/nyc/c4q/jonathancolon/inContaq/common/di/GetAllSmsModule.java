package nyc.c4q.jonathancolon.inContaq.common.di;

import android.content.ContentResolver;

import dagger.Module;
import dagger.Provides;
import nyc.c4q.jonathancolon.inContaq.database.RealmService;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactsms.data.GetAllSms;
import nyc.c4q.jonathancolon.inContaq.utlities.PhoneNumberHelper;

@Module
public class GetAllSmsModule {

    @Provides
    GetAllSms providesGetAllSms(ContentResolver contentResolver, RealmService realmService,
                                PhoneNumberHelper phoneNumberHelper){
        return new GetAllSms(contentResolver, realmService, phoneNumberHelper);
    }
}
