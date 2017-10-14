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

    public static final String[] IMAGES = new String[]{
            // Heavy images
            "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg",
            "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg",
            "https://lh5.googleusercontent.com/-7qZeDtRKFKc/URquWZT1gOI/AAAAAAAAAbs/hqWgteyNXsg/s1024/Another%252520Rockaway%252520Sunset.jpg",
            "https://lh3.googleusercontent.com/--L0Km39l5J8/URquXHGcdNI/AAAAAAAAAbs/3ZrSJNrSomQ/s1024/Antelope%252520Butte.jpg",
            "https://lh6.googleusercontent.com/-8HO-4vIFnlw/URquZnsFgtI/AAAAAAAAAbs/WT8jViTF7vw/s1024/Antelope%252520Hallway.jpg",
            "https://lh4.googleusercontent.com/-WIuWgVcU3Qw/URqubRVcj4I/AAAAAAAAAbs/YvbwgGjwdIQ/s1024/Antelope%252520Walls.jpg",
            "https://lh6.googleusercontent.com/-UBmLbPELvoQ/URqucCdv0kI/AAAAAAAAAbs/IdNhr2VQoQs/s1024/Apre%2525CC%252580s%252520la%252520Pluie.jpg",
            "https://lh3.googleusercontent.com/-s-AFpvgSeew/URquc6dF-JI/AAAAAAAAAbs/Mt3xNGRUd68/s1024/Backlit%252520Cloud.jpg",
            "https://lh5.googleusercontent.com/-bvmif9a9YOQ/URquea3heHI/AAAAAAAAAbs/rcr6wyeQtAo/s1024/Bee%252520and%252520Flower.jpg",
            "https://lh5.googleusercontent.com/-n7mdm7I7FGs/URqueT_BT-I/AAAAAAAAAbs/9MYmXlmpSAo/s1024/Bonzai%252520Rock%252520Sunset.jpg",
            "https://lh6.googleusercontent.com/-4CN4X4t0M1k/URqufPozWzI/AAAAAAAAAbs/8wK41lg1KPs/s1024/Caterpillar.jpg",
            "https://lh3.googleusercontent.com/-rrFnVC8xQEg/URqufdrLBaI/AAAAAAAAAbs/s69WYy_fl1E/s1024/Chess.jpg",
            "https://lh5.googleusercontent.com/-WVpRptWH8Yw/URqugh-QmDI/AAAAAAAAAbs/E-MgBgtlUWU/s1024/Chihuly.jpg",
            "https://lh5.googleusercontent.com/-0BDXkYmckbo/URquhKFW84I/AAAAAAAAAbs/ogQtHCTk2JQ/s1024/Closed%252520Door.jpg",
            "https://lh3.googleusercontent.com/-PyggXXZRykM/URquh-kVvoI/AAAAAAAAAbs/hFtDwhtrHHQ/s1024/Colorado%252520River%252520Sunset.jpg",
            "https://lh3.googleusercontent.com/-ZAs4dNZtALc/URquikvOCWI/AAAAAAAAAbs/DXz4h3dll1Y/s1024/Colors%252520of%252520Autumn.jpg",
            "https://lh4.googleusercontent.com/-GztnWEIiMz8/URqukVCU7bI/AAAAAAAAAbs/jo2Hjv6MZ6M/s1024/Countryside.jpg",
            "https://lh4.googleusercontent.com/-bEg9EZ9QoiM/URquklz3FGI/AAAAAAAAAbs/UUuv8Ac2BaE/s1024/Death%252520Valley%252520-%252520Dunes.jpg",
    };

    /**
     * Class that contains the constants used in the project
     */
    public class Constants{
        public static final String NAME = "name";
        public static final String FIRST_NAME = "FIRST NAME";
        public static final String LAST_NAME = "LAST NAME";
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
        public static final String PHOTO = "photo";
        public static final String LIST_STATE_KEY = "state of list";
        public static final int REQUEST_PHOTO = 1;
        public static final String CAMERA_FACING = "android.intent.extras.CAMERA_FACING";
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
    }
}
