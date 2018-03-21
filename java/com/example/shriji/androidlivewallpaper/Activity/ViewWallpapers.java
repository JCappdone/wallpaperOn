package com.example.shriji.androidlivewallpaper.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shriji.androidlivewallpaper.Common.Common;
import com.example.shriji.androidlivewallpaper.Database.DataSource.RecentRepository;
import com.example.shriji.androidlivewallpaper.Database.LocalDataBase.LocalDataBase;
import com.example.shriji.androidlivewallpaper.Database.LocalDataBase.RecentsDataSource;
import com.example.shriji.androidlivewallpaper.Database.Recents;
import com.example.shriji.androidlivewallpaper.Helper.SaveImageHelper;
import com.example.shriji.androidlivewallpaper.Model.WallpaperItems;
import com.example.shriji.androidlivewallpaper.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ViewWallpapers extends AppCompatActivity {

    @BindView(R.id.imgThumb)
    ImageView imgThumb;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing)
    CollapsingToolbarLayout collapsing;
    @BindView(R.id.txtDesc)
    TextView txtDesc;
    @BindView(R.id.fabWallpaper)
    FloatingActionButton fabWallpaper;
    @BindView(R.id.rootLayout)
    CoordinatorLayout rootLayout;
    @BindView(R.id.fabDownload)
    FloatingActionButton fabDownload;

    //Room Database
    CompositeDisposable compositeDisposable;
    RecentRepository recentRepository;
    @BindView(R.id.fb_share)
    com.github.clans.fab.FloatingActionButton fbShare;
    @BindView(R.id.mainFloating)
    FloatingActionMenu mainFloating;

    //facebook
    CallbackManager callbackManager;
    ShareDialog shareDialog;


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Common.PERMISION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    AlertDialog dialog = new SpotsDialog(ViewWallpapers.this);

                    dialog.setMessage("Please Wait...");

                    String fileName = UUID.randomUUID().toString() + ".png";
                    Picasso.get()
                            .load(Common.select_background.getImgLink())
                            .into(new SaveImageHelper(ViewWallpapers.this,
                                    dialog,
                                    getApplicationContext().getContentResolver(),
                                    fileName,
                                    "Live Wallpaper Image"));
                    dialog.show();
                } else
                    Toast.makeText(this, "Need Permission To Download", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }


    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(ViewWallpapers.this);
            try {
                wallpaperManager.setBitmap(bitmap);
                Snackbar.make(rootLayout, "Wallpaper was Set", Snackbar.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    private Target facebookCpnvertBitmap = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            SharePhoto sharePhoto = new SharePhoto.Builder().setBitmap(bitmap).build();
            if(ShareDialog.canShow(SharePhotoContent.class)){
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(sharePhoto).build();
                shareDialog.show(content);
            }
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wallpapers);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //  getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //init facebook
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);



        //init RoomDatabse
        compositeDisposable = new CompositeDisposable();
        LocalDataBase dataBase = LocalDataBase.getInstance(this);
        recentRepository = RecentRepository
                .getInstance(RecentsDataSource.getInstance(dataBase.recentsDAO()));

        collapsing.setTitle(Common.CATEGORY_SELECTED);
        Picasso.get()
                .load(Common.select_background.getImgLink())
                .into(imgThumb);

        //add to recents
        addToRecent();

        fbShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //create callback
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(ViewWallpapers.this, "Share Successful", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(ViewWallpapers.this, "Share Canceled !", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(ViewWallpapers.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

                //we will fetch photo from link and convert to bitmap
                Picasso.get()
                        .load(Common.select_background.getImgLink())
                        .into(facebookCpnvertBitmap);
            }
        });

        fabWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.get()
                        .load(Common.select_background.getImgLink())
                        .into(target);
            }
        });

        fabDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ViewWallpapers.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                Common.PERMISION_REQUEST_CODE);
                    }
                } else {
                    AlertDialog dialog = new SpotsDialog(ViewWallpapers.this);
                    dialog.show();
                    dialog.setMessage("Please Wait...");

                    String fileName = UUID.randomUUID().toString() + ".png";
                    Picasso.get()
                            .load(Common.select_background.getImgLink())
                            .into(new SaveImageHelper(ViewWallpapers.this,
                                    dialog,
                                    getApplicationContext().getContentResolver(),
                                    fileName,
                                    "Live Wallpaper Image"));
                }


            }
        });
        //View Count
        increaseViewCount();

    }

    private void increaseViewCount() {
        FirebaseDatabase.getInstance()
                .getReference(Common.STR_WALLPAPER)
                .child(Common.select_background_key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("viewCount")) {
                            WallpaperItems wallpaperItems = dataSnapshot.getValue(WallpaperItems.class);
                            long count = wallpaperItems.getViewCount() + 1;
                            //Update
                            Map<String, Object> update_view = new HashMap<>();
                            update_view.put("viewCount", count);

                            FirebaseDatabase.getInstance()
                                    .getReference(Common.STR_WALLPAPER)
                                    .child(Common.select_background_key)
                                    .updateChildren(update_view)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {


                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ViewWallpapers.this, "Cannot update view count", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else //if view count is not set
                        {
                            Map<String, Object> update_view = new HashMap<>();
                            update_view.put("viewCount", Long.valueOf(1));

                            FirebaseDatabase.getInstance()
                                    .getReference(Common.STR_WALLPAPER)
                                    .child(Common.select_background_key)
                                    .updateChildren(update_view)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {


                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ViewWallpapers.this, "Cannot set default view count", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void addToRecent() {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                Recents recents = new Recents(
                        Common.select_background.getImgLink(),
                        Common.select_background.getCategoryID(),
                        String.valueOf(System.currentTimeMillis()),
                        Common.select_background_key);
                recentRepository.insertRecents(recents);
                e.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {

                    @Override
                    public void accept(Object o) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("ERROR", throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                });

        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        Picasso.get().cancelRequest(target);
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); //close activity when click back button
        }
        return super.onOptionsItemSelected(item);

    }
}
