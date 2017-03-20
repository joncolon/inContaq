package nyc.c4q.jonathancolon.inContaq.utlities.sms.model;

import org.parceler.Parcel;

@Parcel
public class Sms implements Comparable<Sms>{
    String _id;
    public String address;
    String msg;
    String readState;
    public String time;
    String foldername;
    public String type;

    public Sms(String address, String msg, String time, String type) {
        this.address = address;
        this.msg = msg;
        this.time = time;
        this.type = type;
    }

    public Sms() {

    }

    public String getId(){
        return _id;
    }

    public String getAddress(){
        return address;
    }

    public String getMsg(){
        return msg;
    }

    public String getReadState(){
        return readState;
    }
    public String getTime(){
        return time;
    }
    public String getFolderName(){
        return foldername;
    }

    public String getType() {
        return type;
    }
    public void setId(String id){
        _id = id;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public void setReadState(String readState){
        this.readState = readState;
    }
    public void setTime(String time){
        this.time = time;
    }

    public void setFolderName(String folderName){
        foldername = folderName;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int compareTo(Sms sms) {
        return getTime().compareTo(sms.getTime());
    }
}