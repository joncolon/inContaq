package nyc.c4q.jonathancolon.inContaq.model;

import android.support.annotation.NonNull;


public class SmsModel implements Comparable<SmsModel> {

    private String message;
    private String phoneNumber;
    private String timeStamp;
    private String type;

    public SmsModel(String address, String message, String timeStamp, String type) {
        this.phoneNumber = address;
        this.message = message;
        this.timeStamp = timeStamp;
        this.type = type;
    }

    public SmsModel() {
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
    public int compareTo(@NonNull SmsModel smsModel) {
        return getTimeStamp().compareTo(smsModel.getTimeStamp());
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}