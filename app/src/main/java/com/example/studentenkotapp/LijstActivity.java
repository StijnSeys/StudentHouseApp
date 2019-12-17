package com.example.studentenkotapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.studentenkotapp.Adapter.KotAdapter;
import com.example.studentenkotapp.Model.KotHuis;
import com.example.studentenkotapp.Model.Student;
import com.example.studentenkotapp.data.DbContract;
import com.example.studentenkotapp.data.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class LijstActivity extends AppCompatActivity {

    SQLiteDatabase db;
    List<KotHuis> kotlist;
   Button _btnBack;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lijst);

        _btnBack = findViewById(R.id.btnLijstTerug);
        DbHelper dbHelper = new DbHelper(this);
        db = dbHelper.getReadableDatabase();
        final ListView result = findViewById(R.id.lvResult);
        final Student student = getIntent().getParcelableExtra("Student");

        kotlist = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+DbContract.MenuEntry.TABLE_NAME,null);

        if (cursor.moveToFirst()){
          while (!cursor.isAfterLast()){

                String straat = cursor.getString(1);
                int number = cursor.getInt(2);
                String gemeente = cursor.getString(3);
                int kamers = cursor.getInt(4);
                int bKamers = cursor.getInt(5);
                int rat = cursor.getInt(6);
                int iD = cursor.getInt(0);
                KotHuis kot = new KotHuis(straat,number,gemeente,kamers,bKamers,rat,iD);
                kotlist.add(kot);
                cursor.moveToNext();

            }

            KotAdapter kotAdapter = new KotAdapter(kotlist,result.getContext(),student);
            result.setAdapter(kotAdapter);
        }
        cursor.close();
        db.close();

        _btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {

        DbHelper dbHelper = new DbHelper(this);

        Student student = getIntent().getParcelableExtra("Student");
        KotHuis kotHuis = dbHelper.getKothuis(student.getKotID());
        switch (item.getItemId()){
            case R.id.menuKot:
                if (student.getKotID()!=null){

                    Intent intent1 = new Intent(this,InfoActivity.class);
                    intent1.putExtra("Student", student);
                    intent1.putExtra("kotSelect", kotHuis);
                    this.startActivity(intent1);
                }else{
                    Toast.makeText(getApplicationContext(),"U hebt nog geen kot gereserveerd!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.menuAnnuleer:
                if (student.getKotID()!= null){

                    int aantal = kotHuis.getAantalKamersBezet()-1;
                    dbHelper.StopReservatie(kotHuis.getId(),aantal);
                    Intent intent2 = new Intent(getApplicationContext(),NavigateActivity.class);
                    intent2.putExtra("LogStudent", student);
                    startActivity(intent2);
                    Toast.makeText(getApplicationContext(),"Kot geanulleerd!",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"U moet eerst een kot reserveren!", Toast.LENGTH_LONG).show();
                }
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;

    }
}
