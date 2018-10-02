package com.smarttersstudio.crimemanagementsystem.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smarttersstudio.crimemanagementsystem.R;

public class MyCrimeViewHolder extends RecyclerView.ViewHolder {
    private TextView nameText,titleText,statusText,dateText,descText,pinText;
    private View v;
    public MyCrimeViewHolder(View itemView) {
        super(itemView);
        nameText=itemView.findViewById(R.id.my_crime_row_name);
        titleText=itemView.findViewById(R.id.my_crime_row_title);
        statusText=itemView.findViewById(R.id.my_crime_row_status);
        dateText=itemView.findViewById(R.id.my_crime_row_date);
        descText=itemView.findViewById(R.id.my_crime_row_desc);
        pinText=itemView.findViewById(R.id.my_crime_row_pin);
        v=itemView;
    }
    public void setInvisible(){
        v.setLayoutParams(new LinearLayout.LayoutParams(0,0));
    }
    public void setName(String name){
        nameText.setText(name);
    }
    public void setTitle(String title){
        titleText.setText(title);
    }
    public void setStatus(String status){
        statusText.setText(status);
    }
    public void setDate(String date){
        dateText.setText(date);
    }
    public void setDesc(String desc){
        descText.setText(desc);
    }
    public void setPin(String pin){
        pinText.setText(pin);
    }

}
