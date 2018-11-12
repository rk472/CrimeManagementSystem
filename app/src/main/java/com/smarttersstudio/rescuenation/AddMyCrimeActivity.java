package com.smarttersstudio.rescuenation;

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

public class AddMyCrimeActivity extends AppCompatActivity {
    private EditText titleText,descText,pinText,phoneText;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef,crimeRef;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_crime);
        titleText=findViewById(R.id.crime_title);
        descText=findViewById(R.id.crime_desc);
        phoneText=findViewById(R.id.crime_phone);
        pinText=findViewById(R.id.crime_pincode);
        mAuth=FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        userRef= FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("crime");
        crimeRef=FirebaseDatabase.getInstance().getReference().child("crime");
    }

    public void addComplain(View view) {
        String title=titleText.getText().toString().trim();
        String desc=descText.getText().toString().trim();
        String pin=pinText.getText().toString().trim();
        String phone=phoneText.getText().toString().trim();
        if(TextUtils.isEmpty(title) || phone.isEmpty() || TextUtils.isEmpty(desc) || TextUtils.isEmpty(pin) ){
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
            m.put("phone",phone);
            m.put("uid",uid);
            m.put("status","submitted");
            SimpleDateFormat s=new SimpleDateFormat("dd:MM:yyyy hh:mm");
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
                            Toast.makeText(AddMyCrimeActivity.this, "Query successfully submitted", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            p.dismiss();
                            Toast.makeText(AddMyCrimeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddMyCrimeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
