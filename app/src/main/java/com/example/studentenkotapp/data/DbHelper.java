package com.example.studentenkotapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;


import com.example.studentenkotapp.Model.KotHuis;
import com.example.studentenkotapp.Model.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class DbHelper extends SQLiteOpenHelper {

    private static final String TAG = DbHelper.class.getSimpleName();


    private static final String DATABASE_NAME = DbContract.DATABSE_NAME;
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;
    private List<Student> kotStudentlist;
    private List<KotHuis> kotHuisList;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        final String SQL_KOT_TABLE = "CREATE TABLE " + DbContract.MenuEntry.TABLE_NAME + " (" +
                DbContract.MenuEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DbContract.MenuEntry.COLUMN_ADRES + " TEXT NOT NULL, " +
                DbContract.MenuEntry.COLUMN_NR + " INTEGER NOT NULL, " +
                DbContract.MenuEntry.COLUMN_CITY + " TEXT NOT NULL, " +
                DbContract.MenuEntry.COLUMN_KAMERS + " INTEGER NOT NULL, " +
                DbContract.MenuEntry.COLUMN_TAKEN + " INTEGER NOT NULL, " +
                DbContract.MenuEntry.COLUMN_RATING + " INTEGER NOT NULL " + " );";

        db.execSQL(SQL_KOT_TABLE);

        final String SQL_STUDENT_TABLE = "CREATE TABLE " + DbContract.studentEntry.TABLE_NAME_STUDENT + " (" +
                DbContract.studentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbContract.studentEntry.COLUMN_VNAAM + " TEXT NOT NULL, " +
                DbContract.studentEntry.COLUMN_ANAAM + " TEXT NOT NULL, " +
                DbContract.studentEntry.COLUMN_EMAIL + " TEXT NOT NULL, " +
                DbContract.studentEntry.COLUMN_PASS + " TEXT NOT NULL, " +
                DbContract.studentEntry.COLUMN_TELE + " TEXT NOT NULL, " +
                DbContract.studentEntry.COLUMN_RICHTING + " TEXT NOT NULL, " +
                DbContract.studentEntry.COLUMN_KOT + " INTEGER, " +
                " FOREIGN KEY("+DbContract.studentEntry.COLUMN_KOT+") REFERENCES "+
                DbContract.MenuEntry.TABLE_NAME+"("+DbContract.MenuEntry._ID+"));";


        db.execSQL(SQL_STUDENT_TABLE);
        Log.d(TAG, "DATABASES CREATED!!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void startAsyncTask() {

        //Check if DATABASE is already filled.
        String count = "SELECT count(*) FROM " + DbContract.MenuEntry.TABLE_NAME;
        Cursor mcCursor = db.rawQuery(count, null);
        mcCursor.moveToFirst();
        int icount = mcCursor.getInt(0);
        if (icount == 0) {
            FetchData2 fetch = new FetchData2();
            fetch.execute();
        }
    }

    //AsyncTask To Put JSON into DataBAse
    private class FetchData2 extends AsyncTask<Void, Void, Void> {

        private String data = "";

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                URL url = new URL("https://data.kortrijk.be/studentenvoorzieningen/koten.json");
                //Setting up the connection with the URL
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                //makes it possible to read the data
                InputStream inputStream = httpURLConnection.getInputStream();
                //This reads the data from the stream
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    data = data + line;
                }

                JSONArray JA = new JSONArray(data);
                for (int i = 0; i < JA.length(); i++) {

                    JSONObject JO = JA.getJSONObject(i);
                    String adres = (String) JO.get("ADRES");
                    String huisnr = String.valueOf(JO.get("HUISNR"));
                    if (huisnr.equals("")) {
                        continue;
                    }
                    while (!huisnr.matches("^[0-9]*$")) {
                        int c = (huisnr.length()) - 1;
                        huisnr = huisnr.substring(0, c);
                    }

                    int ihuisnr = Integer.parseInt(huisnr);
                    String stad = (String) JO.get("GEMEENTE");
                    int kamers = Integer.parseInt(String.valueOf(JO.get("aantal kamers")));

                    ContentValues menuValues = new ContentValues();

                    menuValues.put(DbContract.MenuEntry.COLUMN_ADRES, adres);
                    menuValues.put(DbContract.MenuEntry.COLUMN_NR, ihuisnr);
                    menuValues.put(DbContract.MenuEntry.COLUMN_CITY, stad);
                    menuValues.put(DbContract.MenuEntry.COLUMN_KAMERS, kamers);
                    menuValues.put(DbContract.MenuEntry.COLUMN_TAKEN, 0);
                    menuValues.put(DbContract.MenuEntry.COLUMN_RATING, 0);

                    PutDataInDB(menuValues);
                    Log.d(TAG, adres + " " + huisnr + " " + stad + "" +
                            " " + kamers);

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    public void PutDataInDB(ContentValues cv) {

        db.insert(DbContract.MenuEntry.TABLE_NAME, null, cv);

    }

    public Student ActiveStudent(String email,String pass){

        Student student = null;
        db= this.getReadableDatabase();
       Cursor cursor = db.rawQuery("SELECT * FROM "+ DbContract.studentEntry.TABLE_NAME_STUDENT + " WHERE "+ DbContract.studentEntry.COLUMN_EMAIL +
                "=? AND "+ DbContract.studentEntry.COLUMN_PASS + "=?", new String[]{email,pass});
        if (cursor!=null) {
            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                String vnaam = cursor.getString(1);
                String lnaam = cursor.getString(2);
                String phone = cursor.getString(5);
                int id = cursor.getInt(0);
                Integer kotid = null;
                if (!cursor.isNull(7)) {
                    kotid = cursor.getInt(7);
                }
                 student = new Student(vnaam, lnaam, email, pass, phone, id, kotid);
            }
        }
        return student;

    }
    //Load kotstudentlist students to infoActivity
    public List<Student> GetKotMembers(Integer id){

            kotStudentlist = new ArrayList<>();
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+DbContract.studentEntry.TABLE_NAME_STUDENT+" WHERE "+DbContract.studentEntry.COLUMN_KOT+"="+id,null);
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()){

                String voornaam = cursor.getString(1);
                String achternaam = cursor.getString(2 );
                String studieRichting = cursor.getString(6);
                Student student = new Student(voornaam,achternaam,studieRichting);
                kotStudentlist.add(student);
                cursor.moveToNext();
            }
        }
        cursor.close();
             return kotStudentlist;
    }

    public List<KotHuis> GetAllKotAdress(){

        kotHuisList = new ArrayList<>();
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+DbContract.MenuEntry.TABLE_NAME,null);
        if (cursor.moveToFirst()){

            while (!cursor.isAfterLast()){

                String straat = cursor.getString(cursor.getColumnIndex(DbContract.MenuEntry.COLUMN_ADRES));
                int nummer = cursor.getInt(cursor.getColumnIndex(DbContract.MenuEntry.COLUMN_NR));
                String stad = cursor.getString(cursor.getColumnIndex(DbContract.MenuEntry.COLUMN_CITY));
                KotHuis kotHuis = new KotHuis(straat,nummer,stad);
                kotHuisList.add(kotHuis);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return kotHuisList;
    }



    public KotHuis getKothuis(Integer kotid){

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ DbContract.MenuEntry.TABLE_NAME +" WHERE "+ DbContract.MenuEntry._ID + "="+ kotid,null );
        KotHuis kotHuis = null;
        if (cursor.moveToFirst()){
            String straat = cursor.getString(1);
            int number = cursor.getInt(2);
            String gemeente = cursor.getString(3);
            int kamers = cursor.getInt(4);
            int bKamers = cursor.getInt(5);
            int rat = cursor.getInt(6);

             kotHuis = new KotHuis(straat,number,gemeente,kamers,bKamers,rat,kotid);

        }

        cursor.close();
        return kotHuis;
    }


    //Reseveer kot + kot id aan student toevoegen
    public void Reserveren(KotHuis kot, Student student){


        int reservaties = kot.getAantalKamersBezet()+1;
        ContentValues values = new ContentValues();
        values.put(DbContract.MenuEntry.COLUMN_TAKEN,reservaties);

        db.update(DbContract.MenuEntry.TABLE_NAME,values,"_id="+kot.getId(),null);

        ContentValues values1 = new ContentValues();
        values1.put(DbContract.studentEntry.COLUMN_KOT,kot.getId());
        db.update(DbContract.studentEntry.TABLE_NAME_STUDENT,values1,"_id="+student.getiD(),null);

    }

    public void StopReservatie(int id, int aantal){

        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.putNull(DbContract.studentEntry.COLUMN_KOT);
        db.update(DbContract.studentEntry.TABLE_NAME_STUDENT,values, DbContract.studentEntry.COLUMN_KOT+"="+id , null);

        ContentValues values1 = new ContentValues();
        values1.put(DbContract.MenuEntry.COLUMN_TAKEN,aantal);
        db.update( DbContract.MenuEntry.TABLE_NAME,values1,  DbContract.MenuEntry._ID+"="+id, null );

    }


    public boolean checkRow(String vNaam, String aNaam, String eMail){
        db = this.getReadableDatabase();
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT * FROM "+ DbContract.studentEntry.TABLE_NAME_STUDENT + " WHERE " + DbContract.studentEntry.COLUMN_VNAAM +
               "='" + vNaam + "' AND " + DbContract.studentEntry.COLUMN_ANAAM+ "='" + aNaam + "' AND "+
                DbContract.studentEntry.COLUMN_EMAIL  +"='"+ eMail+"';",null );
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;

    }


}

