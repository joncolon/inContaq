package nyc.c4q.jonathancolon.inContaq.model;

import android.support.annotation.NonNull;


public class Sms implements Comparable<Sms> {

    private String message;
    private String phoneNumber;
    private String timeStamp;
    private String type;

    public Sms(String address, String message, String timeStamp, String type) {
        this.phoneNumber = address;
        this.message = message;
        this.timeStamp = timeStamp;
        this.type = type;
    }

    public Sms() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int compareTo(@NonNull Sms sms) {
        return getTimeStamp().compareTo(sms.getTimeStamp());
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}