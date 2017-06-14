package nyc.c4q.jonathancolon.inContaq.di;

import java.util.Objects;

import nyc.c4q.jonathancolon.inContaq.application.AppController;

public class Injector {

    private static ApplicationComponent applicationComponent;

    private Injector() {}

    public static void initializeApplicationComponent(AppController app) {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationContextModule(new ApplicationContextModule(app))
                .realmModule(new RealmModule())
                .build();
    }

    public static ApplicationComponent getApplicationComponent() {
        Objects.requireNonNull(applicationComponent, "applicationComponent is null");
        return applicationComponent;
    }

}
