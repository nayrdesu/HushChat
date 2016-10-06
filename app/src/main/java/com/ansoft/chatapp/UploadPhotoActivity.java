package com.ansoft.chatapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ansoft.chatapp.Constants.PC;
import com.ansoft.chatapp.Utils.CircleImageView;
import com.ansoft.chatapp.Utils.CircleTransform;
import com.ansoft.chatapp.Utils.LoadingAlertDialog;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class UploadPhotoActivity extends AppCompatActivity {

    CircleImageView photo;
    Button btnNext;
    private Toolbar toolbar;

    static int GET_PICTURE = 1, CAMERA_PIC_REQUEST = 2, PIC_CROP = 4, CAMERA_CROP = 6;
    SharedPreferences pref;
    Uri outputFileUri=null;
    int n = 10000;
    Bitmap bmp;
    static String selectedImagePath = null;
    byte b[];
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LoadingAlertDialog alert = new LoadingAlertDialog(UploadPhotoActivity.this);
        setContentView(R.layout.activity_upload_photo);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Upload Photo");
        photo = (CircleImageView) findViewById(R.id.photo);
        ParseFile file = ParseUser.getCurrentUser().getParseFile(PC.KEY_PROFILE_PHOTO);
        if (file == null) {
        } else {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
             }
        btnNext = (Button) findViewById(R.id.nextButton);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GET_PICTURE);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.show();
                if (bmp != null) {
                    byte[] bitmapdata;
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                    bitmapdata = stream.toByteArray();
                    final ParseFile imgfile = new ParseFile(PC.KEY_PHOTO_NAME, bitmapdata);
                    imgfile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                ParseUser.getCurrentUser().put(PC.KEY_PROFILE_PHOTO, imgfile);
                                ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            alert.dismiss();
                                            Intent in = new Intent(getApplicationContext(), FriendsActivity.class);
                                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            in.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            startActivity(in);
                                            finish();
                                        }
                                    }
                                });
                            }
                        }
                    });
                } else {
                    alert.dismiss();
                    Intent in = new Intent(getApplicationContext(), FriendsActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(in);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri selectedImageUri = null;
            if (requestCode == GET_PICTURE) {
                // if (bmp != null) {
                // bmp.recycle();
                // }
                selectedImageUri = data.getData();
                if (selectedImageUri == null) {
                    Toast.makeText(getApplicationContext(), "Invalid image",
                            Toast.LENGTH_SHORT).show();
                } else {
                    performCrop(selectedImageUri, PIC_CROP);
                }
            }

            else if (requestCode == CAMERA_PIC_REQUEST) {

                int n = Integer.parseInt(pref.getString("originalfile", "0"));
                if (n == 0) {
                    Toast.makeText(getApplicationContext(),
                            "Device Error \n Please Try Again",
                            Toast.LENGTH_SHORT).show();
                } else {
                    String root = Environment.getExternalStorageDirectory()
                            .toString();
                    File myDir = new File(root + "/NudgeApp");
                    myDir.mkdirs();
                    String fname = "Image-" + n + ".jpg";
                    File file = new File(myDir, fname);
                    outputFileUri = Uri.fromFile(file);
                    System.out.println(outputFileUri);
                    if (outputFileUri != null) {
                        performCrop(outputFileUri, CAMERA_CROP);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Error \n Try Again", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            } else if (requestCode == PIC_CROP) {
                if (data != null) {
                    // get the returned data
                    Bundle extras = data.getExtras();
                    // get the cropped bitmap
                    Bitmap selectedBitmap = extras.getParcelable("data");
                    if (selectedBitmap != null) {
                        bmp = selectedBitmap;
                        Bitmap finalbit = bmp;
                        if (finalbit != null) {
                            File ff1 = SaveImage(finalbit, n);
                            if (ff1 != null) {
                                selectedImagePath = ff1.getAbsolutePath();
                                System.out.println(selectedImagePath);
                            } else {
                                System.out.println("rotation pblms");
                            }
                        } else {
                            System.out.println("Not Roatates process doine");
                        }
                        if (selectedImagePath == null
                                && selectedImagePath.length() <= 0) {
                            System.out
                                    .println("No selected image path is there..");
                            Toast.makeText(getApplicationContext(),
                                    "Invalid image", Toast.LENGTH_SHORT);
                        } else {
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory
                                    .decodeFile(selectedImagePath, options);
                            options.inSampleSize = calculateInSampleSize(
                                    options, 150, 150);
                            options.inJustDecodeBounds = false;

                            if (bmp != null) {
                                // profilepic.setBackgroundResource(R.drawable.imguploaded);
                                System.out.println("sele path "
                                        + selectedImagePath);
                                ExifInterface ei;
                                try {
                                    ei = new ExifInterface(selectedImagePath);
                                    int orientation = ei.getAttributeInt(
                                            ExifInterface.TAG_ORIENTATION,
                                            ExifInterface.ORIENTATION_NORMAL);
                                    finalbit = bmp;
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();

                                    photo.setImageBitmap(bmp);
                                }

                                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                                bmp.compress(Bitmap.CompressFormat.JPEG, 90,
                                        bout);
                                b = bout.toByteArray();
                                System.out.println(b);
                                image = Base64
                                        .encodeToString(b, Base64.DEFAULT);
                                photo.setImageBitmap(bmp);

                            } else {
                                selectedImagePath = "";
                                Toast.makeText(getApplicationContext(),
                                        "Invalid image", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Invalid Image", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            else if (requestCode == CAMERA_CROP) {
                if (data != null) {
                    // get the returned data
                    Bundle extras = data.getExtras();
                    // get the cropped bitmap
                    Bitmap selectedBitmap = extras.getParcelable("data");
                    if (selectedBitmap == null) {
                        Toast.makeText(getApplicationContext(),
                                "Invalid Image", Toast.LENGTH_SHORT).show();
                    } else {
                        Bitmap finalbit = selectedBitmap;
                        bmp = selectedBitmap;
                        photo.setImageBitmap(bmp);
                    }
                }
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, bout);
                b = bout.toByteArray();
                System.out.println(b);
                image = Base64.encodeToString(b, Base64.DEFAULT);
            }

        }

        // upload to

    }

    private void performCrop(Uri picUri, int ss) {
        try {

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 200);
            cropIntent.putExtra("outputY", 200);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, ss);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast
                    .makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private File SaveImage(Bitmap finalBitmap, int n) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/NudgeApp");
        myDir.mkdirs();
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            return file;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
