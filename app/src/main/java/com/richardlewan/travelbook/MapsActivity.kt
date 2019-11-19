package com.richardlewan.travelbook

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.richardlewan.travelbook.application.TravelbookApplication
import com.richardlewan.travelbook.service.PersistService
import java.lang.Exception
import java.util.Locale
import javax.inject.Inject

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    @Inject
    lateinit var locationsList: ArrayList<LatLng>

    @Inject
    lateinit var namesList: ArrayList<String>

    @Inject
    lateinit var persistService: PersistService

    private lateinit var googleMap: GoogleMap

    var locationManager : LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        (application as TravelbookApplication).travelbookComponent.inject(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        this.googleMap.setOnMapLongClickListener(myListener)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // We don't yet have access to location, so request it from the user.
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            val intent = intent
            val info = intent.getStringExtra("info")
            if ("new".equals(info)) {
                // The Add Place button was selected
                this.googleMap.clear()
                val lastLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                var lastUserLocation = if (lastLocation != null) { LatLng(lastLocation.latitude, lastLocation.longitude) }
                                        else { LatLng(46.869295, -113.996291) }
                this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 17f))
            } else {
                // List selection from MainActivity
                this.googleMap.clear()
                val latitude = intent.getDoubleExtra("latitude",0.0)
                val longitude = intent.getDoubleExtra("longitude",0.0)
                val name = intent.getStringExtra("name")
                val location = LatLng(latitude, longitude)
                val locationMarker = this.googleMap.addMarker(MarkerOptions().position(location).title(name))
                locationMarker.showInfoWindow()
                this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17f))
            }
        }
    }

    val myListener = object : GoogleMap.OnMapLongClickListener {
        override fun onMapLongClick(p0: LatLng?) {
            // Get the address based on location p0
            val geocoder = Geocoder(applicationContext, Locale.US)
            var address = ""
            try {
                val addressList = geocoder.getFromLocation(p0!!.latitude, p0.longitude, 1)
                if (addressList != null && addressList.isNotEmpty()) {
                    val thoroughfare = addressList[0].thoroughfare
                    if (thoroughfare != null) {
                        addressList[0].subThoroughfare?.let{address += "$it "}
                        address += thoroughfare
                    }
                } else {
                    address = "New Place"
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            // Mark the location with its address
            val locationMarker = googleMap.addMarker(MarkerOptions().position(p0!!).title(address))
            locationMarker.showInfoWindow()
            namesList.add(address)
            locationsList.add(p0)
            Toast.makeText(applicationContext, "New Place Created", Toast.LENGTH_LONG).show()

            // Add the address, lat, long to the sqlLite db as 'place' record.
            persistService.savePlace(applicationContext, address, p0.latitude, p0.longitude)
        }
    }
}
