package nyc.c4q.jonathancolon.inContaq;

import org.junit.Assert;
import org.junit.Test;

import nyc.c4q.jonathancolon.inContaq.model.Contact;

/**
 * Created by jonathancolon on 7/27/17.
 */

public class ContactTest {

    @Test
    public void getFullNameTest(){
        Contact testOne = new Contact();
        testOne.setFirstName("John");
        testOne.setLastName("Doe");

        Contact testTwo = new Contact();
        testTwo.setFirstName("John");
        testTwo.setLastName("");

        Contact testThree = new Contact();
        testThree.setFirstName("John");
        testThree.setLastName(" ");

        Assert.assertEquals(testOne.getFullName(), "John Doe");
        Assert.assertEquals(testTwo.getFullName(), "John ");
        Assert.assertEquals(testThree.getFullName(), "John  ");
    }
}
