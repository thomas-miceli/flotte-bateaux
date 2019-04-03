package ovh.tomus.iut.flotte.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import ovh.tomus.iut.flotte.Models.Containership
import ovh.tomus.iut.flotte.R

class BoatsMapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    val db = FirebaseFirestore.getInstance()
    val boatList = mutableMapOf<String,String>()

    private lateinit var containership: Containership
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boats_map)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        containership = intent.getSerializableExtra("CONTAINERSHIP") as Containership
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
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val shipRef = db.document(containership.id).get().addOnSuccessListener { task ->
            val geoPoint = task["localization"] as GeoPoint
            println(geoPoint.latitude)
            val marker = LatLng(geoPoint.latitude,geoPoint.longitude)
            map.addMarker(MarkerOptions().position(marker).title("Bateau"))
            println("addpassed")
            map.moveCamera(CameraUpdateFactory.newLatLng(marker))
            println("Camerapassed")
            map.getUiSettings().setZoomControlsEnabled(true)
            map.setOnMarkerClickListener(this)

        }

    }

    override fun onMarkerClick(p0: Marker?) = false
}