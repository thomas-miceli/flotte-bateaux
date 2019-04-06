package ovh.tomus.iut.flotte.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import ovh.tomus.iut.flotte.R
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.synthetic.main.activity_addboat.*


class AddBoatActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    var boats = db.collection("containerShips")
    var harbours = db.collection("harbours")
    var harbourList = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addboat)

        getHarbours()
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

    fun addBoat(view: View) {

        val boatName: String = boatname.text.toString()
        val captainName: String = captainname.text.toString()
        val latitude : String = latitudeInput.text.toString()
        val longitude : String = longitudeInput.text.toString()
        val harbour: DocumentReference = harbours.document(harbourList.get(harbourspinner.selectedItem).toString())

        if (boatName.isNotBlank() && captainName.isNotBlank() && latitudeInput.text.isNotBlank() && longitudeInput.text.isNotBlank()) {

            if (latitude.toDouble() >= -90 && latitude.toDouble() <= 90 && longitude.toDouble() >= -180 && longitude.toDouble() <= 180) {
                val localization = GeoPoint(latitude.toDouble(), longitude.toDouble())

                val data = HashMap<String, Any>()

                data["boatName"] = boatName
                data["captainName"] = captainName
                data["localization"] = localization
                data["port"] = harbour
                boats.add(data)

                Toast.makeText(this, "Bateau ajouté avec succès", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Coordonnées impossibles", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Merci de remplir tous les champs", Toast.LENGTH_SHORT).show()
        }
    }

}
