package nyc.c4q.jonathancolon.inContaq;

import org.junit.Assert;
import org.junit.Test;

import nyc.c4q.jonathancolon.inContaq.model.ContactModel;

/**
 * Created by jonathancolon on 7/27/17.
 */

public class ContactModelTest {

    @Test
    public void getFullNameTest(){
        ContactModel testOne = new ContactModel();
        testOne.setFirstName("John");
        testOne.setLastName("Doe");

        ContactModel testTwo = new ContactModel();
        testTwo.setFirstName("John");
        testTwo.setLastName("");

        ContactModel testThree = new ContactModel();
        testThree.setFirstName("John");
        testThree.setLastName(" ");

        Assert.assertEquals(testOne.getFullName(), "John Doe");
        Assert.assertEquals(testTwo.getFullName(), "John ");
        Assert.assertEquals(testThree.getFullName(), "John  ");
    }
}
