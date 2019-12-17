package com.example.studentenkotapp.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.studentenkotapp.Model.KotHuis;
import com.example.studentenkotapp.Model.Student;
import com.example.studentenkotapp.NavigateActivity;
import com.example.studentenkotapp.data.DbHelper;

public class ReserveerDialog extends AppCompatDialogFragment {

    KotHuis kotHuis;
    Student student;

    public ReserveerDialog() {
    }

    public ReserveerDialog(KotHuis kotHuis,Student student) {
        this.kotHuis = kotHuis;
        this.student = student;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Reserveren")
                .setMessage("Bent u zeker dat u dit kot wilt reserveren?")
                .setPositiveButton("JA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {


                Dialog dialog1 = (Dialog) dialogInterface;
                Context context = dialog1.getContext();
                DbHelper dbHelper = new DbHelper(context);
                dbHelper.Reserveren(kotHuis,student);

                Intent intent = new Intent(getContext(), NavigateActivity.class);
                intent.putExtra("LogStudent",student);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                // refresh ????????????????? TODO
               // getActivity().finish();

                Toast.makeText(context,"Kot succesvol gereserveerd!",Toast.LENGTH_LONG).show();

            }
        }).setNegativeButton("Nee", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }

}
