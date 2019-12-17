package com.example.studentenkotapp.data;

import android.provider.BaseColumns;

public class DbContract {

    public static final String DATABSE_NAME = "KotApp.db";

    // DB voor KOTADRES
    public static final class MenuEntry implements BaseColumns {

        public static final String TABLE_NAME = "studentenkot";
        public static final String COLUMN_ADRES = "adres";
        public static final String COLUMN_NR = "huisnr";
        public static final String COLUMN_CITY = "stad";
        public static final String COLUMN_KAMERS = "aantal_kamers";
        public static final String COLUMN_TAKEN = "geboekte_kamers";
        public static final String COLUMN_RATING = "rating";
    }

    //DB voor STUDENT
        public static final class studentEntry implements BaseColumns{

        public static final String TABLE_NAME_STUDENT = "Student";
        public static final String COLUMN_VNAAM = "voornaam";
        public static final String COLUMN_ANAAM = "achternaam";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASS = "password";
        public static final String COLUMN_TELE = "telefoon";
        public static final String COLUMN_RICHTING = "studierichting";
        public static final String COLUMN_KOT = "kot";



    }

}
