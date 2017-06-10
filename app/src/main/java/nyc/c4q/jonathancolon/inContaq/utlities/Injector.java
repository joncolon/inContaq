package nyc.c4q.jonathancolon.inContaq.utlities;

import java.util.Objects;

import nyc.c4q.jonathancolon.inContaq.ApplicationComponent;
import nyc.c4q.jonathancolon.inContaq.ApplicationContextModule;
import nyc.c4q.jonathancolon.inContaq.DaggerApplicationComponent;
import nyc.c4q.jonathancolon.inContaq.MyApplication;
import nyc.c4q.jonathancolon.inContaq.RepositoryModule;


public class Injector {

    private static ApplicationComponent applicationComponent;

    private Injector() {}

    public static void initializeApplicationComponent(MyApplication app) {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationContextModule(new ApplicationContextModule(app))
                .repositoryModule(new RepositoryModule())
                .build();
    }

    public static ApplicationComponent getApplicationComponent() {
        Objects.requireNonNull(applicationComponent, "applicationComponent is null");
        return applicationComponent;
    }

}
