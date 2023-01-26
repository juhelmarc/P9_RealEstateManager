package com.openclassrooms.realestatemanager.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityMapBinding
import com.openclassrooms.realestatemanager.ui.detail.DetailActivity
import com.openclassrooms.realestatemanager.ui.map.PermissionUtils.PermissionDeniedDialog.Companion.newInstance
import com.openclassrooms.realestatemanager.ui.map.PermissionUtils.isPermissionGranted
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MapsActivity: AppCompatActivity(),
    OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    OnRequestPermissionsResultCallback{

    private val viewModel by viewModels<MapsViewModel>()

    private val DEFAULT_ZOOM : Float = 14f
    private lateinit var map: GoogleMap
    private var permissionDenied = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        googleMap.setOnMyLocationButtonClickListener(this)
        googleMap.setOnMyLocationClickListener(this)
        enableMyLocation()

        viewModel.mapsViewStateListLiveData.observe(this) { listMapsViewState ->
            addMarkers(googleMap, listMapsViewState)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(listMapsViewState[0].lat!!, listMapsViewState[0].lng!!), DEFAULT_ZOOM))
        }
    }
    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {

        // [START maps_check_location_permission]
        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            return
        }

        // 2. If if a permission rationale dialog should be shown
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            PermissionUtils.RationaleDialog.newInstance(
                LOCATION_PERMISSION_REQUEST_CODE, true
            ).show(supportFragmentManager, "dialog")
            return
        }

        // 3. Otherwise, request permission
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
        // [END maps_check_location_permission]
    }

    private fun addMarkers(googleMap: GoogleMap, listMapsViewState: List<MapsViewState>) {
        listMapsViewState.forEach { viewState ->
            if(viewState.lat != null && viewState.lng != null) {
                val marker = googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(viewState.lat, viewState.lng))
                        .title("${viewState.price} $  ${viewState.type}")

                )

            }
        }

        map.setOnMarkerClickListener { marker ->
            viewModel.mapsViewStateListLiveData.observe(this) { listMapsViewState ->
                listMapsViewState.forEach { viewState ->
                    if(LatLng(viewState.lat!!, viewState.lng!!) == marker.position) {
                        viewModel.onItemMarkerClick(viewState.id)
                        finish()
//                        startActivity(Intent(this@MapsActivity, DetailActivity::class.java))
                    }
                }
            }
        true
        }
        googleMap.uiSettings.isZoomControlsEnabled = true
    }





    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT)
            .show()
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(this, "Current location:\n$location", Toast.LENGTH_LONG)
            .show()
    }

    // [START maps_check_location_permission_result]
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
            )
            return
        }

        if (isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) || isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation()
        } else {
            // Permission was denied. Display an error message
            // [START_EXCLUDE]
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true
            // [END_EXCLUDE]
        }
    }

    // [END maps_check_location_permission_result]
    override fun onResumeFragments() {
        super.onResumeFragments()
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError()
            permissionDenied = false
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private fun showMissingPermissionError() {
        newInstance(true).show(supportFragmentManager, "dialog")
    }

    companion object {
        /**
         * Request code for location permission request.
         *
         * @see .onRequestPermissionsResult
         */
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }


}