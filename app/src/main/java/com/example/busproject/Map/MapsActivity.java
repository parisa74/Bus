package com.example.busproject.Map;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.busproject.Adapters.ShowLineAdapter;
import com.example.busproject.CodeScannerActivity;
import com.example.busproject.LoginActivity;
import com.example.busproject.Model.Area;
import com.example.busproject.Model.Line;
import com.example.busproject.Model.Station;
import com.example.busproject.R;
import com.example.busproject.Scanner3;
import com.example.busproject.Utils.AppController;
import com.example.busproject.Utils.CustomTypefaceSpan;
import com.example.busproject.Utils.Helpers;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

import omidi.mehrdad.moalertdialog.MoAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private BottomSheetBehavior bottomSheetBehavior;

    int minIndex;
    int count;
    Animation animation;
    ImageView person1, person2, person3, person4;
    ImageButton pluse, minuse;
    Button capacity_btn;
    RelativeLayout choose_taxi_layout;
    ImageView cancel_btn;
    Button btn_choose_taxi;
    Marker marker;
    Activity activity = this;
    private GoogleMap mMap;
    Circle myCircle;
    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static String TAG = "MAP LOCATION";
    Context mContext;
    TextView mLocationMarkerText;
    private LatLng mCenterLatLong;
    ImageButton currentlocation;
    Toolbar mToolbar;
    PolylineOptions options;
    List<Line> lines;
    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View bottom_sheet;
    List<Station> stations;

    public static Line mLine;
    Location mLocation;
    Handler handler = new Handler();

    /**
     * Receiver registered with this activity to get the response from FetchAddressIntentService.
     */
    private AddressResultReceiver mResultReceiver;
    /**
     * The formatted location address.
     */
    //protected String mAddressOutput;
    protected String mAreaOutput;
    protected String mCityOutput;
    protected String mStateOutput;
    EditText mLocationAddress;
    TextView mLocationText, txtCarDistance;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    ImageView imgmenu, imageMarker, imgsearch;
    DrawerLayout drawer_layout;
    private List<Area> areaArrayList;
    private ArrayList<Float> distances = new ArrayList<>();

    int alarmDist = 100;
    TextView txtDistance;
    CardView search_layout;
    LinearLayout  seekbar_layout;
    public static int location_int = 1;
    private int location_time = 0;

    boolean flag = false;
    Location mLastKnownLocation;
    Button confrimDest;

    boolean backpressflag = false;
    private GoogleMap.OnMarkerClickListener clusterManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);


        person1 = findViewById(R.id.p1);
        person2 = findViewById(R.id.p2);
        person3 = findViewById(R.id.p3);
        person4 = findViewById(R.id.p4);

        pluse = findViewById(R.id.pluse_btn);
        minuse = findViewById(R.id.minuse_btn);
        capacity_btn = findViewById(R.id.capacity);

        cancel_btn = findViewById(R.id.cancel_btn);
        choose_taxi_layout = findViewById(R.id.choose_taxi_layout);
        btn_choose_taxi = findViewById(R.id.choose_taxi_btn);

        SharedPreferences sharedPreferences = AppController.getAppContext().getSharedPreferences("verified", 0);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("flag", "true");
        editor.commit();


        turnOnGPS();
        confrimDest = findViewById(R.id.confrimDest);

        confrimDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Location desLoc = new Location("");
