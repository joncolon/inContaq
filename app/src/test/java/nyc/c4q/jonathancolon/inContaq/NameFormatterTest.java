package nyc.c4q.jonathancolon.inContaq;

import junit.framework.Assert;

import org.junit.Test;

import nyc.c4q.jonathancolon.inContaq.utlities.NameFormatter;

/**
 * Created by jonat on 12/13/2017.
 */

public class NameFormatterTest {

    @Test
    public void hasMultipleNames_containsFirstAndLastName_shouldReturnTrue(){
        String name = "John Doe";
        Assert.assertEquals(true, NameFormatter.hasMultipleNames(name));
    }

    @Test
    public void hasMultipleNames_noTrailingWhiteSpace_shouldReturnFalse(){
        String name = "John";
        Assert.assertEquals(false, NameFormatter.hasMultipleNames(name));
    }

    @Test
    public void hasMultipleNames_trailingWhiteSpace_shouldReturnFalse(){
        String name = "John   ";
        Assert.assertEquals(false, NameFormatter.hasMultipleNames(name));
    }
}
