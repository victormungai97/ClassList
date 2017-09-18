package com.example.android.classlist.others;

/**
 * Created by Victor Mungai on 9/7/2017.
 * Helper class to contain all URLs for the project
 * simplifiedcoding.net
 */

public class URLS {
    // root URL
    private static final String root = "http://192.168.0.11:5000/";
    // student root URL
    private static final String student_root = root.concat("student/");
    // student registration URL
    public static final String student_register = student_root.concat("phone/");
    // departments URL
    public static final String departments = student_root.concat("get_departments/");
}