//                desLoc.setLatitude(mCenterLatLong.latitude);
//                desLoc.setLongitude(mCenterLatLong.longitude);
//                mMap.clear();
//
//                Location myLoc = new Location("");
                try {
                    //   startAnim();

                    // myLoc.setLatitude(getMyLocation().getLatitude());

                    // myLoc.setLongitude(getMyLocation().getLongitude());

                    // myLines(desLoc, myLoc);

                    //getNearestTaxis(myLoc);


                    marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(mCenterLatLong.latitude, mCenterLatLong.longitude))
                            .icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.mylocation)));

                    marker.setTag("mainmarker");


                    imageMarker.setVisibility(View.INVISIBLE);
                    confrimDest.setVisibility(View.INVISIBLE);
                    backpressflag = true;
                    //getNearestTaxis();
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } catch (Exception e) {

                    turnOnGPS();
                    Toast.makeText(MapsActivity.this, "برای مشاهده خطوط نزدیک سیستم موقعیت یاب خود را روشن نمایید.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageMarker = findViewById(R.id.imageMarker);


        imgsearch = findViewById(R.id.search);
        search_layout = findViewById(R.id.crd_search);


        seekbar_layout = findViewById(R.id.crd_seekbar);


        final IndicatorSeekBar seekBar = findViewById(R.id.seek_bar);


        seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {

            @Override
            public void onSeeking(SeekParams seekParams) {
                Log.d(TAG, "onSeeking: " + seekParams.progress);
                drawCircle(seekParams.progress);
                alarmDist=seekParams.progress;
//                SharedPreferences.Editor SP;
//                SP = PreferenceManager.getDefaultSharedPreferences(AppController.getAppContext()).edit();
//
//                SP.putString("alarm_len", String.valueOf(seekParams.progress * 1000));
//                SP.apply();
//todo should get from server
                // getNearestTaxis(seekParams.progress*1000);

                if (seekParams.progress == 2) {


                } else if (seekParams.progress == 4) {

                } else if (seekParams.progress == 6){

                }
                else if (seekParams.progress==8){

                }
                else if (seekParams.progress==10){

                }


//                    List<Taxi> taxis = new ArrayList<>();
//                MarkerOptions markerOptions = new MarkerOptions();
//
//                markerOptions.position(new LatLng(35.744964, 50.905066)).icon(BitmapDescriptorFactory.fromResource(R.drawable.car));
//                Marker marker = mMap.addMarker(markerOptions);
//                // marker.setTag(taxi.getId());
//                Taxi taxi = new Taxi();
//                taxi.setLat(35.744964);
//                taxi.setLng(50.905066);
//                taxis.add(taxi);
//
//                MarkerWindowInfoAdapter markerInfoWindowAdapter = new MarkerWindowInfoAdapter(AppController.getAppContext(), taxis);
//                mMap.setInfoWindowAdapter(markerInfoWindowAdapter);
////

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
                LatLng homeLoc = new LatLng(mCenterLatLong.latitude, mCenterLatLong.longitude);
                // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(homeLoc, 12));


            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
            }
        });




        //   seekBar.getIndicator().setContentView(R.layout.indic);

        currentlocation = findViewById(R.id.currentlocation);
//daryaft location feli karbar
        currentlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnOnGPS();
                getLastLocation();
try {
    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
}catch (Exception e){}
                //toye oncreate ham benevisam
//                if (getMyLocation()!=null)
//                initComponent();

            }
        });


        mContext = this;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        mLocationText = (TextView) findViewById(R.id.Locality);
        // mToolbar = (Toolbar) findViewById(R.id.m_toolbar);
        // setSupportActionBar(mToolbar);


        drawer_layout = findViewById(R.id.drawer_layout);
        imgmenu = findViewById(R.id.imgMenu);
        NavigationView navView = findViewById(R.id.nav_view);
        View hView = navView.getHeaderView(0);
        TextView txt_name = hView.findViewById(R.id.nav_header_textView);
        try {
            txt_name.setText(Helpers.getName());
        } catch (Exception e) {
        }
        navView.setItemIconTintList(ColorStateList.valueOf(ContextCompat.getColor(MapsActivity.this, android.R.color.black)));
        navView.setItemTextColor(ColorStateList.valueOf(ContextCompat.getColor(MapsActivity.this, android.R.color.black)));
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawer_layout.closeDrawers();
                switch (item.getItemId()) {

//todo mavard zir version badi
//                    case R.id.travel_list:
//                        Intent intentsetting = new Intent(MainActivity.this, TravelHistoryActivity.class);
//                        startActivity(intentsetting);
//                        break;
//
//
                    case R.id.profile:

                        // startActivity(new Intent(MapsActivity.this, ProfileActivity.class));
                        break;


                    case R.id.qr_reader:


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                startActivity(new Intent(MapsActivity.this, Scanner3.class));

                            } else {
                                String[] permissions = new String[]{Manifest.permission.CAMERA};
                                requestPermissions(permissions, 100);

                            }

                        } else
                            startActivity(new Intent(MapsActivity.this, Scanner3.class));
                        break;

                    case R.id.exit:
                        Alert_Exit();
                        break;

                }
                return true;
            }
        });
        Menu m = navView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

        imgmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //    startActivity(new Intent(MapsActivity.this, PaymentActivity.class));
                if (drawer_layout.isDrawerOpen(Gravity.RIGHT)) {
                    drawer_layout.closeDrawer(Gravity.RIGHT);
                } else {
                    drawer_layout.openDrawer(Gravity.END);
                }
            }


        });


        //  getSupportActionBar().setDisplayShowHomeEnabled(true);

        // getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        //ActionBar actionbar = getSupportActionBar();
        //actionbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.side_nav_bar));


        mLocationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

           //     openAutocompleteActivity();

            }


        });

        mapFragment.getMapAsync(this);
        mResultReceiver = new AddressResultReceiver(new Handler());
        if (checkPlayServices()) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
            if (!AppUtils.isLocationEnabled(mContext)) {
                // notify user
//                turnGpsOnDialog();
            }
            buildGoogleApiClient();
        } else {
            Toast.makeText(mContext, "Location not supported in this device", Toast.LENGTH_SHORT).show();
        }

