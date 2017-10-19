package com.example.android.classlist.others;

import android.text.TextWatcher;

public class Other {
    /**
     * Created by User on 5/25/2017.
     * Interface for common methods that need to be overriden for different uses
     */

    public interface Extras {

        /**
         * Method that connects to next activity
         */
        void moveToScreen(String... args);

        /**
         * Checks whether field is empty
         */
        abstract class MyTextWatcher implements TextWatcher {
            protected boolean empty = true;

            public boolean nonEmpty() {
                return !empty;
            }
        }
    }

    /**
     * Class that contains the constants used in the project
     */
    public class Constants{
        public static final String NAME = "name";
        public static final String FIRST_NAME = "FIRST NAME";
        public static final String LAST_NAME = "LAST NAME";
        public static final String EMAIL_ADDRESS = "email";
        public static final String REG_NO = "regno";
        public static final String PIC = "picture";
        public static final String IMAGES = "images";
        public static final String TIME = "time";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String ALTITUDE = "altitude";
        public static final String LAC = "lac";
        public static final String CI = "ci";
        public static final String PHONE = "phone";
        public static final String SUGGESTION = "suggestion";
        public static final String CHOICE = "choice";
        public static final String DEPARTMENT = "departments";
        public static final String YEAR = "year";
        public static final String ERROR = "ERROR";
        public static final String TAG = "TAG";
        public static final String FILE = "FILE";
        public static final String LIST_STATE_KEY = "state of list";
        public static final int REQUEST_PHOTO = 1;
        static final String CAMERA_FACING = "android.intent.extras.CAMERA_FACING";
        public static final String ARG_USER_FULL_NAME = "com.example.android.classlist.full_name";
        public static final String ARG_USER_REG_NUM = "com.example.android.classlist.reg_num";
        public static final String ARG_USER_DIR = "com.example.android.classlist.directory";
        public static final String ARG_USER_POSITION = "com.example.android.classlist.position";
        public static final String ARG_USER_PICTURE = "com.example.android.classlist.picture";
        public static final String ARG_USER_IMAGES = "com.example.android.classlist.images";
        public static final String URL_TO_SEND_DATA = "http://192.168.43.229:5000/fromapp/";
        public static final String EXTRA_USER_FULL_NAME = "com.example.android.classlist.full_name";
        public static final String EXTRA_USER_REG_NUM = "com.example.android.classlist.reg_num";
        public static final String EXTRA_USER_DIR = "com.example.android.classlist.directory";
        public static final String EXTRA_USER_POSITION = "com.example.android.classlist.position";
        public static final String EXTRA_USER_PICTURE = "com.example.android.classlist.picture";
        public static final String EXTRA_USER_IMAGES = "com.example.android.classlist.images";
        public static final String URL = "url";
        static final int BUFFER = 1024;
    }
}
