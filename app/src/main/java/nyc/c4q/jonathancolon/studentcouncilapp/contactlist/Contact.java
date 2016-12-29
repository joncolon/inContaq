package nyc.c4q.jonathancolon.studentcouncilapp.contactlist;

import java.io.Serializable;

import nyc.c4q.jonathancolon.studentcouncilapp.R;

/**
 * Created by jonathancolon on 10/27/16.
 */

public class Contact implements Serializable{

    private long _id;
    private long birthDate;
    private String firstName;
    private String lastName;
    private String nickname;
    private String homePhoneNumber;
    private String workPhoneNumber;
    private String cellPhoneNumber;
    private String address;
    private String email;
    private Integer contactImage;

    public Contact() {
        contactImage = R.drawable.c4q;
    }

    public Contact(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        contactImage = R.drawable.c4q;
    }



    public long getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(long birthDate) {
        this.birthDate = birthDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHomePhoneNumber() {
        return homePhoneNumber;
    }

    public void setHomePhoneNumber(String homePhoneNumber) {
        this.homePhoneNumber = homePhoneNumber;
    }

    public String getWorkPhoneNumber() {
        return workPhoneNumber;
    }

    public void setWorkPhoneNumber(String workPhoneNumber) {
        this.workPhoneNumber = workPhoneNumber;
    }

    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }

    public void setCellPhoneNumber(String cellPhoneNumber) {
        this.cellPhoneNumber = cellPhoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getContactImage() {
        return contactImage;
    }

    public void setContactImage(Integer contactImage) {
        this.contactImage = contactImage;
    }
}