//
//        showTip();
//        getLastLocation();
//
//        if (getMyLocation()!=null)
//            try {
//
//                initComponent();
//
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            } catch (Exception e) {
//            }
//        else {
//            if(Helpers.getSharePrf("StationTip").equals("1"))
//            chooseLoc();
//        }

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

    //-------------------------------------------------------------------onMapReady-------------------------------------------------------------------------------------------
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "OnMapReady");
        mMap = googleMap;


        LatLng latLong = new LatLng(35.684209, 51.388263);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLong).zoom(16).build();

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));


        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {


                Log.d("Camera postion change" + "", cameraPosition + "");
                mCenterLatLong = cameraPosition.target;
                if (!flag) {
//---------------------------------------------------area hay mokhtalef ra migirad va ba tavajo be makan kononni karbar area karbar ra bar migardanad------------------------------
                    if (Helpers.isNetworkAvailable(AppController.getAppContext()))
                        getAreas();
                    else Helpers.noInternetDialog();

//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


                    //getStations(5);
                }

//                mMap.clear();


                try {

                    mLocation = new Location("");
                    mLocation.setLatitude(mCenterLatLong.latitude);
                    mLocation.setLongitude(mCenterLatLong.longitude);

                    startIntentService(mLocation);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Location location = new Location("");
                location.setLatitude(mCenterLatLong.latitude);
                location.setLongitude(mCenterLatLong.longitude);
                // Toast.makeText(MapsActivity.this, String.valueOf(mCenterLatLong.latitude)+","+String.valueOf( mCenterLatLong.longitude), Toast.LENGTH_SHORT).show();

                Log.d("latlng", String.valueOf(mCenterLatLong.latitude) + "," + String.valueOf(mCenterLatLong.longitude));


                initComponent(location);
                if (location_time < location_int)
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                else bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                location_time = location_time + 1;


            }
        });

        imageMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } catch (Exception e) {
                }

