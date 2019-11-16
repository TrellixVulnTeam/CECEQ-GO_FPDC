package com.example.ceceqgo;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.turf.TurfJoins;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;


import static com.mapbox.mapboxsdk.style.expressions.Expression.exponential;
import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.expressions.Expression.zoom;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

public class MainActivity extends AppCompatActivity implements PermissionsListener,OnMapReadyCallback,MapboxMap.OnMapClickListener {

    //Variable for the map
    private MapView mapView;

    //Variable for the buttons
    private View levelButtons;

    //Variable to store the bounding box (area of the map to be described)
    private List<List<Point>> boundingBoxList;

    //Variable to load the json file (coordinates of the classrooms)
    private GeoJsonSource indoorBuildingSource;

    //Variables to get the user's location
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;

    //TAG variable declaration
    private static final String TAG = "MainActivity";

    //Variables for continuous tracking
    private static final long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private static final long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
    private LocationEngine locationEngine;
    private LocationChangeListeningMainActivityLocationCallback callback = new LocationChangeListeningMainActivityLocationCallback(MainActivity.this);

    //Variable for dinamic map interaction
    private static final String geoJsonLayerId = "polygonFillLayer";

    //Variables for the search bar
    private MaterialSearchBar searchBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        InitialConnection ();
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(getApplicationContext(), getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_main);


        searchBar = findViewById(R.id.searchBar);



        //Set the style of the map
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                MainActivity.this.mapboxMap = mapboxMap;

                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                        //Enable user's location
                        enableLocationComponent(style);

                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments
                        levelButtons = findViewById(R.id.floor_level_buttons);

                        //Set the bounding box (area of the map to describe)
                        final List<Point> boundingBox = new ArrayList<>();
                        boundingBox.add(Point.fromLngLat(-100.386855, 20.584795));
                        boundingBox.add(Point.fromLngLat(-100.387379, 20.586158));
                        boundingBox.add(Point.fromLngLat(-100.385917, 20.586681));
                        boundingBox.add(Point.fromLngLat(-100.385353, 20.585276));
                        boundingBoxList = new ArrayList<>();
                        boundingBoxList.add(boundingBox);


                        //Declare a CameraListener in order to work with animations with the buttons
                        mapboxMap.addOnCameraMoveListener(new MapboxMap.OnCameraMoveListener() {
                            @Override
                            public void onCameraMove() {
                                if (mapboxMap.getCameraPosition().zoom > 16) {
                                    if (TurfJoins.inside(Point.fromLngLat(mapboxMap.getCameraPosition().target.getLongitude(),
                                            mapboxMap.getCameraPosition().target.getLatitude()), Polygon.fromLngLats(boundingBoxList))) {
                                        if (levelButtons.getVisibility() != View.VISIBLE) {
                                            showLevelButton();
                                        }
                                    } else {
                                        if (levelButtons.getVisibility() == View.VISIBLE) {
                                            hideLevelButton();
                                        }
                                    }
                                } else if (levelButtons.getVisibility() == View.VISIBLE) {
                                    hideLevelButton();
                                }
                            }
                        });

                        //Load geojson data of the first floor
                        indoorBuildingSource = new GeoJsonSource("indoor-building", loadGeoJsonFromAsset("Planta_Baja_CECEQ.geojson"));

                        /*try {
                            URI geoJsonUrl = new URI("https://www.upyougo.com.mx/Planta_Alta_CECEQ.geojson");
                            indoorBuildingSource= new GeoJsonSource("geojson-source", geoJsonUrl);
                            style.addSource(indoorBuildingSource);
                        } catch (URISyntaxException exception) {
                            //Log.d(TAG, exception);
                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
                        }*/

                        style.addSource(indoorBuildingSource);

                        //The map listener is declared
                        mapboxMap.addOnMapClickListener(MainActivity.this);

