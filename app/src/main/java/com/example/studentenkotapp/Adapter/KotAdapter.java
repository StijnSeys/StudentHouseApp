package com.example.studentenkotapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.example.studentenkotapp.InfoActivity;
import com.example.studentenkotapp.LijstActivity;
import com.example.studentenkotapp.Model.KotHuis;
import com.example.studentenkotapp.Model.Student;
import com.example.studentenkotapp.R;

import java.util.List;

public class KotAdapter extends ArrayAdapter<KotHuis> {

    private List<KotHuis> kotList;
    private Context context;
    private Student student;

    public KotAdapter(List<KotHuis> kotList,Context context,Student student) {
        //wat doet super???
        super(context, R.layout.kotlist_item,kotList );
        this.kotList = kotList;
        this.context = context;
        this.student = student;
    }


    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        if (convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.kotlist_item, parent,false);
        }


        final KotHuis item = kotList.get(position);

        if (item.getAantalKamersBezet()< (item.getAantalKamers()/2)){
            convertView.setBackgroundColor(context.getResources().getColor(R.color.lightgreen));
        }
       else if (item.getAantalKamersBezet()== item.getAantalKamers()){
            convertView.setBackgroundColor(context.getResources().getColor(R.color.lightred));
        }else {
           convertView.setBackgroundColor(context.getResources().getColor(R.color.lightorange));
        }
        TextView tv = convertView.findViewById(R.id.tvinfo);
       tv.setText(item.getAdres()+" "+item.getHuisnr()+"  Bezetting: "+ item.getAantalKamersBezet()+"/"+item.getAantalKamers() );


       Button btnInfo =  convertView.findViewById(R.id.btninfo);
        final View finalConvertView = convertView;
        btnInfo.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(parent.getContext(), InfoActivity.class);
               intent.putExtra("kotSelect", item);
               intent.putExtra("Student", student);
               finalConvertView.getContext().startActivity(intent);
           }
       });

       return convertView;
    }
}