//
//                Location desLoc = new Location("");
//                desLoc.setLatitude(mCenterLatLong.latitude);
//                desLoc.setLongitude(mCenterLatLong.longitude);
//                mMap.clear();


                try {
//                    startAnim();
//                    Location myLoc = new Location("");
//
//
//                    myLoc.setLatitude(getMyLocation().getLatitude());
//                    myLoc.setLongitude(getMyLocation().getLongitude());
//
//                    myLines(desLoc, myLoc);
//                    getNearestTaxis(myLoc);

                    marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(mCenterLatLong.latitude, mCenterLatLong.longitude))
                            .icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_location2)))
                    ;

                    imageMarker.setVisibility(View.INVISIBLE);
                    confrimDest.setVisibility(View.INVISIBLE);
                    backpressflag = true;
                    //     getNearestTaxis(getMyLocation());
                } catch (Exception e) {
                    Toast.makeText(MapsActivity.this, "برای مشاهده خطوط نزدیک سیستم موقعیت یاب خود را روشن نمایید.", Toast.LENGTH_SHORT).show();

                    turnOnGPS();
                }
            }
        });


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
//        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        //     initComponent();

    }

    @Override
    public void onConnected(Bundle bundle) {
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
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            changeMap(mLastLocation);
            Log.d(TAG, "ON connected");

        } else
            try {
                LocationServices.FusedLocationApi.removeLocationUpdates(
                        mGoogleApiClient, this);

            } catch (Exception e) {
                e.printStackTrace();
            }
        try {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            if (location != null)
                changeMap(location);
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            mGoogleApiClient.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //finish();
            }
            return false;
        }
        return true;
    }

    private void changeMap(Location location) {

        Log.d(TAG, "Reaching map" + mMap);


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

        // check if map is created successfully or not
        if (mMap != null) {
            mMap.getUiSettings().setZoomControlsEnabled(false);
            LatLng latLong;


            latLong = new LatLng(location.getLatitude(), location.getLongitude());

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLong).zoom(19).build();

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            // mLocationMarkerText.setText("Lat : " + location.getLatitude() + "," + "Long : " + location.getLongitude());
            startIntentService(location);


        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        }

    }


    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            //  mAddressOutput = resultData.getString(AppUtils.LocationConstants.RESULT_DATA_KEY);

            mAreaOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_AREA);

            mCityOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_CITY);
            mStateOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_STREET);
