package ovh.tomus.iut.flotte.Activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_boat_editor.*
import ovh.tomus.iut.flotte.Models.Container
import ovh.tomus.iut.flotte.Models.Containership
import ovh.tomus.iut.flotte.R

class BoatEditorActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private var harbours = db.collection("harbours")
    private var containershiptypes = db.collection("containership-type")

    private var harbourList = mutableMapOf<String, String>()
    private var typeList = mutableMapOf<String, String>()

    private lateinit var docref : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boat_editor)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        docref = intent.getStringExtra("DOCREF")

        getHarbours()
        getTypesContainerships()
    }

    fun getHarbours() {
        harbours.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (harbour in task.result!!) {
                    harbourList[harbour.data["name"].toString()] = harbour.id
                }
                val adapter = ArrayAdapter(
                    this, R.layout.spinner_item, harbourList.keys.toTypedArray()
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                harbourspinner.adapter = adapter
            }
        }
    }

    fun getTypesContainerships() {
        containershiptypes.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (types in task.result!!) {
                    typeList[types.data["name"].toString()] = types.id
                }

                val adapter = ArrayAdapter(
                    this, R.layout.spinner_item, typeList.keys.toTypedArray()
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                typespinner.adapter = adapter
            }
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

    fun updateData (view: View){
        val boatAttribute = mutableMapOf<String,Any>()
        val containerShipRef = db.document(docref)

        val boatName = edit_boatname.text.toString()
        val captainName = edit_captainname.text.toString()

        // Si la nouvelle latitude/longitude n'est pas renseignée alors on remet les anciennes
        val latitudeInput = latitudeInput.text.toString()
        val longitudeInput = longitudeInput.text.toString()

        var latitude = 0.0
        var longitude = 0.0

        if (boatName.isNotEmpty()) {
            boatAttribute["boatName"] = boatName
        }

        if (captainName.isNotEmpty()) {
            boatAttribute["captainName"] = captainName
        }

        if (latitudeInput.isNotBlank() && latitudeInput.toDouble() <= 90 && latitudeInput.toDouble() >= -90) {
            latitude = latitudeInput.toDouble()
        }

        if (longitudeInput.isNotBlank() && longitudeInput.toDouble() <= 180 && longitudeInput.toDouble() >= -180) {
            longitude = longitudeInput.toDouble()
        }

        boatAttribute["port"] = harbours.document(harbourList.get(harbourspinner.selectedItem).toString())
        boatAttribute["boatType"] = containershiptypes.document(typeList.get(typespinner.selectedItem).toString())

        boatAttribute["localization"] = GeoPoint(latitude, longitude)


        for (attribute in boatAttribute)
            containerShipRef.update(attribute.key, attribute.value)

        setResult(Activity.RESULT_OK)
        finish()

    }

}
