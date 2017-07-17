package nyc.c4q.jonathancolon.inContaq.common.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import nyc.c4q.jonathancolon.inContaq.ui.contactlist.ServiceLauncher;


@Module
public class ServiceLauncherModule {

    @Provides
    ServiceLauncher providesServiceLauncher(Context context){
        return new ServiceLauncher(context);
    }
}
