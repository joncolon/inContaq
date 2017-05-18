package nyc.c4q.jonathancolon.inContaq.model;

import io.realm.annotations.PrimaryKey;


public class Sms implements Comparable<Sms> {

    @PrimaryKey
    private String msg;
    private String address;
    private String time;
    private String type;

    public Sms(String address, String msg, String time, String type) {
        this.address = address;
        this.msg = msg;
        this.time = time;
        this.type = type;
    }

    public Sms() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int compareTo(Sms sms) {
        return getTime().compareTo(sms.getTime());
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}