                        //The style is given to the map
                        loadBuildingLayer(style);


                    }
                });

                //Variable for the button (ground and second level)
                final Button buttonGroundLevel = findViewById(R.id.ground_level_button);
                final Button buttonSecondLevel = findViewById(R.id.second_level_button);


                buttonSecondLevel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        indoorBuildingSource.setGeoJson(loadGeoJsonFromAsset("Planta_Alta_CECEQ.geojson"));
                        buttonSecondLevel.setBackgroundResource(R.drawable.rounded_button_light_blue);
                        buttonGroundLevel.setBackgroundResource(R.drawable.rounded_button_white);
                        Toast.makeText(MainActivity.this, R.string.change_second_level, Toast.LENGTH_LONG).show();
                    }
                });


                buttonGroundLevel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        indoorBuildingSource.setGeoJson(loadGeoJsonFromAsset("Planta_Baja_CECEQ.geojson"));
                        buttonSecondLevel.setBackgroundResource(R.drawable.rounded_button_white);
                        buttonGroundLevel.setBackgroundResource(R.drawable.rounded_button_light_blue);
                        Toast.makeText(MainActivity.this, R.string.change_ground_level, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }



    @Override
    protected void onResume() {
        //Lifecycle method
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        //Lifecycle method
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        //Lifecycle method
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        //Lifecycle method
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        //Lifecycle method
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        //Lifecycle method
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Lifecycle method
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void hideLevelButton() {
        //Method to hide the navigation buttons once the CameraListener is far away from the CECEQ
        // When the user moves away from our bounding box region or zooms out far enough the floor level
        // buttons are faded out and hidden.
        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(500);
        levelButtons.startAnimation(animation);
        levelButtons.setVisibility(View.GONE);
    }

    private void showLevelButton() {
        //Method to show the navigation buttons once the CameraListener is near the CECEQ
        // When the user moves inside our bounding box region or zooms in to a high enough zoom level,
        // the floor level buttons are faded out and hidden.
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500);
        levelButtons.startAnimation(animation);
        levelButtons.setVisibility(View.VISIBLE);
    }



    private String loadGeoJsonFromAsset(String filename) {
        //Method to load the geojson file and return it in a string format
        // Using this method to load in GeoJSON files from the assets folder.
        try {
            InputStream is = getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, Charset.forName("UTF-8"));

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void loadBuildingLayer(@NonNull Style style) {
        // Method used to load the indoor layer on the map. First the fill layer is drawn and then the
        // line layer is added.

        FillLayer indoorBuildingLayer = new FillLayer("indoor-building-fill", "indoor-building").withProperties(
                //FALTA CAMBIAR EL COLOR EL RESOURCE COLOR
                fillColor(Color.parseColor("#b9bcbd")),
        // Function.zoom is used here to fade out the indoor layer if zoom level is beyond 16. Only
        // necessary to show the indoor map at high zoom levels.
                fillOpacity(interpolate(exponential(1f), zoom(),
                        stop(16f, 0f),
                        stop(16.5f, 0.5f),
                        stop(17f, 1f))));

        style.addLayer(indoorBuildingLayer);

        LineLayer indoorBuildingLineLayer = new LineLayer("indoor-building-line", "indoor-building").withProperties(
                //FALTA CAMBIAR EL COLOR EL RESOURCE COLOR
                lineColor(Color.parseColor("#50667f")),
                lineWidth(0.5f),
                lineOpacity(interpolate(exponential(1f), zoom(),
                        stop(16f, 0f),
                        stop(16.5f, 0.5f),
                        stop(17f, 1f))));
        style.addLayer(indoorBuildingLineLayer);
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);

            initLocationEngine();
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }



    private void initLocationEngine() {
        //Mthod to continuos track the users location
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);

        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();

        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Method to ask for the user's permission
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        //Method to show a toast of the reason of using the user's location
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        //Method to show a toast if user's location information is not granted
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }


    public boolean onMapClick(@NonNull final LatLng point) {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                final PointF finalPoint = mapboxMap.getProjection().toScreenLocation(point);
                List<Feature> features = mapboxMap.queryRenderedFeatures(finalPoint, "indoor-building-fill");
                if (features.size() > 0) {
                    GeoJsonSource selectedBuildingSource = style.getSourceAs("indoor-building");
                    if (selectedBuildingSource != null) {
                        selectedBuildingSource.setGeoJson(FeatureCollection.fromFeatures(features));
                    }
                }
            }
        });
        return true;
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {

    }

    private static class LocationChangeListeningMainActivityLocationCallback
            implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<MainActivity> activityWeakReference;

        LocationChangeListeningMainActivityLocationCallback(MainActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location has changed.
         *
         * @param result the LocationEngineResult object which has the last known location within it.
         */
        @Override
        public void onSuccess(LocationEngineResult result) {
            MainActivity activity = activityWeakReference.get();

            if (activity != null) {
                Location location = result.getLastLocation();

                if (location == null) {
                    return;
                }

                // Create a Toast which displays the new location's coordinates
                /*Toast.makeText(activity, String.format(activity.getString(R.string.new_location),
                        String.valueOf(result.getLastLocation().getLatitude()),
                        String.valueOf(result.getLastLocation().getLongitude())),
                        Toast.LENGTH_SHORT).show();*/

            // Pass the new location to the Maps SDK's LocationComponent
                if (activity.mapboxMap != null && result.getLastLocation() != null) {
                    activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                }
            }
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location can't be captured
         *
         * @param exception the exception message
         */
        @Override
        public void onFailure(@NonNull Exception exception) {
            Log.d("LocationChangeActivity", exception.getLocalizedMessage());
            MainActivity activity = activityWeakReference.get();
            if (activity != null) {
                Toast.makeText(activity, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void InitialConnection (){
        String type="connection";
        DataBase database=new DataBase(this);
        database.execute(type);
    }

}
