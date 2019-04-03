package ovh.tomus.iut.flotte.Activities

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_boat_editor.*
import ovh.tomus.iut.flotte.Models.Containership
import ovh.tomus.iut.flotte.Models.ContainershipType
import ovh.tomus.iut.flotte.Models.Port
import ovh.tomus.iut.flotte.R

class BoatEditorActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private var containershiptypes = db.collection("containership-boatType")

    private var typeList = mutableMapOf<String, String>()

    private lateinit var containership: Containership
    private lateinit var harbourList: ArrayList<Port>
    private lateinit var containtershipTypeList: ArrayList<ContainershipType>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boat_editor)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        containership = intent.getSerializableExtra("CONTAINERSHIP") as Containership
        harbourList = intent.getSerializableExtra("HARBOURS") as ArrayList<Port>
        containtershipTypeList = intent.getSerializableExtra("CONTAINERSHIPTYPES") as ArrayList<ContainershipType>

        getHarbours()
        getTypesContainerships()
    }

    fun getHarbours() {
        val adapter = ArrayAdapter(this, R.layout.spinner_item, harbourList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        harbourspinner.adapter = adapter
    }

    fun getTypesContainerships() {
        val adapter = ArrayAdapter(this, R.layout.spinner_item, containtershipTypeList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typespinner.adapter = adapter
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

    fun updateData (view: View){
        val containerShipRef = db.document(containership.id)

        val boatName = edit_boatname.text.toString()
        val captainName = edit_captainname.text.toString()

        // Si la nouvelle latitude/longitude n'est pas renseignée alors on remet les anciennes
        val latitudeInput = latitudeInput.text.toString()
        val longitudeInput = longitudeInput.text.toString()

        if (boatName.isNotEmpty()) {
            containership.boatName = boatName
        }

        if (captainName.isNotEmpty()) {
            containership.captainName = captainName
        }

        if (latitudeInput.isNotBlank() && latitudeInput.toDouble() <= 90 && latitudeInput.toDouble() >= -90) {
            containership.latitude = latitudeInput.toDouble()
        }

        if (longitudeInput.isNotBlank() && longitudeInput.toDouble() <= 180 && longitudeInput.toDouble() >= -180) {
            containership.latitude = longitudeInput.toDouble()
        }

        containership.port = harbourspinner.selectedItem as Port
        containership.boatType = typespinner.selectedItem as ContainershipType


        containerShipRef.set(containership)

        setResult(Activity.RESULT_OK)
        finish()

    }

}
