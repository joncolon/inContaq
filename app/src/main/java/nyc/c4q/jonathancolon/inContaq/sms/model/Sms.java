package nyc.c4q.jonathancolon.inContaq.sms.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Sms extends RealmObject implements Comparable<Sms> {

    @PrimaryKey
    private Long realmId;
    private String id;
    private String msg;
    private String readState;
    private String foldername;
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

    public Long getRealmId() {
        return realmId;
    }

    public void setRealmId(Long realmId) {
        this.realmId = realmId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setId(Long id) {
        realmId = id;
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

    public String getReadState() {
        return readState;
    }

    public void setReadState(String readState) {
        this.readState = readState;
    }

    public String getFolderName() {
        return foldername;
    }

    public void setFolderName(String folderName) {
        foldername = folderName;
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