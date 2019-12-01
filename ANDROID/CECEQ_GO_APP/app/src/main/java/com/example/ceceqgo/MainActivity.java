package com.example.ceceqgo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfRenderer;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonElement;
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
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngQuad;
import com.mapbox.mapboxsdk.location.CompassEngine;
import com.mapbox.mapboxsdk.location.CompassListener;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.RasterLayer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.ImageSource;
import com.mapbox.turf.TurfJoins;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import timber.log.Timber;


import static android.provider.Telephony.Mms.Part.FILENAME;
import static com.mapbox.mapboxsdk.style.expressions.Expression.exponential;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.expressions.Expression.zoom;
import static com.mapbox.mapboxsdk.style.layers.Property.TEXT_ANCHOR_BOTTOM;
import static com.mapbox.mapboxsdk.style.layers.Property.TEXT_ANCHOR_LEFT;
import static com.mapbox.mapboxsdk.style.layers.Property.TEXT_ANCHOR_RIGHT;
import static com.mapbox.mapboxsdk.style.layers.Property.TEXT_ANCHOR_TOP;
import static com.mapbox.mapboxsdk.style.layers.Property.TEXT_JUSTIFY_AUTO;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textJustify;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textRadialOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textVariableAnchor;

public class MainActivity extends AppCompatActivity implements PermissionsListener,OnMapReadyCallback,MapboxMap.OnMapClickListener {


    private ListView listview;

    private ArrayList<String> names;

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
    private CompassEngine compassEngine;
    private LocationChangeListeningMainActivityLocationCallback callback = new LocationChangeListeningMainActivityLocationCallback(MainActivity.this);

    //Variable for dinamic map interaction
    private static final String geoJsonLayerId = "polygonFillLayer";

    //Variables for the search bar
    private MaterialSearchBar searchBar;

    //Variables to display the bathrooms' images
    ImageSource banosAimg;
    ImageSource banosBimg;
    ImageSource banosCimg;
    ImageSource banosDimg;
    LatLngQuad banosAB;
    LatLngQuad banosBB;
    LatLngQuad banosCB;
    LatLngQuad banosDB;
    LatLngQuad banosAA;
    LatLngQuad banosBA;
    LatLngQuad banosCA;
    LatLngQuad banosDA;

    //Variables to display the stairs' images
    ImageSource escalerasAimg;
    ImageSource escalerasBimg;
    ImageSource escalerasCimg;
    ImageSource escalerasDimg;
    LatLngQuad escalerasAB;
    LatLngQuad escalerasBB;
    LatLngQuad escalerasCB;
    LatLngQuad escalerasDB;
    LatLngQuad escalerasAA;
    LatLngQuad escalerasBA;
    LatLngQuad escalerasCA;
    LatLngQuad escalerasDA;

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


                        banosAB = new LatLngQuad(
                                new LatLng(20.585832,
                                        -100.386788),
                                new LatLng(20.585903,
                                        -100.386752),

                                new LatLng(20.585875,
                                        -100.386687),
                                new LatLng(20.585803,
                                        -100.386722)
                        );


                        banosBB = new LatLngQuad(
                                new LatLng(20.58614,
                                        -100.386309),
                                new LatLng(20.586194,
                                        -100.386283),
                                new LatLng(20.586168,
                                        -100.386219),
                                new LatLng(20.586113,
                                        -100.386245)
                        );


                        banosCB = new LatLngQuad(
                                new LatLng(20.585694,
                                        -100.385996),
                                new LatLng(20.585757,
                                        -100.385967),
                                new LatLng(20.585727,
                                        -100.385895),
                                new LatLng(20.585665,
                                        -100.385925)
                        );

                        banosDB = new LatLngQuad(
                                new LatLng(20.585384,
                                        -100.38649),
                                new LatLng(20.58545,
                                        -100.386458),
                                new LatLng(20.585418,
                                        -100.386382),
                                new LatLng(20.585352,
                                        -100.386413)
                        );


                        banosAA = new LatLngQuad(
                                new LatLng(20.585847,
                                        -100.386785),
                                new LatLng(20.585909,
                                        -100.386755),

                                new LatLng(20.585882,
                                        -100.386691),
                                new LatLng(20.58582,
                                        -100.38672)
                        );

