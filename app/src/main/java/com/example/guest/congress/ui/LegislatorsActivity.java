package com.example.guest.congress.ui;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.guest.congress.R;
import com.example.guest.congress.adapters.ListAdapter;
import com.example.guest.congress.models.Legislator;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class LegislatorsActivity extends ListActivity {
    public static final String TAG = LegislatorsActivity.class.getSimpleName();

    private ArrayList<Legislator> mLegislators;

    private ArrayList<String> mLegislatorList;

    private String mZipcode;

    private static ListAdapter mAdapter;



    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legislators);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        mZipcode = extras.getString("zipcode");
        // Toast.makeText(this, mZipcode, Toast.LENGTH_LONG).show();

        Runnable displayInfo = new Runnable() {
            @Override
            public void run() {

                mAdapter = new ListAdapter(LegislatorsActivity.this, mLegislators);
                setListAdapter(mAdapter);
            }
        };

        getLegislators(mZipcode, displayInfo);
    }


    public void getLegislators(String zipcode, final Runnable runnable) {
        String apiKey = "c7dff4303dd844eeaaeebdce9ca1fa8d";

        String legislatorsURL = "https://congress.api.sunlightfoundation.com/legislators/locate?apikey=" + apiKey + "&zip=" + zipcode;

        if (isNetworkAvailable()) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(legislatorsURL)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {

                @Override
                public void onFailure(Request request, IOException e) {
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);

                        if (response.isSuccessful()) {
                            mLegislators = getLegislatorDetails(jsonData);
                            runOnUiThread(runnable);
                        } else {
                            alertUserAboutError();
                        }

                    } catch (IOException e) {
                        Log.e(TAG, getString(R.string.exception_caught), e);
                    } catch (JSONException e) {
                        Log.e(TAG, getString(R.string.exception_caught), e);
                    }
                }
            });
        } else {
            Toast.makeText(this, R.string.Network_Unavailable, Toast.LENGTH_LONG).show();
        }
    }

    private ArrayList<Legislator> getLegislatorDetails(String jsonData) throws JSONException {

        ArrayList<Legislator> legislatorArrayList = new ArrayList<>();

        JSONObject legislatorsData = new JSONObject(jsonData);

        String legislatorsInfo = legislatorsData.getString("results");

        Log.i("legislators Info", legislatorsInfo);
        JSONArray jsonArray = new JSONArray(legislatorsInfo);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonPart = jsonArray.getJSONObject(i);
            String bioguide_id = jsonPart.getString("bioguide_id");
            String firstName = jsonPart.getString("first_name");
            String lastName = jsonPart.getString("last_name");
            String party = jsonPart.getString("party");
            String title = jsonPart.getString("title");
            String email = jsonPart.getString("oc_email");
            String phone = jsonPart.getString("phone");
            String office = jsonPart.getString("office");
            String website = jsonPart.getString("website");

            Legislator thisLegislator = new Legislator(bioguide_id, firstName, lastName, party, title, email, phone, office, website);
            legislatorArrayList.add(thisLegislator);

        }

        return legislatorArrayList;
    }


    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id){
        super.onListItemClick(l, v, position, id);
        Legislator thisLegislator = (Legislator) mAdapter.getItem(position);
        Bundle bundle = new Bundle();
        bundle.putString("bioguide_id", thisLegislator.getBioguideId());
        bundle.putString("first_name", thisLegislator.getFirstName());
        bundle.putString("last_name", thisLegislator.getLastName());
        bundle.putString("party", thisLegislator.getParty());
        bundle.putString("title", thisLegislator.getTitle());
        bundle.putString("oc_email", thisLegislator.getEmail());
        bundle.putString("phone", thisLegislator.getPhone());
        bundle.putString("office", thisLegislator.getOffice());
        bundle.putString("website", thisLegislator.getWebsite());
        bundle.putString("zipcode", mZipcode);

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }

}
