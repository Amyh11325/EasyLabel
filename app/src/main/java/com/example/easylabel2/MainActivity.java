
package com.example.easylabel2;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

//    All the variables related to capturing the image
private static final String TAG = "MainActivity";
    public static final int REQUEST_IMAGE_CAPTURE = 101;
    Button captureButton;
    boolean picTaken_marker;
    TextView t1;
    ImageView picTaken;
    private static final int Permission_Code = 1000;
    Uri image_uri;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    ArrayList<String> finalIngredientList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //--------------
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        captureButton = findViewById(R.id.button);
        t1 = findViewById(R.id.textView);
        captureButton.setTag(1);
        captureButton.setText("Take a Pic");
        picTaken = findViewById(R.id.imageView);


        captureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_DENIED) {
//                        Permission not enabled so we ask for it
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//                        Show pop up to request permission
                        requestPermissions(permission, Permission_Code);

                    } else {
                        // permission granted
                        openCamera();

                    }

                } else {
                    // System os < marshmallow
                    openCamera();
                }

            }
        });
    }

    public void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(camIntent, IMAGE_CAPTURE_CODE);
        captureButton.setId(0);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Permission_Code: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    // When image is captured from the camera

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == RESULT_OK) {
            picTaken.setImageURI(image_uri);
//            openActivity2();

            captureButton.setText("It looks good.");
            captureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageToText();
                    openActivity2();

                }

            });

        }

    }

    public void openActivity2() {
//        Intent for Second Activity
        Intent act2 = new Intent(this, DetailActivity.class);
        act2.putExtra("finalIngredientsList", finalIngredientList);

//        Start the next activity
        startActivity(act2);
    }


    //    -----------------------------------
    private void imageToText()  {
        Context context = getApplicationContext();

//    TODO: Creating the textRecognizer
        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();

//    TODO: Check if the TextRecognizer is operational
        if (!textRecognizer.isOperational()) {
        Log.w(TAG, "Detector dependencies are not yet available.");



            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
            Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
            Log.w(TAG, getString(R.string.low_storage_error));


            }
        }
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Frame outputFrame = new Frame.Builder().setBitmap(bitmap).build();

        SparseArray<TextBlock> ingredients = textRecognizer.detect(outputFrame);
        String result = "";
        for(int i = 0; i < ingredients.size(); i++) {

            result+= ingredients.valueAt(i).getValue();
            finalIngredientList.add(i,ingredients.valueAt(i).getValue());
        }
        t1.setText(result);
    }


}