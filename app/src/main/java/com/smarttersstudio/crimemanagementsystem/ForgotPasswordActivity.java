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

public class ForgotPasswordActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailtext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mAuth=FirebaseAuth.getInstance();
        emailtext=findViewById(R.id.forgot_email);
    }

    public void reset(View view) {
        String email=emailtext.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email can't be blank", Toast.LENGTH_SHORT).show();
        }else{
            final ProgressDialog p=new ProgressDialog(this);
            p.setMessage("Please wait while we are logging you in");
            p.setTitle("Please wait");
            p.setCancelable(false);
            p.setCanceledOnTouchOutside(false);
            p.show();
            mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    p.dismiss();
                    Toast.makeText(ForgotPasswordActivity.this,"Password reset mail successfully sent to your mail", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    p.dismiss();
                    Toast.makeText(ForgotPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
