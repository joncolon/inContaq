package nyc.c4q.jonathancolon.inContaq.common.di;

import java.util.Objects;

import nyc.c4q.jonathancolon.inContaq.utlities.RxBus;
import nyc.c4q.jonathancolon.inContaq.common.application.App;

public class Injector {

    private static ApplicationComponent applicationComponent;

    private Injector() {}

    public static void initializeApplicationComponent(App app) {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationContextModule(new ApplicationContextModule(app))
                .realmServiceModule(new RealmServiceModule())
                .picassoModule(new PicassoModule())
                .build();
    }

    public static ApplicationComponent getApplicationComponent() {
        Objects.requireNonNull(applicationComponent, "applicationComponent is null");
        return applicationComponent;
    }

    public static RxBus getRxBus() {
        return getApplicationComponent().getRxBus();
    }
}
