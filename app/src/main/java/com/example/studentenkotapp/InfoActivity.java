package com.example.studentenkotapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentenkotapp.Model.KotHuis;
import com.example.studentenkotapp.Model.Student;
import com.example.studentenkotapp.data.DbHelper;
import com.example.studentenkotapp.dialog.ReserveerDialog;

import java.util.ArrayList;
import java.util.List;

public class InfoActivity extends AppCompatActivity {



    TextView _infoAdres, _infoKamers, _infoBKamers, _infoKotStudents;
    Button _btnTerug, _btnReserveer;

    public InfoActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        _infoAdres = findViewById(R.id.tvInfoAdres);
        _infoKamers = findViewById(R.id.tvInfoKamers);
        _infoBKamers = findViewById(R.id.tvInfoBezet);
        _infoKotStudents = findViewById(R.id.tvInfoStudenten);
        _btnTerug = findViewById(R.id.btnInfoTerug);
        _btnReserveer = findViewById(R.id.btnReserveer);

     final KotHuis kotHuis = getIntent().getParcelableExtra("kotSelect");
    final Student student = getIntent().getParcelableExtra("Student");

        _infoAdres.setText("Adres: \n"+kotHuis.getAdres()+" "+kotHuis.getHuisnr()+"\n"+ "8500  "+kotHuis.getGemeente());
        _infoKamers.setText("Aantal kamers:  "+kotHuis.getAantalKamers());
        _infoBKamers.setText("Nog beschikbare kamers:  "+ beschikbaar(kotHuis));
        loadStudents();

        _btnTerug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            finish();
            }
        });

        _btnReserveer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (student.getKotID() == null) {
                            openDialog(kotHuis,student);
                        }else {
                            Toast.makeText(getApplicationContext(),"U hebt al een kot gereserveerd. \n Verwijder eerst uw gereserveerd kot!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public String beschikbaar(KotHuis kotHuis){
        int k = kotHuis.getAantalKamers();
        int b = kotHuis.getAantalKamersBezet();
        int free = k - b;
        return String.valueOf(free);

    }
    public void openDialog(KotHuis kotHuis,Student student){

        ReserveerDialog reserveerDialog = new ReserveerDialog(kotHuis,student);
        reserveerDialog.show(getSupportFragmentManager(),"YesNo");

    }

    public void loadStudents(){

        KotHuis kot = getIntent().getParcelableExtra("kotSelect");
        DbHelper dbHelper = new DbHelper(this);
      List<Student> Students=  dbHelper.GetKotMembers(kot.getId());
       Student kotstudent;
       String kotMembers = "";
       if (!Students.isEmpty()){

           for (int i = 0; i < Students.size(); i++){
               kotstudent = Students.get(i);
               kotMembers = kotMembers + kotstudent.getVoorNaam()+"  "+kotstudent.getAchterNaam()+ "  " + kotstudent.getStudieRichting()+"\n";
           }
        _infoKotStudents.setText(kotMembers);
       }else {
           _infoKotStudents.setText("Nog geen medestudenten in dit kot");
       }
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
