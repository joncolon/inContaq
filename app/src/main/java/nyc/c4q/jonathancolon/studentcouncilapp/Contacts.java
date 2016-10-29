package nyc.c4q.jonathancolon.studentcouncilapp;

import android.support.annotation.DrawableRes;

/**
 * Created by jonathancolon on 10/27/16.
 */

public class Contacts {

    private String mName;
    private Integer mImage;
    private String mNote;

    public Contacts(String name, @DrawableRes Integer image){
        mName = name;
        mImage = image;
    }

    public Contacts(String name){
        mName = name;
        mImage = R.drawable.c4q;
    }

    public Contacts(String name, String note){
        mName = name;
        mImage = R.drawable.c4q;
        mNote = note;
    }

    public void setName(String name){
        this.mName = name;
    }

    public String getName(){
        return mName;
    }

    public void setImage(Integer image) {
        this.mImage = image;
    }

    public Integer getImage() {
        return mImage;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        this.mNote = note;
    }


}
