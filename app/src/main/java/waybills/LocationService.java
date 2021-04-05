package waybills;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationService extends Service implements LocationListener, OnMapReadyCallback {
    LocationManager lm;
    private GoogleMap mMap;
    private double latitude;
    private double longitude;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);
        //System.out.println("ЫЧ");
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            System.out.println(String.format(
                    "Coordinates: lat = %1$.4f, lon = %2$.4f",
                    location.getLatitude(), location.getLongitude()));
            /*latitude = location.getLatitude();
            longitude = location.getLongitude();
            if (latitude != 0 && longitude != 0) {
                LatLng marker = new LatLng(latitude, longitude);
                mMap.moveCamera((CameraUpdateFactory.newLatLng(marker)));
            }*/
        }
    };

    public void OnDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
       /* System.out.println(String.format(
                "Coordinates: lat = %1$.4f, lon = %2$.4f",
                location.getLatitude(), location.getLongitude()));
        //tvLocation.setText(formatLocation(location));
        /*latitude = location.getLatitude();
        longitude = location.getLongitude();
        if (location.getLatitude() != 0 && location.getLongitude() != 0) {
            LatLng marker = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera((CameraUpdateFactory.newLatLng(marker)));
        }*/
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //System.out.println(123);
                //stopSelf(startId);
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng marker = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions()
                .position(marker));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
    }
}
