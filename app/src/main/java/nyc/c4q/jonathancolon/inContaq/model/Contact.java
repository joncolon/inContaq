package nyc.c4q.jonathancolon.inContaq.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

// Your model just have to extend RealmObject.
// This will inherit an annotation which produces proxy getters and setters for all fields.

public class Contact extends RealmObject {

    // All fields are by default persisted.
    @PrimaryKey
    private Long realmID;

    private String contactID;
    private long birthDate;
    private String firstName;
    private String lastName;
    private String nickname;
    private String homePhoneNumber;
    private String workPhoneNumber;
    private String mobileNumber;
    private String address;
    private String email;
    private String backgroundImage;
    private String contactImage;
    private Long timeLastContacted;
    private boolean reminderEnabled = false;
    private int reminderDuration;

    public Contact() {
    }

    public Contact(String firstName) {
        this.firstName = firstName;
    }

    public Contact(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Contact(String firstName, String lastName, String mobileNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
    }

    public String getContactImage() {
        return contactImage;
    }

    public void setContactImage(String contactImage) {
        this.contactImage = contactImage;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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

    public Long getTimeLastContacted() {
        return timeLastContacted;
    }

    public void setTimeLastContacted(Long timeLastContacted) {
        this.timeLastContacted = timeLastContacted;
    }

    public int getReminderDuration() {
        return reminderDuration;
    }

    public void setReminderDuration(int reminderDuration) {
        this.reminderDuration = reminderDuration;
    }

    public boolean isReminderEnabled() {
        return reminderEnabled;
    }

    public void setReminderEnabled(boolean reminderEnabled) {
        this.reminderEnabled = reminderEnabled;
    }

    public Long getRealmID() {
        return realmID;
    }

    public void setRealmID(Long realmID) {
        this.realmID = realmID;
    }
}
