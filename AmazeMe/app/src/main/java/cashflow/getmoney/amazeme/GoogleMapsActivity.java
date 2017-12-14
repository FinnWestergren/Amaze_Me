package cashflow.getmoney.amazeme;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Location;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import processing.android.PFragment;
import processing.android.CompatUtils;
import processing.core.PApplet;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback,
GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
GoogleMap.OnMarkerClickListener, LocationListener,SensorEventListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private boolean mLocationUpdateState;
    private BroadcastReceiver broadcastReceiver;

    private PApplet sketch;

    private float azimuth = 0;

    private static final int FINE_LOCATION_REQUEST_CODE = 1;
    private static final int REQUEST_CHECK_SETTINGS = 2;
    final int PLAY_SERVICES_RESOLUTION_CODE = 1000;

    protected void startLocationUpdates() {
        // If the ACCESS_FINE_LOCATION permission has not been granted, request it now and return
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_REQUEST_CODE);
            return;
        }
        // If there is permission, request for location updates
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    // Create instance of LocationRequest, add it to instance of LocationSettingsRequest.Builder and retrieve
    // and handle changes to be made based on current state of user's location settings.
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        // Specifies the rate at which app will like to receive updates
        mLocationRequest.setInterval(20);
        // Specifies the fastest rate at which the app can handle updates
        mLocationRequest.setFastestInterval(10);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch(status.getStatusCode()) {
                    // A SUCCESS means location request can be initialized
                    case LocationSettingsStatusCodes.SUCCESS:
                        mLocationUpdateState = true;
                        startLocationUpdates();
                        break;
                    // A RESOLUTION_REQUIRED means location settings have some issues which can be fixed.
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(GoogleMapsActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    // A SETTINGS_CHANGE_UNAVAILABLE means the location settings have some issues that you can't fix
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    private void setUpMap() {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_REQUEST_CODE);
            return;
        }

        // enables the my location layer which draws a light blue dot on the user's location
        // also adds button to center map on user's location
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // determines the availability of location data on the device
        LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);
        if(null != locationAvailability && locationAvailability.isLocationAvailable()) {
            // gives the most recent location currently available
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            // If most recent location was successfully retrieved, move camera to user's current location
            if(mLastLocation != null) {
                LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                // add pin at user's location
//                placeMarkerOnMap(currentLocation);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 20));

            }
        }
    }

