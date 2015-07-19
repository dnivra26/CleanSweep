package io.github.dnivra26.cleansweep;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import io.github.dnivra26.cleansweep.models.Bid;
import io.github.dnivra26.cleansweep.models.Issue;

@EActivity(R.layout.activity_new_issue)
public class NewIssueActivity extends AppCompatActivity implements LocationListener {


    ParseFile issueImageFile;

    @ViewById(R.id.issue_image)
    ImageView issueImage;

    @ViewById(R.id.issue_title)
    EditText issueTitle;

    @ViewById(R.id.issue_description)
    EditText issueDescription;

    @ViewById(R.id.issue_location)
    EditText issueLocation;

    @ViewById(R.id.initial_bid)
    EditText initialBid;


    @Click(R.id.issue_image)
    public void takePicture() {
        String address = "";
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
        ParseGeoPoint geoPoint = getGeoPoint();
        if (geoPoint.getLongitude() > 1) {
            address = convertToAddress(geoPoint);
            issueLocation.setText(address);
            // issueLocation.setText(geoPoint.getLatitude() + "," + geoPoint.getLongitude());
        }
    }


    public String convertToAddress(ParseGeoPoint geoPoint) {
        String address = null;
        Geocoder geocoder;
        geocoder = new Geocoder(this);
        String city = null;
        try {
            List<Address> fromLocation = geocoder.getFromLocation(geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
            address = fromLocation.get(0).getAddressLine(0);
            city = fromLocation.get(0).getAddressLine(1);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return address + "," + city;
    }

    public ParseGeoPoint getGeoPoint() {

        boolean isNetworkEnabled = false;
        boolean isGPSEnabled = false;
        Location location = null;
        double latitude, longitude;
        ParseGeoPoint point = new ParseGeoPoint();
        LocationManager locationmanager = null;
        locationmanager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        isGPSEnabled = locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


        if (isGPSEnabled) {

            locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

            if (locationmanager != null) {
                location = locationmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    point.setLongitude(longitude);
                    point.setLatitude(latitude);
                } else {
                    return point;
                }
            }

        }


        return point;


    }

    @Click(R.id.create_issue)
    public void createNewIssue() {
        final ProgressDialog progressDialog = UiUtil.buildProgressDialog(this);
        progressDialog.show();

        final Issue newIssue = new Issue();
        newIssue.setTitle(issueTitle.getText().toString());
        newIssue.setDescription(issueDescription.getText().toString());
        newIssue.setLocation(issueLocation.getText().toString());
        newIssue.setBid(Long.valueOf(initialBid.getText().toString()));



        issueImageFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    newIssue.setPhotoFile(issueImageFile);
                    newIssue.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Bid bid = new Bid();
                                bid.setBid(newIssue.getBid());
                                bid.setUser(ParseUser.getCurrentUser());
                                bid.setParent(newIssue);

                                bid.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            Toast.makeText(NewIssueActivity.this, "New Issue created!", Toast.LENGTH_LONG).show();
                                            finish();
                                        } else {
                                            Toast.makeText(NewIssueActivity.this, "Issue creation failed", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(NewIssueActivity.this, "Issue creation failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(NewIssueActivity.this, "Issue creation failed", Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && data.getExtras() != null) {

            Bitmap bp = (Bitmap) data.getExtras().get("data");
            issueImage.setImageBitmap(bp);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();


            issueImageFile = new ParseFile("issue_image.jpg", byteArray);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_issue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
