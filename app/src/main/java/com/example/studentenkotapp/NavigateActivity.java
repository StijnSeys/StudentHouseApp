package com.example.studentenkotapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentenkotapp.Model.KotHuis;
import com.example.studentenkotapp.Model.Student;
import com.example.studentenkotapp.data.DbHelper;

public class NavigateActivity extends AppCompatActivity {

    TextView _txtWelkom;
    ImageView _imgGoogleMaps, _imgKotLijst;
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);

        DbHelper dbHelper = new DbHelper(this);
        _txtWelkom = findViewById(R.id.tvWelkom);
        _imgGoogleMaps = findViewById(R.id.imgGoogleMaps);
        _imgKotLijst = findViewById(R.id.imgKotLijst);


        final Student logstudent = getIntent().getParcelableExtra("LogStudent");

        student = dbHelper.ActiveStudent(logstudent.geteMail(),logstudent.getPassWord());

        _txtWelkom.setText("Welkom: "+ student.getVoorNaam()+"  "+student.getAchterNaam());

        _imgKotLijst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NavigateActivity.this,LijstActivity.class);
                intent.putExtra("Student",student);
                startActivity(intent);

            }
        });

        _imgGoogleMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavigateActivity.this, MapsActivity.class);

                startActivity(intent);

            }
        });

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
