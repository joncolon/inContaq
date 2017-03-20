package nyc.c4q.jonathancolon.inContaq.utlities.datehelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import nyc.c4q.jonathancolon.inContaq.contactlist.model.Contact;


public class MilliToDateFormat {

    private DateFormat calanderDateformatter = new SimpleDateFormat("MM/dd/yyyy");
    private SimpleDateFormat timeDateFormatter = new SimpleDateFormat("hh:mm:ss a");

    public String convertToCalendarFormat(Contact contact) {
        return calanderDateformatter.format(new Date(contact.getTimeLastContacted()));
    }

    public String convertToTimeFormat(Contact contact) {
        return timeDateFormatter.format(new Date(contact.getTimeLastContacted()));
    }
}