                        banosBA = new LatLngQuad(
                                new LatLng(20.586138,
                                        -100.386312),
                                new LatLng(20.586198,
                                        -100.386283),
                                new LatLng(20.586167,
                                        -100.38621),
                                new LatLng(20.586108,
                                        -100.386241)
                        );

                        banosCA = new LatLngQuad(
                                new LatLng(20.585695,
                                        -100.386022),
                                new LatLng(20.585763,
                                        -100.385989),
                                new LatLng(20.585731,
                                        -100.385915),
                                new LatLng(20.585663,
                                        -100.385948)
                        );


                        banosDA = new LatLngQuad(
                                new LatLng(20.585402,
                                        -100.386501),
                                new LatLng(20.585468,
                                        -100.386467),
                                new LatLng(20.585438,
                                        -100.386398),
                                new LatLng(20.585372,
                                        -100.386432)
                        );

                        escalerasAB = new LatLngQuad(
                                new LatLng(20.585991,
                                        -100.386707),
                                new LatLng(20.586057,
                                        -100.386675),
                                new LatLng(20.58603,
                                        -100.386612),
                                new LatLng(20.585964,
                                        -100.386643)
                        );

                        escalerasBB = new LatLngQuad(
                                new LatLng(20.586069,
                                        -100.386137),
                                new LatLng(20.586123,
                                        -100.386111),
                                new LatLng(20.586097,
                                        -100.386048),
                                new LatLng(20.586042,
                                        -100.386076)
                        );


                        escalerasCB = new LatLngQuad(
                                new LatLng(20.585521,
                                        -100.386077),
                                new LatLng(20.585593,
                                        -100.386042),
                                new LatLng(20.585562,
                                        -100.38597),
                                new LatLng(20.585491,
                                        -100.386005)
                        );


                        escalerasDB = new LatLngQuad(
                                new LatLng(20.585452,
                                        -100.386651),
                                new LatLng(20.585514,
                                        -100.386622),
                                new LatLng(20.585486,
                                        -100.386554),
                                new LatLng(20.585424,
                                        -100.386583)
                        );


                        escalerasAA = new LatLngQuad(
                                new LatLng(20.585982,
                                        -100.386718),
                                new LatLng(20.586039,
                                        -100.386689),
                                new LatLng(20.586014,
                                        -100.386628),
                                new LatLng(20.585956,
                                        -100.386656)
                        );

                        escalerasBA = new LatLngQuad(
                                new LatLng(20.586079,
                                        -100.386173),
                                new LatLng(20.586139,
                                        -100.386142),
                                new LatLng(20.586108,
                                        -100.386075),
                                new LatLng(20.586049,
                                        -100.386106)
                        );

                        escalerasCA = new LatLngQuad(
                                new LatLng(20.585533,
                                        -100.38609),
                                new LatLng(20.585598,
                                        -100.38606),
                                new LatLng(20.585569,
                                        -100.385989),
                                new LatLng(20.585505,
                                        -100.386019)
                        );


                        escalerasDA = new LatLngQuad(
                                new LatLng(20.585463,
                                        -100.386634),
                                new LatLng(20.585527,
                                        -100.386599),
                                new LatLng(20.585497,
                                        -100.386531),
                                new LatLng(20.585432,
                                        -100.386566)
                        );






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

                        //Add the image source in a specific location for bathrooms
                        banosAimg=new ImageSource("banosA",banosAB,R.drawable.wc_90);
                        banosBimg=new ImageSource("banosB",banosBB,R.drawable.wc_90);
                        banosCimg=new ImageSource("banosC",banosCB,R.drawable.wc_90);
                        banosDimg=new ImageSource("banosD",banosDB,R.drawable.wc_90);

                        //Add the image source in a specific location for stairs
                        escalerasAimg=new ImageSource("escaleraA",escalerasAB,R.drawable.stair_90);
                        escalerasBimg=new ImageSource("escaleraB",escalerasBB,R.drawable.stair_90);
                        escalerasCimg=new ImageSource("escaleraC",escalerasCB,R.drawable.stair_90);
                        escalerasDimg=new ImageSource("escaleraD",escalerasDB,R.drawable.stair_90);

                        //Add bathrooms as a source to the style
                        style.addSource(banosAimg);
                        style.addSource(banosBimg);
                        style.addSource(banosCimg);
                        style.addSource(banosDimg);

                        //Add stairs as a source to the style
                        style.addSource(escalerasAimg);
                        style.addSource(escalerasBimg);
                        style.addSource(escalerasCimg);
                        style.addSource(escalerasDimg);


