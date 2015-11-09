package com.example.guest.congress.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guest.congress.R;
import com.koushikdutta.ion.Ion;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        final String number = bundle.getString("phone");

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
                // Build the intent
                Uri phoneNumber = Uri.parse("tel:" + number);
                Intent callIntent = new Intent(Intent.ACTION_DIAL, phoneNumber);

                PackageManager packageManager = getPackageManager();
                List activities = packageManager.queryIntentActivities(callIntent, PackageManager.MATCH_DEFAULT_ONLY);
                boolean isIntentSafe = activities.size() > 0;

                if(isIntentSafe) {
                    startActivity(callIntent);
                }
            }
        });
    }

}
