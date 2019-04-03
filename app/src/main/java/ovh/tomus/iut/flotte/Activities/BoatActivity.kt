package ovh.tomus.iut.flotte.Activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_boat.*
import ovh.tomus.iut.flotte.Models.Containership
import ovh.tomus.iut.flotte.Models.ContainershipType
import ovh.tomus.iut.flotte.Models.Port

import ovh.tomus.iut.flotte.R

class BoatActivity : AppCompatActivity() {

    private lateinit var containership: Containership
    private lateinit var harbourList: ArrayList<Port>
    private lateinit var containerShipTypeList: ArrayList<ContainershipType>


    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boat)

        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        containership = intent.getSerializableExtra("CONTAINERSHIP") as Containership
        harbourList = intent.getSerializableExtra("HARBOURS") as ArrayList<Port>
        containerShipTypeList = intent.getSerializableExtra("HARBOURS") as ArrayList<ContainershipType>

        loadData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            loadData()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetTextI18n")
    fun loadData() {

        // Load bateau
        db.document(containership.id).get().addOnSuccessListener { containerShip ->
            title = containerShip["boatName"].toString()
            val geopoint : GeoPoint = containerShip["localization"] as GeoPoint

            boatName.text = containerShip["boatName"].toString()
            captainName.text = containerShip["captainName"].toString()
            coords.text = "[" + geopoint.latitude.toString() + "° N, " + geopoint.longitude.toString() + "° E]"
            val portref = containerShip["port"] as DocumentReference
            val typeref = containerShip["boatType"] as DocumentReference

            // Load port
            db.document(portref.path).get().addOnSuccessListener { port ->
                portText.text = port["name"].toString()
            }

            db.document(typeref.path).get().addOnSuccessListener { type ->
                typeText.text = type["name"].toString()
            }
        }


    }

    fun edit(view : View) {
        val page = Intent(this, BoatEditorActivity::class.java)

        page.putExtra("CONTAINERSHIP", containership)
        page.putExtra("HARBOURS", harbourList)
        page.putExtra("CONTAINERSHIPTYPES", containerShipTypeList)

        startActivityForResult(page, 1)
    }

    fun map(view : View) {
        val page = Intent(this, BoatsMapActivity::class.java)

        page.putExtra("CONTAINERSHIP", containership)

        startActivity(page)
    }

    fun listContainer(view: View) {
        val page = Intent(this, ListConteneurActivity::class.java)

        page.putExtra("CONTAINERSHIP", containership)
        page.putExtra("MODE", "list")

        startActivity(page)
    }

    fun addContainer(view: View) {
        val page = Intent(this, ListConteneurActivity::class.java)

        page.putExtra("CONTAINERSHIP", containership)
        page.putExtra("MODE", "add")

        startActivity(page)
    }

}
