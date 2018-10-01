package com.smarttersstudio.crimemanagementsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText emailText,passText;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailText=findViewById(R.id.login_email);
        passText=findViewById(R.id.login_password);
        mAuth=FirebaseAuth.getInstance();
    }

    public void login(View view) {
        String email=emailText.getText().toString().trim();
        String pass=passText.getText().toString().trim();
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
            Toast.makeText(this, "You must fill all the fields", Toast.LENGTH_SHORT).show();
        }else{
            final ProgressDialog p=new ProgressDialog(this);
            p.setMessage("Please wait while we are logging you in");
            p.setTitle("Please wait");
            p.setCancelable(false);
            p.setCanceledOnTouchOutside(false);
            p.show();
            mAuth.signInWithEmailAndPassword(email,pass).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    p.dismiss();
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    p.dismiss();
                    startActivity(new Intent(LoginActivity.this,ProfileActivity.class));
                    finish();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(LoginActivity.this,ProfileActivity.class));
            finish();
        }
    }

    public void goToSignup(View view) {
        startActivity(new Intent(this,SignupActivity.class));
    }

    public void gotoToForgot(View view) {
        startActivity(new Intent(this,ForgotPasswordActivity.class));
    }
}
