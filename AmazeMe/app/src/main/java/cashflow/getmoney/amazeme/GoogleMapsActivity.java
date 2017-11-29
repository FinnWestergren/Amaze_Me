package cashflow.getmoney.amazeme;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
<<<<<<< HEAD
import android.support.v7.app.AlertDialog;
import android.widget.Toast;
=======
>>>>>>> origin/master

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
<<<<<<< HEAD
    LatLng mCoordinates;
    LocationManager locationManager;
    MarkerOptions mo;
    Marker marker;

    final int FINE_LOCATION_REQUEST_CODE = 0;
=======
    int FINE_LOCATION_REQUEST_CODE = 0;
>>>>>>> origin/master



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mo = new MarkerOptions().position(new LatLng(0, 0)).title("Current Location");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Initialize Google Play Services
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
<<<<<<< HEAD
=======


            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

>>>>>>> origin/master
            // Should we show an explanation?
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response!

            } else {
                // No explanation needed, we can request the permission
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_REQUEST_CODE);
            }

            return;
        } else {
            mMap.setMyLocationEnabled(true);
            // Set a preference for minimum and maximum zoom
            mMap.setMinZoomPreference(15.0f);
            mMap.setMaxZoomPreference(20.0f);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void requestLocation() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        String provider = locationManager.getBestProvider(criteria, true);
        locationManager.requestLocationUpdates(provider, 10000, 10, this);
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case FINE_LOCATION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted! Do the location related
                    // task that needs to be done
<<<<<<< HEAD
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                        // Set a preference for minimum and maximum zoom
                        mMap.setMinZoomPreference(15.0f);
                        mMap.setMaxZoomPreference(20.0f);

                    }
=======
                    mMap.setMyLocationEnabled(true);
>>>>>>> origin/master
                } else {
                    // Permission denied! Disable the functionality
                    // that depends on this permission
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();

                }
                return;
            }
        }
    }
}