//show address
            //displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == AppUtils.LocationConstants.SUCCESS_RESULT) {
                //  showToast(getString(R.string.address_found));


            }


        }

    }

    /**
     * Updates the address in the UI.
     */


    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */
    protected void startIntentService(Location mLocation) {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(AppUtils.LocationConstants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(AppUtils.LocationConstants.LOCATION_DATA_EXTRA, mLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent);
    }

//
//    private void openAutocompleteActivity() {
//        try {
//            // The autocomplete activity requires Google Play Services to be available. The intent
//            // builder checks this and throws an exception if it is not the case.
//            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
//                    .build(this);
//            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
//        } catch (GooglePlayServicesRepairableException e) {
//            // Indicates that Google Play Services is either not installed or not up to date. Prompt
//            // the user to correct the issue.
//            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
//                    0 /* requestCode */).show();
//        } catch (GooglePlayServicesNotAvailableException e) {
//            // Indicates that Google Play Services is not available and the problem is not easily
//            // resolvable.
//            String message = "Google Play Services is not available: " +
//                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);
//
//            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
//        }
//    }

    /**
     * Called after the autocomplete activity has finished to return its result.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                //Place place = PlaceAutocomplete.getPlace(mContext, data);

                // TODO call location based filter


                LatLng latLong;


               // latLong = place.getLatLng();

                //mLocationText.setText(place.getName() + "");

//                CameraPosition cameraPosition = new CameraPosition.Builder()
//                        .target(latLong).zoom(19).build();

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
                mMap.setMyLocationEnabled(true);
//                mMap.animateCamera(CameraUpdateFactory
//                        .newCameraPosition(cameraPosition));


            }


//        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
//            Status status = PlaceAutocomplete.getStatus(mContext, data);
//        } else if (resultCode == RESULT_CANCELED) {
//            // Indicates that the activity closed before a selection was made. For example if
//            // the user pressed the back button.
        }
    }

    //-------------------------------------in function location kononi karbar ra hesab mikonad--------------------------------------------------------------------------------------
    private void getLastLocation() {

        if (ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's Response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 124;
                final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 123;

                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);
                // MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        try {
            Location mLastKnownLocation = LocationServices.
                    FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastKnownLocation != null) {
                LatLng latLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
//                StaticVars.getCurrentRequest().setLat(mLastKnownLocation.getLatitude());
//                StaticVars.getCurrentRequest().setLng(mLastKnownLocation.getLongitude());

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 19);
                mMap.animateCamera(cameraUpdate);
                onLocationChanged(mLastKnownLocation);


            }
        } catch (SecurityException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------area hay mokhtalef ra az server migirad----------------------------------------------------------------------------------------


    public Location getMyLocation() {

        if (ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's Response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 124;
                final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 123;

                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);
                // MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        try {
            mLastKnownLocation = LocationServices.
                    FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastKnownLocation != null) {
                LatLng latLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
//                StaticVars.getCurrentRequest().setLat(mLastKnownLocation.getLatitude());
//                StaticVars.getCurrentRequest().setLng(mLastKnownLocation.getLongitude());


            }
        } catch (SecurityException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return mLastKnownLocation;
    }


    public void getAreas() {


        RequestParams params = new RequestParams();
        //    params.put("db", "states");
        //admin.idpz.ir/api/getstates

        Helpers.client.get("http://admin.idpz.ir/api/getstates", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {


                try {

                    Area[] areaModel = Helpers.gson.fromJson(responseString, Area[].class);

                    areaArrayList = Arrays.asList(areaModel);

                    findArea();
//                    if (areaModel.length > 0)
//                        for (int i = 0; i < areaModel.length; i++) {
//                            //findArea(areaModel[i]);
//                            Log.d(TAG, "onSuccess: " + areaModel[i].getName());
//
//                        }

                } catch (Exception e) {

                }


            }
        });

    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //-----------------------------------------area karbar ra bamigardanad--------------------------------------------------------------------------------------------------------------------
    public void findArea() {


        distances.clear();

        for (Area area : areaArrayList) {
            float myDistance = 0;
            Location mycity = new Location("");
            Location myLocation = new Location("");
            myLocation.setLatitude(mCenterLatLong.latitude);
            myLocation.setLongitude(mCenterLatLong.longitude);

            mycity.setLatitude(area.getAttributes().getLat());
            mycity.setLongitude(area.getAttributes().getLng());
            myDistance = Math.round(myLocation.distanceTo(mycity));
            distances.add(myDistance);

        }

        minIndex = distances.indexOf(Collections.min(distances));
        mMap.clear();

        if (Helpers.isNetworkAvailable(AppController.getAppContext()))
            getStations(minIndex);
        else Helpers.noInternetDialog();

        Log.d("pariarea", "findArea: " + areaArrayList.get(minIndex).getId().toString());

    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //------------------------------khotot taxi ra bar migardanim---------------------------------------------------------------------------------------------------------------------------------
    public void getStations(int id) {
        /*
         ***************************************RECIEVING SERVICES*****************************************************************
         */

        RequestParams params = new RequestParams();
        params.put("id", id);
        Helpers.client.post("http://admin.idpz.ir/api/getlines", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                flag = true;

                try {


                    Line[] areaModel = Helpers.gson.fromJson(responseString, Line[].class);

//todo function namyesh khotot bayad inja gharar begire

                    lines = Arrays.asList(areaModel);
                    for (Line line : lines) {
                        drawLine(line);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onSuccess: ", e);
                }

            }
        });


    }

