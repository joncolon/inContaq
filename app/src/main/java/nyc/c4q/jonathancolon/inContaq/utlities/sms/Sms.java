package nyc.c4q.jonathancolon.inContaq.utlities.sms;

import org.parceler.Parcel;

@Parcel
public class Sms implements Comparable<Sms>{
    public String _id;
    public String address;
    public String msg;
    public String readState;
    public String time;
    public String foldername;
    public String type;

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