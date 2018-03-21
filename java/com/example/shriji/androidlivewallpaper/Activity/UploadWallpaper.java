package com.example.shriji.androidlivewallpaper.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shriji.androidlivewallpaper.Common.Common;
import com.example.shriji.androidlivewallpaper.Model.CategoryItem;
import com.example.shriji.androidlivewallpaper.Model.WallpaperItems;
import com.example.shriji.androidlivewallpaper.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadWallpaper extends AppCompatActivity {

    @BindView(R.id.image_preview)
    ImageView imagePreview;
    @BindView(R.id.spinner)
    MaterialSpinner spinner;
    @BindView(R.id.btn_browser)
    Button btnBrowser;
    @BindView(R.id.btn_upload)
    Button btnUpload;

    private Uri filePath;
    String categoryIdSelected = "";

    //Matrial Spinner Data
    Map<String,String> spinnerData = new HashMap<>();

    //FireBase Storage
    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_wallpaper);
        ButterKnife.bind(this);

        //firebase Storage Init
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //load Spinner Data
        loadCategorySpinner();

        btnBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();

                if(spinner.getSelectedIndex() == 0) // hint, not choose anymore
                    Toast.makeText(UploadWallpaper.this, "please choose category", Toast.LENGTH_SHORT).show();
                else
                    upload();
            }
        });



    }

    private void loadCategorySpinner() {
        FirebaseDatabase.getInstance()
                .getReference(Common.STR_CATEGORY_BACKGROUND)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                            CategoryItem item = postSnapshot.getValue(CategoryItem.class);
                            String key = postSnapshot.getKey();
                            spinnerData.put(key,item.getName());

                        }

                        //matrial spinner will not receive hint so we need custom hint
                        //trick:
                        Object[] valueArray =  spinnerData.values().toArray();
                        List<Object> valueList = new ArrayList<>();
                        valueList.add(0,"Category");//we will add first item in
                        valueList.addAll(Arrays.asList(valueArray));
                        spinner.setItems(valueList); // set source data for spinner
                        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                                //when user choose  category ,we will get categoryID(key)
                                Object[] keyArray =  spinnerData.keySet().toArray();
                                List<Object> keyList = new ArrayList<>();
                                keyList.add(0,"Category_Key");//we will add first item in
                                keyList.addAll(Arrays.asList(keyArray));
                                spinner.setItems(keyList); // set source data for spinner
                                categoryIdSelected = keyList.get(position).toString(); //assign key when user choose category
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

   /* @OnClick({R.id.btn_browser, R.id.btn_upload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_browser:

                break;
            case R.id.btn_upload:

                break;
        }
    }*/

    private void upload() {
        if(filePath != null){
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference ref = storageReference
                    .child(new StringBuilder("images/").append(UUID.randomUUID().toString()).toString());
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    saveUrlToCategory(categoryIdSelected,taskSnapshot.getDownloadUrl().toString());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(UploadWallpaper.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Upload: "+(int)progress+"%");
                }
            });

        }
    }

    private void saveUrlToCategory(String categoryIdSelected, String imageLink) {
        FirebaseDatabase.getInstance()
                .getReference(Common.STR_WALLPAPER)
                .push()   //gen key
                .setValue(new WallpaperItems(imageLink,categoryIdSelected))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UploadWallpaper.this, "Success!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture: "),Common.PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Error", "onActivityResult1: "+(requestCode == Common.PICK_IMAGE_REQUEST)+" Data: "+data );
        Log.e("Error", "onActivityResult2: "+(requestCode == RESULT_OK)+" Data: "+data );
        Log.e("Error", "onActivityResult3: "+" Data: "+data );
        Log.e("Error", "onActivityResult4: "+data.getData() );
        if(requestCode == Common.PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null){

            filePath = data.getData();
            Log.e("Error", "onActivityResult: "+filePath);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                Log.e("Error", "onActivityResult: "+bitmap);

                imagePreview.setImageBitmap(bitmap);
                btnUpload.setEnabled(true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