//////////////////////////////////////////////////////namayesh khotot atraf karbar////////////////////////////////////////////////////////////////////////////////

    public void myLines(final Location destLocation, final Location myLocation) {
//http://admin.idpz.ir/api/getlines
        RequestParams params = new RequestParams();
        params.put("id", areaArrayList.get(minIndex).getId());
        Helpers.client.post("http://admin.idpz.ir/api/getlines", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                try {

                    Line[] areaModel = Helpers.gson.fromJson(responseString, Line[].class);


                    lines = Arrays.asList(areaModel);
                    List<Line> mlines = new ArrayList<>();


                    for (Line line : lines) {


                        stations = line.getStations();
                        for (Station station : stations) {

                            Location stationLoc = new Location("");
                            stationLoc.setLatitude(station.getLat());
                            stationLoc.setLongitude(station.getLng());

//todo 1000 should change to alarmDist
                            if (stationLoc.distanceTo(destLocation) < 1000 && !mlines.contains(line)) {
                                mlines.add(line);
                                // drawLine(line);

                            }


                        }
                    }

                    for (Line line : mlines) {
                        stations = new ArrayList<>();
                        stations = line.getStations();
                        for (Station station : stations) {

                            Location stationLoc = new Location("");
                            stationLoc.setLatitude(station.getLat());
                            stationLoc.setLongitude(station.getLng());

//todo 1000 should change to alarmDist
                            if (stationLoc.distanceTo(myLocation) < 100000) {

                                drawLine(line);


                            }


                        }
                    }


                } catch (Exception e) {
                    Log.e(TAG, "onSuccess: ", e);
                }

            }
        });


    }


    public void drawLine(Line line) {

        options = new PolylineOptions().width(5).color(Color.parseColor(line.getColor())).geodesic(true);

        stations = line.getStations();
        for (Station station : stations) {

            LatLng point = new LatLng(station.getLat(), station.getLng());

            options.add(point);


        }
        mMap.addPolyline(options);
    }


//    public void newRequest() {
//
//        final Dialog dialog = new Dialog(MapsActivity.this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
//        dialog.setContentView(R.layout.send_request_dialog);
//        dialog.setCancelable(false);
//        dialog.show();
//        Button btnOk = dialog.findViewById(R.id.btnOk);
//        btnOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                turnOnGPS();
//                dialog.dismiss();
//            }
//        });
//
//
//    }


    private void turnOnGPS() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(activity)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            mGoogleApiClient.connect();
        }


        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    activity, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }


    public void drawCircle(int progress) {

        // mMap.clear();
       try {
           CircleOptions circleOptions = new CircleOptions().center(new LatLng(getMyLocation().getLatitude(), getMyLocation().getLongitude()))
                   .radius(progress)
                   .strokeColor(R.color.material_blue)
                   .fillColor(R.color.material_blue)
                   .strokeWidth(1);

           if (myCircle != null) {
               myCircle.remove();
           }
           myCircle = mMap.addCircle(circleOptions);

       }catch (Exception e){}
    }


    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/IRANSans(FaNum).ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }


    private BitmapDescriptor bitmapDescriptorFromVector2(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        background.setVisible(true, true);

        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
//        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
//
//    public void choose_taxi(Taxi taxi, String customer_number) {
//        RequestParams params = new RequestParams();
//        try {
//            params.put("user_id", Helpers.getUser_id());
//        } catch (Exception e) {
//        }
//        params.put("customer_number", customer_number);
//
//        params.put("lat", getMyLocation().getLatitude());
//        params.put("lng", getMyLocation().getLongitude());
//        params.put("taxi_id", taxi.getId());
//        params.put("token", taxi.getToken());
//        Helpers.client.post(Helpers.baseUrl + "select-taxi", params, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, String responseString) {
//
//            }
//        });
//    }

    @Override
    public void onBackPressed() {

        if (backpressflag) {
            imageMarker.setVisibility(View.VISIBLE);
            // confrimDest.setVisibility(View.VISIBLE);
            marker.remove();
            backpressflag = false;
        } else {
            super.onBackPressed();

        }
    }


    public void Alert_Exit() {

//        Typeface face = Typeface.createFromAsset(AppController.getAppContext().getAssets(),
//                "fonts/IRANSans(FaNum).ttf");
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(MapsActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(MapsActivity.this);
        }
        builder.setTitle("خروج از حساب")
                .setMessage("آیا از خروج خود اطمینان دارید؟")
                .setPositiveButton("بله", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete


                        File Item2 = new File("/data/data/" + getPackageName() + "/shared_prefs/verified.xml");

                        Item2.delete();


                        Intent i1 = new Intent(getApplicationContext(), LoginActivity.class);
                        i1.setAction(Intent.ACTION_MAIN);
                        i1.addCategory(Intent.CATEGORY_HOME);
                        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        overridePendingTransition(0, 0);

                        startActivity(i1);
                        finish();

                        dialog.dismiss();
                    }
                })
                .setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing

                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();


    }


