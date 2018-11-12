package com.smarttersstudio.rescuenation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private TextView nameText,emailText,phoneText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth=FirebaseAuth.getInstance();
        nameText=findViewById(R.id.profile_name);
        emailText=findViewById(R.id.profile_email);
        phoneText=findViewById(R.id.profile_phone);
        userRef= FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nameText.setText(dataSnapshot.child("name").getValue().toString());
                emailText.setText(dataSnapshot.child("email").getValue().toString());
                phoneText.setText(dataSnapshot.child("phone").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void goToMyMissings(View view) {
        startActivity(new Intent(this,MyMissingActivity.class));
    }

    public void goToMyComplains(View view) {
        startActivity(new Intent(this,MyComplainActivity.class));
    }

    public void goToMyCrimes(View view) {
        startActivity(new Intent(this,MyCrimeActivity.class));
    }

    public void logout(View view) {
        mAuth.signOut();
        finish();
    }
}
