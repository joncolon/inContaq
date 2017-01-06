package nyc.c4q.jonathancolon.inContaq.utilities.sms;

import org.parceler.Parcel;

@Parcel
public class Sms implements Comparable<Sms>{
    public String _id;
    public String _address;
    public String _msg;
    public String _readState;
    public String _time;
    public String _folderName;
    public String _type;
    public String getId(){
        return _id;
    }
    public String getAddress(){
        return _address;
    }
    public String getMsg(){
        return _msg;
    }
    public String getReadState(){
        return _readState;
    }
    public String getTime(){
        return _time;
    }
    public String getFolderName(){
        return _folderName;
    }

    public String getType() {
        return _type;
    }
    public void setId(String id){
        _id = id;
    }
    public void setAddress(String address){
        _address = address;
    }
    public void setMsg(String msg){
        _msg = msg;
    }
    public void setReadState(String readState){
        _readState = readState;
    }
    public void setTime(String time){
        _time = time;
    }

    public void setFolderName(String folderName){
        _folderName = folderName;
    }

    public void setType(String type) {
        this._type = type;
    }

    @Override
    public int compareTo(Sms another) {
        return getTime().compareTo(another.getTime());
    }
}