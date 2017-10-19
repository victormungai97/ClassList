package com.example.android.classlist.others;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.android.classlist.others.Other.Constants.*;

/**
 * Created by User on 4/22/2017.
 * Class to package information to be sent to server as message
 */

public class Message {

    private String name="";
    private String reg_no="";
    private String email="";
    private String time="";
    private String pic="";
    private String latitude="";
    private String longitude="";
    private String altitude="";
    private String lac="";
    private String ci="";
    private String phone="";
    private String message="";
    private String choice="";
    private String department="";
    private String year="";
    private File file;

    public Message(HashMap args){
        this.name = (String) args.get(NAME);
        this.reg_no = (String) args.get(REG_NO);
        this.email = (String) args.get(EMAIL_ADDRESS);
        this.department = (String) args.get(DEPARTMENT);
        this.pic = (String) args.get(PIC);
        this.time = (String) args.get(TIME);
        this.latitude = (String) args.get(LATITUDE);
        this.longitude = (String) args.get(LONGITUDE);
        this.altitude = (String) args.get(ALTITUDE);
        this.lac = (String) args.get(LAC);
        this.ci = (String) args.get(CI);
        this.phone =(String) args.get(PHONE);
        this.message = (String) args.get(SUGGESTION);
        this.choice = (String) args.get(CHOICE);
        this.year = (String) args.get(YEAR);
        this.file = (File) args.get(FILE);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getReg_no() {
        return reg_no;
    }

    String getTime() {
        return time;
    }

    String getPic() {
        return pic;
    }

    String getLatitude() {
        return latitude;
    }

    String getLongitude() {
        return longitude;
    }

    String getLac() {
        return lac;
    }

    String getCi() {
        return ci;
    }

    String getPhone() {
        return phone;
    }

    String getMessage() {
        return message;
    }

    String getChoice() {
        return choice;
    }

    String getDepartment() {
        return department;
    }

    String getYear(){
        return year;
    }

    String getAltitude() {
        return altitude;
    }

    File getFile() {
        return file;
    }

    String getEmail() {
        return email;
    }
}
