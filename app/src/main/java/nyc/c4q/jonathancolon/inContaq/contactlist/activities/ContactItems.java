package nyc.c4q.jonathancolon.inContaq.contactlist.activities;

import android.graphics.Bitmap;

/**
 * Created by jonathancolon on 3/7/17.
 */

public class ContactItems {
    String id;
    String name;
    String number;
    Bitmap image;

    public ContactItems(String id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}