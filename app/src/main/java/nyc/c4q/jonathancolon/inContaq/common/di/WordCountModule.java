package nyc.c4q.jonathancolon.inContaq.common.di;

import dagger.Module;
import dagger.Provides;
import nyc.c4q.jonathancolon.inContaq.ui.contactdetails.contactstats.data.WordCount;
import nyc.c4q.jonathancolon.inContaq.utlities.SmsHelper;

/**
 * Created by jonathancolon on 6/19/17.
 */

@Module
public class WordCountModule {

    @Provides
    WordCount providesWordCount(SmsHelper smsHelper){
        return new WordCount(smsHelper);
    }
}
