package com.example.android.classlist.others;

import java.util.HashMap;

import static com.example.android.classlist.others.Other.Constants.*;

/**
 * Created by User on 4/22/2017.
 * Class to package information to be sent to server as message
 */

public class Message {

    private String name="";
    private String reg_no="";
    private String time="";
    private String pic="";
    private String latitude="";
    private String longitude="";
    private String lac="";
    private String ci="";
    private String phone="";
    private String message="";
    private String choice="";
    private String department="";
    private String year="";

    public Message(HashMap<String, String> args){
        this.name = args.get(NAME);
        this.reg_no = args.get(REG_NO);
        this.department = args.get(DEPARTMENT);
        this.pic = args.get(PIC);
        this.time = args.get(TIME);
        this.latitude = args.get(LATITUDE);
        this.longitude = args.get(LONGITUDE);
        this.lac = args.get(LAC);
        this.ci = args.get(CI);
        this.phone = args.get(PHONE);
        this.message = args.get(SUGGESTION);
        this.choice = args.get(CHOICE);
        this.year = args.get(YEAR);
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
}