                        //The style is given to the map

                        loadBuildingLayer(style);

                        //The map listener is declared
                        mapboxMap.addOnMapClickListener(MainActivity.this);

                        //Add maker position to all Access
                        mapboxMap.addMarker(new MarkerOptions()
                                .position(new LatLng(20.585951, -100.386738))
                                .title("Acceso A"));

                        mapboxMap.addMarker(new MarkerOptions()
                                .position(new LatLng(20.586149, -100.386162))
                                .title("Acceso B"));

                        mapboxMap.addMarker(new MarkerOptions()
                                .position(new LatLng(20.58561, -100.385941))
                                .title("Acceso C"));

                        mapboxMap.addMarker(new MarkerOptions()
                                .position(new LatLng(20.585402, -100.386537))
                                .title("Acceso D"));

                        mapboxMap.addMarker(new MarkerOptions()
                                .position(new LatLng(20.585523, -100.386818))
                                .title("Acceso Principal"));

                        //Enable user's location
                        enableLocationComponent(style);


                    }
                });

                //Variable for the button (ground and second level)
                final FloatingActionButton buttonGroundLevel = (FloatingActionButton)findViewById(R.id.ground_level_button);
                final FloatingActionButton buttonSecondLevel = (FloatingActionButton) findViewById(R.id.second_level_button);
                final TextView pa = findViewById(R.id.pa);
                final TextView pb = findViewById(R.id.pb);


                buttonSecondLevel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        indoorBuildingSource.setGeoJson(loadGeoJsonFromAsset("Planta_Alta_CECEQ.geojson"));
                        Toast.makeText(MainActivity.this, R.string.change_second_level, Toast.LENGTH_LONG).show();

                        pa.bringToFront();
                        pb.bringToFront();

                        //Change bathrooms image location
                        banosAimg.setCoordinates(banosAA);
                        banosBimg.setCoordinates(banosBA);
                        banosCimg.setCoordinates(banosCA);
                        banosDimg.setCoordinates(banosDA);

                        //Change stairs image location
                        escalerasAimg.setCoordinates(escalerasAA);
                        escalerasBimg.setCoordinates(escalerasBA);
                        escalerasCimg.setCoordinates(escalerasCA);
                        escalerasDimg.setCoordinates(escalerasDA);

                    }
                });


               buttonGroundLevel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        indoorBuildingSource.setGeoJson(loadGeoJsonFromAsset("Planta_Baja_CECEQ.geojson"));
                        Toast.makeText(MainActivity.this, R.string.change_ground_level, Toast.LENGTH_LONG).show();

                        pa.bringToFront();
                        pb.bringToFront();

                        //Change bathrooms image location
                        banosAimg.setCoordinates(banosAB);
                        banosBimg.setCoordinates(banosBB);
                        banosCimg.setCoordinates(banosCB);
                        banosDimg.setCoordinates(banosDB);

                        //Change stairs image location
                        escalerasAimg.setCoordinates(escalerasAB);
                        escalerasBimg.setCoordinates(escalerasBB);
                        escalerasCimg.setCoordinates(escalerasCB);
                        escalerasDimg.setCoordinates(escalerasDB);
                    }
                });

               //Trigger the mail APP
                final ru.dimorinny.floatingtextbutton.FloatingTextButton send_mail = (ru.dimorinny.floatingtextbutton.FloatingTextButton)findViewById(R.id.correo_button);
                send_mail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(MainActivity.this, "SIRVE", Toast.LENGTH_LONG).show();
                        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                        /* Fill it with Data */
                        emailIntent.setType("plain/text");
                        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"quejasydenuncias@queretaro.gob.mx"});
                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Comentarios de Experiencia de Uso");
                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Mediante el presente correo comparto mi experiencia de uso con la aplicación CECEQ.");

                        /* Send it off to the Activity-Chooser */
                       startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    }
                });

                //Trigger the mail APP
                final ru.dimorinny.floatingtextbutton.FloatingTextButton send_mail_quejas = findViewById(R.id.quejas_y_sujerencias);
                send_mail_quejas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(MainActivity.this, "SIRVE", Toast.LENGTH_LONG).show();
                        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                        /* Fill it with Data */
                        emailIntent.setType("plain/text");
                        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"quejasydenuncias@queretaro.gob.mx"});
                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Queja de la Aplicación");
                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Por medio del presente muestro las inquietudes que presentó mi vista al CECEQ:");

                        /* Send it off to the Activity-Chooser */
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
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

        SymbolLayer labels= new SymbolLayer("indoor-building-label", "indoor-building").withProperties(
                textField(get("label")),
                textSize(17f),
                textColor(Color.BLACK),
                textVariableAnchor(
                        new String[]{TEXT_ANCHOR_TOP, TEXT_ANCHOR_BOTTOM, TEXT_ANCHOR_LEFT, TEXT_ANCHOR_RIGHT}),
                textJustify(TEXT_JUSTIFY_AUTO),
                textRadialOffset(0.5f)
        );

        FillLayer indoorBuildingLayer = new FillLayer("indoor-building-fill", "indoor-building").withProperties(
                //FALTA CAMBIAR EL COLOR EL RESOURCE COLOR
                fillColor(Color.parseColor("#9CBFD9")),
                // Function.zoom is used here to fade out the indoor layer if zoom level is beyond 16. Only
                // necessary to show the indoor map at high zoom levels.
                fillOpacity(interpolate(exponential(1f), zoom(),
                        stop(16f, 0f),
                        stop(16.5f, 0.5f),
                        stop(17f, 1f))));


        LineLayer indoorBuildingLineLayer = new LineLayer("indoor-building-line", "indoor-building").withProperties(
                //FALTA CAMBIAR EL COLOR EL RESOURCE COLOR
                lineColor(Color.parseColor("#50667f")),
                lineWidth(0.5f),
                lineOpacity(interpolate(exponential(1f), zoom(),
                        stop(16f, 0f),
                        stop(16.5f, 0.5f),
                        stop(17f, 1f))));

        style.addLayer(indoorBuildingLayer);
        style.addLayer(indoorBuildingLineLayer);
        style.addLayer(new RasterLayer("image_layer_banosA", "banosA"));
        style.addLayer(new RasterLayer("image_layer_banosB", "banosB"));
        style.addLayer(new RasterLayer("image_layer_banosC", "banosC"));
        style.addLayer(new RasterLayer("image_layer_banosD", "banosD"));


        style.addLayer(new RasterLayer("image_layer_escaleraA", "escaleraA"));
        style.addLayer(new RasterLayer("image_layer_escaleraB", "escaleraB"));
        style.addLayer(new RasterLayer("image_layer_escaleraC", "escaleraC"));
        style.addLayer(new RasterLayer("image_layer_escaleraD", "escaleraD"));
        style.addLayer(labels);


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
            locationComponent.setCameraMode(CameraMode.TRACKING_COMPASS);

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
        compassEngine = new CompassEngine() {
            @Override
            public void addCompassListener(@NonNull CompassListener compassListener) {
            }

            @Override
            public void removeCompassListener(@NonNull CompassListener compassListener) {

            }

            @Override
            public float getLastHeading() {
                return 0;
            }

            @Override
            public int getLastAccuracySensorStatus() {
                return 0;
            }
        };
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
            //finish();
        }
    }


    public boolean onMapClick(@NonNull final LatLng point) {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                // Convert LatLng coordinates to screen pixel and only query the rendered features.
                final PointF finalPoint = mapboxMap.getProjection().toScreenLocation(point);

                List<Feature> features = mapboxMap.queryRenderedFeatures(finalPoint, "indoor-building-fill");
                // Get the first feature within the list if one exist
                if (features.size() > 0) {
                    Feature feature = features.get(0);
                    /*Layer layer=style.getLayer("indoor-building-fill");
                    layer=new FillLayer();

                    FillLayer indoorBuildingLayer = new FillLayer("indoor-building-fill", "indoor-building").withProperties(
                            //FALTA CAMBIAR EL COLOR EL RESOURCE COLOR
                            fillColor(Color.parseColor("#9CBFD9")),
                            // Function.zoom is used here to fade out the indoor layer if zoom level is beyond 16. Only
                            // necessary to show the indoor map at high zoom levels.
                            fillOpacity(interpolate(exponential(1f), zoom(),
                                    stop(16f, 0f),
                                    stop(16.5f, 0.5f),
                                    stop(17f, 1f))));
    */




                    if (feature.properties() != null) {
                        for (Map.Entry<String, JsonElement> entry : feature.properties().entrySet()) {
                            // Log all the properties
                            Log.d(TAG, String.format("%s = %s", entry.getKey(), entry.getValue()));
                        }
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


