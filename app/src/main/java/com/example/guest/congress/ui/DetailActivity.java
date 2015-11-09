package com.example.guest.congress.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guest.congress.R;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    @Bind(R.id.photoImage)ImageView mPhotoImage;
    @Bind(R.id.titleText)TextView mTitleText;
    @Bind(R.id.firstNameText) TextView mFirstNameText;
    @Bind(R.id.lastNameText) TextView mLastNameText;
    @Bind(R.id.partyText) TextView mPartyText;
    @Bind(R.id.emailText) TextView mEmailText;
    @Bind(R.id.phoneText) TextView mPhoneText;
    @Bind(R.id.officeText) TextView mOfficeText;
    @Bind(R.id.websiteText) TextView mWebsiteText;
    @Bind(R.id.addressButton) Button mAddressButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        final String number = bundle.getString("phone");
        final String address = bundle.getString("office");
        final String zipcode = bundle.getString("zipcode");
        final String website = bundle.getString("website");

        mTitleText.setText(bundle.getString("title"));
        mFirstNameText.setText(bundle.getString("first_name"));
        mLastNameText.setText(bundle.getString("last_name"));
        mPartyText.setText("( "+ bundle.getString("party") + " )");
        mEmailText.setText(bundle.getString("oc_email"));
        mPhoneText.setText(bundle.getString("phone"));
        mOfficeText.setText(bundle.getString("office"));
        mWebsiteText.setText(bundle.getString("website"));

        String photoUrl = "https://theunitedstates.io/images/congress/225x275/" + bundle.getString("bioguide_id") + ".jpg";
        Ion.with(mPhotoImage)
                //.placeholder(R.drawable.person)
                .load(photoUrl);


        mPhoneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri phoneNumber = Uri.parse("tel:" + number);
                Intent callIntent = new Intent(Intent.ACTION_DIAL, phoneNumber);

                // Execute
                if (isIntentSafe(callIntent)) {
                    startActivity(callIntent);
                }
            }
        });

        mAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addressWithPlus = (address.replace(" ", "+"));
                Uri address = Uri.parse("geo:0,0?q=" + addressWithPlus + ",+" + zipcode);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, address);

                // Execute
                if (isIntentSafe(mapIntent)) {
                    startActivity(mapIntent);
                }
            }
        });

        mWebsiteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri websiteLink = Uri.parse(website);
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, websiteLink);

                // Execute
                if (isIntentSafe(websiteIntent)) {
                    startActivity(websiteIntent);
                }
            }
        });

        mEmailText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();



            }
        });
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Log.d(TAG, "Could not create an image file.");
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            Log.d(TAG, "We're inside onActivityResult!");
//
//        }
//    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        //Create image file name
        String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        //Save file path
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private boolean isIntentSafe(Intent intent) {
        PackageManager packageManager = getPackageManager();
        List activities = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return activities.size() > 0;
    }

}
