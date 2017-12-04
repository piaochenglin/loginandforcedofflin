package com.klip.android.broadcastbestpractice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class NotificationActivity extends AppCompatActivity {
    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_FROM_ALBUM = 2;
    private static final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 2;
    private Button take_photo;
    private Button choose_from_album;
    private ImageView picture;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        take_photo = (Button) findViewById(R.id.take_photo);
        take_photo.setOnClickListener(takePhotoListener);
        choose_from_album = (Button) findViewById(R.id.choose_from_album);
        choose_from_album.setOnClickListener(chooseFromAlbumListener);

        picture = (ImageView) findViewById(R.id.picture);
    }

    private View.OnClickListener takePhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
            if (outputImage.exists()) {
                outputImage.delete();
            }
            try {
                outputImage.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageUri = FileProvider.getUriForFile(NotificationActivity.this, "com.klip.android.broadcastbestpractice.fileprovider", outputImage);

//                imageUri = Uri.fromFile(outputImage);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, TAKE_PHOTO);
        }
    };

    private View.OnClickListener chooseFromAlbumListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (ActivityCompat.checkSelfPermission(NotificationActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(NotificationActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE);
            } else {
                openAlbum();
            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_FROM_ALBUM:
                if (resultCode == RESULT_OK) {

                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(NotificationActivity.this, getString(R.string.denied_permission), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void openAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_FROM_ALBUM);
    }
}
