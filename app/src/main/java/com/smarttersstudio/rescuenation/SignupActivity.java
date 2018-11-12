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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private EditText nameText,emailText,phoneText,passText;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth=FirebaseAuth.getInstance();
        userRef= FirebaseDatabase.getInstance().getReference().child("users");
        nameText=findViewById(R.id.signup_name);
        emailText=findViewById(R.id.signup_email);
        phoneText=findViewById(R.id.signup_phone);
        passText=findViewById(R.id.signup_password);
    }

    public void signup(View view) {
        final String name=nameText.getText().toString().trim();
        final String email=emailText.getText().toString().trim();
        final String phone=phoneText.getText().toString().trim();
        String pass=passText.getText().toString().trim();
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(pass) ){
            Toast.makeText(this, "You must fill all the fields ", Toast.LENGTH_SHORT).show();
        }else{
            final ProgressDialog p=new ProgressDialog(this);
            p.setMessage("Please wait while we are creating your account");
            p.setTitle("Please wait");
            p.setCancelable(false);
            p.setCanceledOnTouchOutside(false);
            p.show();
            mAuth.createUserWithEmailAndPassword(email,pass).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    p.dismiss();
                    Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    String uid=authResult.getUser().getUid();
                    Map<String,Object> m=new HashMap<>();
                    m.put("name",name);
                    m.put("email",email);
                    m.put("phone",phone);
                    userRef.child(uid).updateChildren(m).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            p.dismiss();
                            Toast.makeText(SignupActivity.this, "account successfully created", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            p.dismiss();
                            Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

    }
}