//    protected void placeMarkerOnMap(LatLng location) {
//        // Create MarkerOptions object and set the user's current location as the position for the marker
//        MarkerOptions markerOptions = new MarkerOptions().position(location);
//        // Add marker to map
//        mMap.addMarker(markerOptions);
//    }

    public boolean checkGooglePlayServices(Context context) {

        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int resultCode = api.isGooglePlayServicesAvailable(context);

        if(resultCode != ConnectionResult.SUCCESS) {

            if(api.isUserResolvableError(resultCode)) {
                api.getErrorDialog(((Activity) context), resultCode, PLAY_SERVICES_RESOLUTION_CODE).show();
            } else {
                Toast.makeText(context, "This device is not supported", Toast.LENGTH_LONG);
                ((Activity) context).finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SensorManager sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        Sensor accel=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magneto=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magneto, SensorManager.SENSOR_DELAY_NORMAL);

        setContentView(R.layout.activity_google_maps);

        FrameLayout frame = new FrameLayout(this);
        frame.setId(CompatUtils.getUniqueViewId());
        setContentView(frame, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        sketch = new Sketch();
        PFragment fragment = new PFragment(sketch);
        fragment.setView(frame, this);

        // Listens for a logout broadcast
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.package.ACTION_LOGOUT");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive", "Logout in progress");

                finish();
            }
        };
        registerReceiver(broadcastReceiver, filter);

        checkGooglePlayServices(this);

        if(mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        createLocationRequest();

    }

    @Override
    protected void onStart() {
        super .onStart();

        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super .onStop();
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
        unregisterReceiver(broadcastReceiver);
    }

    // Start the update request if it has a RESULT_OK result for a REQUEST_CHECK_SETTINGS request
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super .onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CHECK_SETTINGS) {
            if(resultCode == RESULT_OK) {
                mLocationUpdateState = true;
                startLocationUpdates();
            }
        }

        if(requestCode == PLAY_SERVICES_RESOLUTION_CODE) {
            int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
            if(status == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) {
                String GPS_LINK = "play.google.com/store/apps/details?id=com.google.android.gms&hl=en";
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://" + GPS_LINK)));
                } catch (android.content.ActivityNotFoundException exception) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + GPS_LINK)));
                }
            }

        }
    }

    // Stop location update request
    @Override
    protected void onPause() {
        super .onPause();
        if(mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    // Restart location update request
    @Override
    public void onResume() {
        super .onResume();

        if(mGoogleApiClient.isConnected() && !mLocationUpdateState) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        setUpMap();

        if(mLocationUpdateState) {
            startLocationUpdates();

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

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
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Initialize Google Play Services
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response!
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setCancelable(false);
                dialog.setTitle("Permission access")
                        .setMessage("This app uses your location in order to generate the maze. Please allow access.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(GoogleMapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_REQUEST_CODE);
                            }
                        });
                dialog.show();

            } else {
                // No explanation needed, we can request the permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_REQUEST_CODE);
            }
            return;
        } else {
            mMap.setMyLocationEnabled(true);

            // determines the availability of location data on the device
            LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);
            if(null != locationAvailability && locationAvailability.isLocationAvailable()) {
                // gives the most recent location currently available
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                // If most recent location was successfully retrieved, move camera to user's current location
                if(mLastLocation != null) {
                    LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    // add pin at user's location
//                    placeMarkerOnMap(currentLocation);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 20));
                }
            }
        }
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case FINE_LOCATION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted! Do the location related
                    // task that needs to be done
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mMap.setMyLocationEnabled(true);

                    // determines the availability of location data on the device
                    LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);
                    if(null != locationAvailability && locationAvailability.isLocationAvailable()) {
                        // gives the most recent location currently available
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                        // If most recent location was successfully retrieved, move camera to user's current location
                        if(mLastLocation != null) {
                            LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                            // add pin at user's location
//                            placeMarkerOnMap(currentLocation);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 20));
                        }
                    }

                } else {
                    // Permission denied! Disable the functionality
                    // that depends on this permission

                }
                return;
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public ArrayList<Float> latHistory = new ArrayList<Float>();
    public ArrayList<Float> longHistory = new ArrayList<Float>();

    int count = 0;
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        if(null != mLastLocation) {
//            mMap.clear();
//            placeMarkerOnMap(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
            LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 20));

            float[] values;

            if(!((Sketch)sketch).initialized && count >= 8)((Sketch)sketch).init(5,getAverage(latHistory),
                    getAverage(longHistory),getAverage(azimut),
                    (0.00014),4);

            latHistory.add((float)mLastLocation.getLatitude());
            longHistory.add((float)mLastLocation.getLongitude());

            ((Sketch)sketch).updateLocation(getAverage(latHistory),getAverage(longHistory));
            if(latHistory.size()>4) latHistory.remove(0);
            if(longHistory.size()>4) longHistory.remove(0);



            count++;
            Toast.makeText(this,"location: " + location.toString(),Toast.LENGTH_LONG);
        }

    }


    @Override
    public void onNewIntent(Intent intent) {
        if (sketch != null) {
            sketch.onNewIntent(intent);
        }
    }

    ArrayList<Float> azimut = new ArrayList<Float>();
    float[] mGravity;
    float[] mGeomagnetic;
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimut.add(orientation[0]); // orientation contains: azimut, pitch and roll
                if(azimut.size()>20) azimut.remove(0);
                ((Sketch)sketch).updateRotation(getAverage(azimut));
            }
        }
    }

    private float getAverage(ArrayList<Float> list) {
        float sum = 0;
        for(float f: list) {
            sum+=f;
        }
        return (sum/list.size());
    }




    @Override
    public boolean onMarkerClick(Marker marker){
        return true;
    }

    @Override
    public void onAccuracyChanged(Sensor s, int i){

    }

}