//---------------------------------------------------------------------------------------------------------------------------------------------------

    private void initComponent(Location location) {
        String test = "[\n" +
                "    {\n" +
                "        \"id\": 41,\n" +
                "        \"name\": \"میدان انقلاب بیمارستان قلب\",\n" +
                "        \"color\": \"ff00bf\",\n" +
                "        \"Stations\": [\n" +
                "            {\n" +
                "                \"id\": 462,\n" +
                "                \"lat\": 35.701427,\n" +
                "                \"lng\": 51.391068,\n" +
                "                \"name\": \"میدان انقلاب\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 463,\n" +
                "                \"lat\": 35.703533,\n" +
                "                \"lng\": 51.390812,\n" +
                "                \"name\": \"ایستگاه1\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 464,\n" +
                "                \"lat\": 35.708153,\n" +
                "                \"lng\": 51.390232,\n" +
                "                \"name\": \"ایستگاه2\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 465,\n" +
                "                \"lat\": 35.714195,\n" +
                "                \"lng\": 51.389435,\n" +
                "                \"name\": \"ایستگاه3\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 466,\n" +
                "                \"lat\": 35.717499,\n" +
                "                \"lng\": 51.389442,\n" +
                "                \"name\": \"ایستگاه4\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 467,\n" +
                "                \"lat\": 35.719101,\n" +
                "                \"lng\": 51.38916,\n" +
                "                \"name\": \"بیمارستان قلب\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": 42,\n" +
                "        \"name\": \"میدان انقلاب میدان فاطمی\",\n" +
                "        \"color\": \"ff8000\",\n" +
                "        \"Stations\": [\n" +
                "            {\n" +
                "                \"id\": 468,\n" +
                "                \"lat\": 35.700832,\n" +
                "                \"lng\": 51.39209,\n" +
                "                \"name\": \"میدان انقلاب\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 469,\n" +
                "                \"lat\": 35.701172,\n" +
                "                \"lng\": 51.400448,\n" +
                "                \"name\": \"ایستگاه1\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 470,\n" +
                "                \"lat\": 35.701443,\n" +
                "                \"lng\": 51.400349,\n" +
                "                \"name\": \"ایستگاه2\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 471,\n" +
                "                \"lat\": 35.708954,\n" +
                "                \"lng\": 51.397076,\n" +
                "                \"name\": \"پارک لاله\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 472,\n" +
                "                \"lat\": 35.715904,\n" +
                "                \"lng\": 51.394169,\n" +
                "                \"name\": \"ایستگاه4\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 473,\n" +
                "                \"lat\": 35.719639,\n" +
                "                \"lng\": 51.404808,\n" +
                "                \"name\": \"میدان فاطمی\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": 43,\n" +
                "        \"name\": \"چمران میدان فاطمی\",\n" +
                "        \"color\": \"0000cc\",\n" +
                "        \"Stations\": [\n" +
                "            {\n" +
                "                \"id\": 474,\n" +
                "                \"lat\": 35.712017,\n" +
                "                \"lng\": 51.380085,\n" +
                "                \"name\": \"چمران\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 475,\n" +
                "                \"lat\": 35.712574,\n" +
                "                \"lng\": 51.382359,\n" +
                "                \"name\": \"ایستگاه\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 476,\n" +
                "                \"lat\": 35.714123,\n" +
                "                \"lng\": 51.389355,\n" +
                "                \"name\": \"خیابان کارگر\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 477,\n" +
                "                \"lat\": 35.719639,\n" +
                "                \"lng\": 51.404793,\n" +
                "                \"name\": \"میدان فاطمی\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "]";


        // get the bottom sheet view
        LinearLayout llBottomSheet = findViewById(R.id.linear_bottomsheet);


        Location myLoc = new Location("");
        try {
            myLoc.setLatitude(Double.parseDouble("35.713509"));


            myLoc.setLongitude(Double.parseDouble("51.385377"));
        } catch (Exception e) {
        }
        final Line[] lines = Helpers.gson.fromJson(test, Line[].class);

        for (Line line : lines) {

            for (Station station : line.getStations()) {

                if (!station.getName().equals("")) {

                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(station.getLat(), station.getLng()))
                            .icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.busstation))).setTitle(station.getName());


                }

            }

        }


        final ArrayList<Line> arrayList = new ArrayList<Line>(Arrays.asList(lines));

        for (Line line : arrayList) {

            if (!line.getColor().contains("#")) {
                line.setColor("#" + line.getColor());
            }

        }

