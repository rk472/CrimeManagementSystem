package com.smarttersstudio.crimemanagementsystem;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddMyComplainActivity extends AppCompatActivity {
    private EditText titleText,descText,pinText,phoneText;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef,crimeRef;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_complain);
        titleText=findViewById(R.id.complain_title);
        descText=findViewById(R.id.complain_desc);
        pinText=findViewById(R.id.complain_pincode);
        phoneText=findViewById(R.id.complain_phone);
        mAuth=FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        userRef= FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("complain");
        crimeRef=FirebaseDatabase.getInstance().getReference().child("complain");
    }

    public void addComplain(View view) {
        String title=titleText.getText().toString().trim();
        String desc=descText.getText().toString().trim();
        String pin=pinText.getText().toString().trim();
        String phone=phoneText.getText().toString();
        if(TextUtils.isEmpty(title) || TextUtils.isEmpty(desc) || phone.isEmpty() || TextUtils.isEmpty(pin) ){
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
        }else{
            final ProgressDialog p=new ProgressDialog(this);
            p.setMessage("Please wait while we are submitting your query");
            p.setTitle("Please wait");
            p.setCancelable(false);
            p.setCanceledOnTouchOutside(false);
            p.show();
            Map<String,Object> m=new HashMap<>();
            m.put("title",title);
            m.put("desc",desc);
            m.put("pin",pin);
            m.put("uid",uid);
            m.put("phone",phone);
            m.put("status","submitted");
            SimpleDateFormat s=new SimpleDateFormat("dd mm yyyy hh mm");
            String date=s.format(new Date());
            m.put("date",date);
            final String key=crimeRef.push().getKey();
            crimeRef.child(key).updateChildren(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    p.dismiss();
                    userRef.push().child("key").setValue(key).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            p.dismiss();
                            Toast.makeText(AddMyComplainActivity.this, "Query successfully submitted", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            p.dismiss();
                            Toast.makeText(AddMyComplainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddMyComplainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
