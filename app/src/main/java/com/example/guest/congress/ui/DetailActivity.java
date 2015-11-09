package com.example.guest.congress.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

                // Verify
                PackageManager packageManager = getPackageManager();
                List activities = packageManager.queryIntentActivities(callIntent, PackageManager.MATCH_DEFAULT_ONLY);
                boolean isIntentSafe = activities.size() > 0;

                // Execute
                if (isIntentSafe) {
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

                // Verify
                PackageManager packageManager = getPackageManager();
                List activities = packageManager.queryIntentActivities(mapIntent, PackageManager.MATCH_DEFAULT_ONLY);
                boolean isIntentSafe = activities.size() > 0;

                // Execute
                if (isIntentSafe) {
                    startActivity(mapIntent);
                }
            }
        });
    }

}