//        Location location=new Location("");
//        location.setLatitude(getMyLocation().getLatitude());
//        location.setLongitude(getMyLocation().getLongitude());

        ShowLineAdapter showLineAdapter = new ShowLineAdapter(arrayList, location, MapsActivity.this, new ShowLineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos, Object object) {

                Line line = (Line) object;
                try {
                    drawLine(line);

                } catch (Exception e) {
                    Log.d("parisa", e.toString());
                }

            }
        });
        RecyclerView recyclerView = findViewById(R.id.recycle);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(showLineAdapter);
        // init the bottom sheet behavior
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);

        // change the state of the bottom sheet
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


    }

    public void getStation(int id) {
        /*
         ***************************************RECIEVING SERVICES*****************************************************************
         */

        RequestParams params = new RequestParams();
        params.put("id", 4);
        Helpers.client.post("http://admin.idpz.ir/api/getlines", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                flag = true;


                try {

                    Line[] areaModel = Helpers.gson.fromJson(responseString, Line[].class);

//todo function namyesh khotot bayad inja gharar begire

                    lines = Arrays.asList(areaModel);
                    for (Line line : lines) {
                        drawLine(line);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onSuccess: ", e);
                }

            }
        });


    }


    private void showTip() {
        Typeface irsans = Typeface.createFromAsset(getAssets(), "fonts/IRANSans(FaNum).ttf");


        if (!Helpers.getSharePrf("StationTip").equals("1"))
            new MaterialTapTargetPrompt.Builder(activity)
                    .setTarget(R.id.currentlocation)
                    .setFocalColour(getResources().getColor(R.color.colorPrimaryDark))
                    .setPrimaryText("تشخیص موقعیت مکانی")
                    .setPrimaryTextTypeface(irsans)
                    .setSecondaryTextTypeface(irsans)
                    .setBackgroundColour(getResources().getColor(R.color.colorPrimary))
                    .setSecondaryText("برای اینکه بتونیم خطوط اطراف رو بهت نشون بدیم باید موقعیت مکانی خودت رو با زدن روی این دکمه مشخص کنی. ")
                    .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                        @Override
                        public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state) {
                            if (state == MaterialTapTargetPrompt.STATE_DISMISSED) {
                                Helpers.addToSharePrf("StationTip", "1");
                            }
                        }
                    })
                    .show();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    public void chooseLoc() {
        try {
            Typeface face = Typeface.createFromAsset(getAssets(),
                    "fonts/IRANSans(FaNum).ttf");

            final MoAlertDialog dialog = new MoAlertDialog(MapsActivity.this);

            dialog.showSuccessDialog("موقعیت مکانی", "برای اینکه بتونی خطوط اطراف خودت رو مشخص کنی به ما کمک کن موقعیتت رو پیدا کنیم.");

            dialog.setTypeface(face);

            dialog.setDilogIcon(R.drawable.ic_location_off_black_24dp);
            dialog.setDialogButtonText("یافتن موقعیت");
            dialog.setOnButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //initComponent();
                    dialog.dismis();
                }
            });
        } catch (Exception e) {
            Log.d("parisa", e.toString());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startActivity(new Intent(MapsActivity.this, Scanner3.class));

            } else {

                Toast.makeText(this, "برای اسکن کردن کد باید دسترسی به دوربین را به ما بدهید.", Toast.LENGTH_LONG).show();

            }

        }
    }//
}

