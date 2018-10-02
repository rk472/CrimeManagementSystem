package com.smarttersstudio.crimemanagementsystem;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smarttersstudio.crimemanagementsystem.pojo.Crime;
import com.smarttersstudio.crimemanagementsystem.pojo.Own;
import com.smarttersstudio.crimemanagementsystem.viewholder.MyCrimeViewHolder;

public class CrimeActivity extends AppCompatActivity {
    private RecyclerView list;
    private FirebaseRecyclerAdapter<Crime,MyCrimeViewHolder> f;
    private DatabaseReference childRef;
    private EditText searchText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime);
        list=findViewById(R.id.crime_list);
        childRef= FirebaseDatabase.getInstance().getReference();
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        list.setAdapter(refresh(""));
        searchText=findViewById(R.id.crime_search);
    }
    FirebaseRecyclerAdapter<Crime,MyCrimeViewHolder> refresh(String s){
        Query q;
        if(TextUtils.isEmpty(s))
            q=childRef.child("missing");
        else
            q=childRef.child("missing").startAt(s).orderByChild("pin").endAt(s+"\uf8ff");
        FirebaseRecyclerOptions<Crime> options=new FirebaseRecyclerOptions.Builder<Crime>().setQuery(q,Crime.class).build();
        f=new FirebaseRecyclerAdapter<Crime, MyCrimeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MyCrimeViewHolder holder, int position, @NonNull Crime model) {
                if(model.getStatus().equalsIgnoreCase("solved")){
                    holder.setInvisible();
                }else {
                    holder.setDate(model.getDate());
                    holder.setStatus(model.getStatus());
                    holder.setTitle(model.getTitle());
                    holder.setDesc(model.getDesc());
                    holder.setPin(model.getPin());
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(model.getUid());
                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            holder.setName(dataSnapshot.child("name").getValue().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @NonNull
            @Override
            public MyCrimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new MyCrimeViewHolder(LayoutInflater.from(getApplicationContext()).inflate(R.layout.my_crime_row,parent,false));
            }
        };
        f.startListening();
        return f;
    }

    @Override
    protected void onStart() {
        super.onStart();
        f.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_icon_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_login){
            startActivity(new Intent(this,LoginActivity.class));
        }
        return  true;
    }

    public void searchCrime(View view) {
        list.setAdapter(refresh(searchText.getText().toString()));
    }
}
