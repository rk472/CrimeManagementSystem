package com.smarttersstudio.rescuenation.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smarttersstudio.rescuenation.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyMissingViewHolder extends RecyclerView.ViewHolder {
    private CircleImageView image;
    private TextView nameText,ageText,dateText,genderText,statusText,pinText;
    public Button callButton;
    private View v;
    public MyMissingViewHolder(View itemView) {
        super(itemView);
        image=itemView.findViewById(R.id.my_missing_row_image);
        nameText=itemView.findViewById(R.id.my_missing_row_name);
        ageText=itemView.findViewById(R.id.my_missing_row_age);
        dateText=itemView.findViewById(R.id.my_missing_row_date);
        genderText=itemView.findViewById(R.id.my_missing_row_gender);
        statusText=itemView.findViewById(R.id.my_missing_row_status);
        pinText=itemView.findViewById(R.id.my_missing_row_pin);
        callButton=itemView.findViewById(R.id.missing_call_button);
        v=itemView;
    }
    public void setInvisible(){
        v.setLayoutParams(new LinearLayout.LayoutParams(0,0));
    }
    public void setName(String name){nameText.setText(name);}
    public void setAge(String age){ageText.setText(age);}
    public void setImage(String url, Context c){
        Picasso.with(c).load(url).into(image);
    }
    public void setDate(String date){
        dateText.setText(date);
    }
    public void setGender(String gender){
        genderText.setText(gender);
    }
    public void setStatus(String status){
        statusText.setText(status);
    }
    public void setPin(String pin){
        pinText.setText(pin);
    }
}
