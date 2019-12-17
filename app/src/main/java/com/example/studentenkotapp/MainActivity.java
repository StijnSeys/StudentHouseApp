package com.example.studentenkotapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentenkotapp.Model.Student;
import com.example.studentenkotapp.data.DbContract;
import com.example.studentenkotapp.data.DbHelper;

public class MainActivity extends AppCompatActivity {

    private DbHelper dbHelper;

    Button _btnReg,_btnLogin;
    EditText _txtEmail,_txtPass;
    Cursor cursor;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _btnReg = findViewById(R.id.btnRegister);
        _btnLogin = findViewById(R.id.btnAanmelden);
        _txtEmail = findViewById(R.id.etLoginMail);
        _txtPass = findViewById(R.id.etLoginPass);

         dbHelper = new DbHelper(this);

        //Fill the dataBase with the JSON
       dbHelper.startAsyncTask();

       _btnReg.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
               startActivity(intent);
           }
       });


       _btnLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String email = _txtEmail.getText().toString().trim();
               String pass = _txtPass.getText().toString().trim();

               if (email.isEmpty()){
                   _txtEmail.setError("Email nodig om in te loggen!");
                    return;
               }
               if (pass.isEmpty()){
                   _txtPass.setError("Passwoord nodig om in te loggen!");
                   return;
               }

               Student student = dbHelper.ActiveStudent(email,pass);
               if(student!=null){

                       Intent intent = new Intent(MainActivity.this,NavigateActivity.class);
                       intent.putExtra("LogStudent",student);
                       startActivity(intent);

                   }else {
                       Toast toast =    Toast.makeText(getApplicationContext(),"Inlog gegevens zijn niet correct.",Toast.LENGTH_LONG);
                       toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL  , 0, 0);
                       toast.show();
                   }
               }

       });

    }


}
