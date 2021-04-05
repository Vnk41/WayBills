package waybills;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ru.pstu.vmmb.waybills.R;

public class CurrentLocation extends AppCompatActivity implements OnMapReadyCallback {

    TextView tvLocation;
    static LocationManager LocationManager;
    private GoogleMap mMap;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);

        SupportMapFragment mapFrag =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        tvLocation = findViewById(R.id.tvLocation);

        LocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
    }

    @Override
    protected void onPause(){
        super.onPause();
        LocationManager.removeUpdates(locationListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng marker = new LatLng(latitude, longitude);
        if (latitude != 0 && longitude != 0) {
            mMap.addMarker(new MarkerOptions()
                    .position(marker));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
        }
    }


    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
            System.out.println(String.format(
                    "Coordinates: lat = %1$.4f, lon = %2$.4f",
                    location.getLatitude(), location.getLongitude()));
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            if (latitude != 0 && longitude != 0) {
                LatLng marker = new LatLng(latitude, longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker, 15.0f));}
        }

        @Override
        public void onProviderDisabled(String provider) {
            checkEnabled();
        }

        @Override
        public void onProviderEnabled(String provider) {
            checkEnabled();
            showLocation(LocationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == -1) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        //LocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
        //        1000 * 10, 10, locationListener);
        checkEnabled();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("Yeah, that's cool!");
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    //LocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    //        60000, 0, locationListener);
                } else {
                    System.out.println("Shit!");
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void showLocation(Location location) {
        System.out.println("location = " + location);
        if (location == null)
            return;
        else {
            tvLocation.setText(formatLocation(location));
        }
    }

    private void checkEnabled() {

    }

    private String formatLocation(Location location) {
        //System.out.println(location.g);
        if (location == null)
            return "";
        return String.format(
                "Ширина = %1$.4f \nДолгота = %2$.4f",
                location.getLatitude(), location.getLongitude());
    }

}