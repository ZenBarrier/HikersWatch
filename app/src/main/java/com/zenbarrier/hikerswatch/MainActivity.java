package com.zenbarrier.hikerswatch;

import android.Manifest;
import android.app.Service;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    String provider;
    Geocoder geocoder;
    TextView infoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infoText = (TextView)findViewById(R.id.infoTextView);

        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
        onLocationChanged(locationManager.getLastKnownLocation(provider));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        double accuracy = location.getAccuracy();
        double speed = location.getSpeed();
        double bearing = location.getBearing();
        double altitude = location.getAltitude();

        String locationInfo = "";

        List<Address> addressList;

        locationInfo += String.format("Latitude: %.2f\n\n",lat);
        locationInfo += String.format("Longitude: %.2f\n\n",lng);
        locationInfo += String.format("Accuracy: %.2fm\n\n",accuracy);
        locationInfo += String.format("Speed: %.2fm/s\n\n",speed);
        locationInfo += String.format("Bearing: %.2f\n\n",bearing);
        locationInfo += String.format("Altitude: %.2fm\n\n",altitude);


        try {
            addressList = geocoder.getFromLocation(lat, lng, 1);
            if(addressList.size() > 0){
                Address address = addressList.get(0);
                locationInfo += "Address:\n";
                for(int i = 0; address.getAddressLine(i) != null ; i++) {
                    locationInfo += String.format("%s\n", address.getAddressLine(i));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        infoText.setText(locationInfo);
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
