package com.example.ceceq_go

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Gravity
import android.widget.Toast
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineListener
import com.mapbox.android.core.location.LocationEnginePriority
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode
import com.mancj.materialsearchbar.MaterialSearchBar
import com.mapbox.mapboxsdk.constants.Style
import com.sothree.slidinguppanel.SlidingUpPanelLayout


class MainActivity : AppCompatActivity(),PermissionsListener,LocationEngineListener {
    //Variable for the map
    private lateinit var mapView: MapView
    //Variable of class MapboxMap that allows to interact in general with Android Mapbox sdk
    private lateinit var map:MapboxMap
    //Variable of Mapbox permission class to ask permission to use location
    private lateinit var permissionManager:PermissionsManager
    //Variable to store the current location al all time
    private lateinit var originLocation:Location
    //Variable to UI to represent the user current location
    private var locationEngine:LocationEngine?=null
    private var locationLayerPlugin: LocationLayerPlugin?=null
    //Variables for SearchBar

    //Variables for SlidingUpPanel
    private lateinit var slidingUpLayout: SlidingUpPanelLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Give Mapbox the context and the access token
        Mapbox.getInstance(applicationContext, getString(R.string.mapbox_access_token))
        mapView=findViewById(R.id.mapView)

        //Create de MapView
        mapView.onCreate(savedInstanceState)

        //Make map variable available for the class
        mapView.getMapAsync{mapboxMap->
            map=mapboxMap
            enableLocation()
            mapboxMap.setStyle(Style.MAPBOX_STREETS) {

                // Map is set up and the style has loaded. Now you can add data or make other map adjustments



            }
        }

        //SlidingUpPanel
        //slidingUpLayout=findViewById(R.id.slideUpPanel_all)

    }

    //Function to ask for user's location permissions
    private fun enableLocation(){
        if(PermissionsManager.areLocationPermissionsGranted(this)){
            initializeLocationEngine()
            initializeLocationLayer()
        }else{
            permissionManager= PermissionsManager(this)
            permissionManager.requestLocationPermissions(this)
        }
    }

    //Function to get the location of the user using the LocationEngine Class
    @SuppressWarnings("MissingPermission")
    private fun initializeLocationEngine(){
        locationEngine=LocationEngineProvider(this).obtainBestLocationEngineAvailable()
        locationEngine?.priority=LocationEnginePriority.HIGH_ACCURACY
        locationEngine?.activate()

        val lastlocation=locationEngine?.lastLocation
        if(lastlocation!=null){
            originLocation=lastlocation
            setCameraPosition(lastlocation)
        }else{
            locationEngine?.addLocationEngineListener(this)
        }
    }

    //Function to see onscreen the current location of the user obtained by the location engine
    @SuppressWarnings("MissingPermission")
    private fun initializeLocationLayer(){
        locationLayerPlugin= LocationLayerPlugin(mapView,map,locationEngine)
        locationLayerPlugin?.setLocationLayerEnabled(true)
        locationLayerPlugin?.cameraMode=CameraMode.TRACKING
        locationLayerPlugin?.renderMode=RenderMode.COMPASS
    }

    //Function to move the camera to new position
    private fun setCameraPosition(location:Location){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
            LatLng(location.latitude,location.longitude),25.0)
        )
    }

    //Function used when users denies permission for first time
    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        //Present a toast or a dialog why they need to grant access
        //val locationToast = Toast.makeText(applicationContext,"CECEQ GO requiere de los permisos de ubicación para continuar.",Toast.LENGTH_SHORT)
        //locationToast.setGravity(Gravity.LEFT,200,200)
        //locationToast.show()
        //enableLocation()
    }

    override fun onPermissionResult(granted: Boolean) {
        if(granted){
            enableLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissionManager.onRequestPermissionsResult(requestCode, permissions,grantResults)
    }

    //Function called when the user moves
    override fun onLocationChanged(location: Location?) {
        location?.let {
            originLocation=location
            setCameraPosition(location)
        }
    }

    @SuppressWarnings("MissingPermission")
    override fun onConnected() {
        locationEngine?.requestLocationUpdates()
    }

    //Override lifecycle methods
    @SuppressWarnings("MissingPermission")
    override fun onStart() {
        super.onStart()
        if(PermissionsManager.areLocationPermissionsGranted(this)){
            locationEngine?.requestLocationUpdates()
            locationLayerPlugin?.onStart()
        }
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        locationEngine?.removeLocationUpdates()
        locationLayerPlugin?.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        locationEngine?.deactivate()

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (outState!=null){
            mapView.onSaveInstanceState(outState)
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
    //Override lifecycle methods
}
