package com.smarttersstudio.crimemanagementsystem;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarttersstudio.crimemanagementsystem.pojo.Own;
import com.smarttersstudio.crimemanagementsystem.viewholder.MyCrimeViewHolder;
import com.smarttersstudio.crimemanagementsystem.viewholder.MyMissingViewHolder;

public class MyMissingActivity extends AppCompatActivity {
    private FirebaseRecyclerAdapter<Own,MyMissingViewHolder> f;
    private DatabaseReference userRef;
    private RecyclerView list;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_missing);
        list=findViewById(R.id.my_missing_list);
        mAuth=FirebaseAuth.getInstance();
        userRef= FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
        FirebaseRecyclerOptions<Own> options=new FirebaseRecyclerOptions.Builder<Own>().setQuery(userRef.child("missing"),Own.class).build();
        f=new FirebaseRecyclerAdapter<Own, MyMissingViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MyMissingViewHolder holder, int position, @NonNull Own model) {
                DatabaseReference crimeRef=FirebaseDatabase.getInstance().getReference().child("missing").child(model.getKey());
                crimeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        holder.setDate(dataSnapshot.child("date").getValue().toString());
                        holder.setPin(dataSnapshot.child("pin").getValue().toString());
                        holder.setStatus(dataSnapshot.child("status").getValue().toString());
                        holder.setAge(dataSnapshot.child("age").getValue().toString());
                        holder.setGender(dataSnapshot.child("gender").getValue().toString());
                        holder.setImage(dataSnapshot.child("image").getValue().toString(),getApplicationContext());
                        holder.setName(dataSnapshot.child("name").getValue().toString());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @NonNull
            @Override
            public MyMissingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new MyMissingViewHolder(LayoutInflater.from(getApplicationContext()).inflate(R.layout.my_missing_row,parent,false));
            }
        };
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        list.setAdapter(f);

    }

    @Override
    protected void onStart() {
        super.onStart();
        f.startListening();
    }
    public void goToAddMyMissing(View view) {
        startActivity(new Intent(this,AddMyMissingActivity.class));
    }

}
