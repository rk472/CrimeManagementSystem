package com.smarttersstudio.rescuenation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.smarttersstudio.rescuenation.pojo.Missing;
import com.smarttersstudio.rescuenation.viewholder.MyMissingViewHolder;

public class MissingActivity extends AppCompatActivity {
    private RecyclerView list;
    private FirebaseRecyclerAdapter<Missing, MyMissingViewHolder> f;
    private DatabaseReference childRef;
    private EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing);
        list = findViewById(R.id.missing_list);
        searchText = findViewById(R.id.missing_search);
        childRef = FirebaseDatabase.getInstance().getReference();
        list.setAdapter(getAdapter(""));
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    FirebaseRecyclerAdapter<Missing, MyMissingViewHolder> getAdapter(String s) {
        Query q;
        if (TextUtils.isEmpty(s))
            q = childRef.child("missing");
        else
            q = childRef.child("missing").startAt(s).orderByChild("pin").endAt(s + "\uf8ff");
        FirebaseRecyclerOptions<Missing> options = new FirebaseRecyclerOptions.Builder<Missing>().setQuery(q, Missing.class).build();
        f = new FirebaseRecyclerAdapter<Missing, MyMissingViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MyMissingViewHolder holder, int position, @NonNull final Missing model) {
                if (model.getStatus().equalsIgnoreCase("found")) {
                    holder.setInvisible();
                } else {
                    holder.setDate(model.getDate());
                    holder.setPin(model.getPin());
                    holder.setStatus(model.getStatus());
                    holder.setAge(model.getAge());
                    holder.setGender(model.getGender());
                    holder.setImage(model.getImage(), getApplicationContext());
                    holder.setName(model.getName());
                    holder.callButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(Intent.ACTION_CALL);
                            i.setData(Uri.parse("tel:" + model.getPhone()));
                            if (ActivityCompat.checkSelfPermission(MissingActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            startActivity(i);
                        }
                    });
                }
            }

            @NonNull
            @Override
            public MyMissingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new MyMissingViewHolder(LayoutInflater.from(getApplicationContext()).inflate(R.layout.my_missing_row,parent,false));
            }
        };
        f.startListening();
        return f;
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

    public void searchMissing(View view) {
        list.setAdapter(getAdapter(searchText.getText().toString()));
    }
}
