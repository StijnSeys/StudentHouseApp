package com.example.studentenkotapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentenkotapp.data.DbContract;
import com.example.studentenkotapp.data.DbHelper;

public class RegisterActivity extends AppCompatActivity {

    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    Button _btnBevestigButton;
    EditText _txtfirstName,_txtlasName,_txtEmail,_txtPassWord,_txtPassWord2,_txtTelefoon, _txtStudieRichting;
    Context context;
    boolean check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final DbHelper dbHelper = new DbHelper(this);


       openHelper = new DbHelper(RegisterActivity.this);
        setContentView(R.layout.activity_register_page);

        _txtfirstName = findViewById(R.id.etVoornaam);
        _txtlasName = findViewById(R.id.etAchternaam);
        _txtEmail = findViewById(R.id.etEmail);
        _txtPassWord = findViewById(R.id.etPass1);
        _txtPassWord2 = findViewById(R.id.etPass2);
        _txtTelefoon = findViewById(R.id.etTelefoon);
        _txtStudieRichting = findViewById(R.id.etRichting);

        _btnBevestigButton = findViewById(R.id.btnBevestig);

        _btnBevestigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db= openHelper.getReadableDatabase();

                String firstname = _txtfirstName.getText().toString().trim();
                String lastname = _txtlasName.getText().toString().trim();
                String eMail = _txtEmail.getText().toString().trim();
                String pass1 = _txtPassWord.getText().toString().trim();
                String pass2 = _txtPassWord2.getText().toString().trim();
                if (!pass1.equals(pass2)){
                    _txtPassWord.setText("");
                    _txtPassWord2.setText("");
                    Toast.makeText(getApplicationContext(),"Niet dezelfde passwoorden!",Toast.LENGTH_LONG).show();
                    return;
                }

                 check = dbHelper.checkRow(firstname,lastname,eMail);
                 if (check){
                    finish();
                 }
                String telefoon = _txtTelefoon.getText().toString().trim();
                String studRichting= _txtStudieRichting.getText().toString().trim();
                InsertStudenRegData(firstname,lastname,eMail,pass2,telefoon,studRichting);
                finish();

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (check){
        Toast.makeText(getApplicationContext(), "U ben al geregistreerd!", Toast.LENGTH_LONG).show();}else {
        Toast.makeText(getApplicationContext(),"U bent succesvol geregistreerd!",Toast.LENGTH_LONG).show();}



    }
    //DIT MOET NOG IN APPARTE CLASS

    public void InsertStudenRegData(String firstname,String lastname,String email,String password , String telefoon, String studieRichting){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.studentEntry.COLUMN_VNAAM,firstname);
        contentValues.put(DbContract.studentEntry.COLUMN_ANAAM,lastname);
        contentValues.put(DbContract.studentEntry.COLUMN_EMAIL,email);
        contentValues.put(DbContract.studentEntry.COLUMN_PASS,password);
        contentValues.put(DbContract.studentEntry.COLUMN_TELE, telefoon);
        contentValues.put(DbContract.studentEntry.COLUMN_RICHTING,studieRichting);

        db.insert(DbContract.studentEntry.TABLE_NAME_STUDENT,null,contentValues);

    }
}
