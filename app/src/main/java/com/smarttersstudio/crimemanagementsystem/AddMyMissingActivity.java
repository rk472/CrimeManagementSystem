package com.smarttersstudio.crimemanagementsystem;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class AddMyMissingActivity extends AppCompatActivity {
    private EditText nameText,ageText,pinText,phoneText;
    private RadioGroup genderSelect;
    private CircleImageView image;
    private Bitmap bitmap;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef,missingRef;
    private String uid;
    private RadioButton male;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_missing);
        nameText=findViewById(R.id.my_missing_name);
        ageText=findViewById(R.id.my_missing_age);
        pinText=findViewById(R.id.my_missing_pin);
        phoneText=findViewById(R.id.my_missing_phone);
        genderSelect=findViewById(R.id.gender_select);
        male=findViewById(R.id.gender_male);
        male.setChecked(true);
        image=findViewById(R.id.my_missing_image);
        mAuth=FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        userRef= FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        missingRef=FirebaseDatabase.getInstance().getReference().child("missing");
    }

    public void uploadPhoto(View view) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(AddMyMissingActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                File thumb_filePath=new File(resultUri.getPath());
                try{
                    bitmap=new Compressor(this)
                            .setMaxHeight(200)
                            .setMaxWidth(200)
                            .setQuality(10)
                            .compressToBitmap(thumb_filePath);
                    image.setImageBitmap(bitmap);
                }catch (Exception e){
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(AddMyMissingActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addMissing(View view) {
        final String name=nameText.getText().toString().trim();
        final String pin=pinText.getText().toString().trim();
        final String age=ageText.getText().toString();
        final String phone=phoneText.getText().toString();
        RadioButton rb=findViewById(genderSelect.getCheckedRadioButtonId());
        final String gender=rb.getText().toString();
        if(bitmap==null){
            Toast.makeText(this, "You must choose a photo first", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(name) || TextUtils.isEmpty(age) || TextUtils.isEmpty(pin) ){
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
        }else {
            final ProgressDialog p = new ProgressDialog(this);
            p.setMessage("Please wait while we are submitting your query");
            p.setTitle("Please wait");
            p.setCancelable(false);
            p.setCanceledOnTouchOutside(false);
            p.show();
            String key=missingRef.push().getKey();
            StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("missing").child(key+".jpg");
            Bitmap b = ((BitmapDrawable) image.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            final byte[] mbyte = byteArrayOutputStream.toByteArray();
            storageReference.putBytes(mbyte).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddMyMissingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("name", name);
                    m.put("image",taskSnapshot.getDownloadUrl().toString());
                    m.put("age", age);
                    m.put("pin", pin);
                    m.put("gender",gender);
                    m.put("uid", uid);
                    m.put("status", "Not Found");
                    m.put("phone",phone);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat s = new SimpleDateFormat("dd mm yyyy hh mm");
                    String date = s.format(new Date());
                    m.put("date", date);
                    final String key = missingRef.push().getKey();
                    missingRef.child(key).updateChildren(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            p.dismiss();
                            userRef.child("missing").push().child("key").setValue(key).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    p.dismiss();
                                    Toast.makeText(AddMyMissingActivity.this, "Query successfully submitted", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    p.dismiss();
                                    Toast.makeText(AddMyMissingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddMyMissingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }
    }
}
