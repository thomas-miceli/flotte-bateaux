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
import ovh.tomus.iut.flotte.Models.Containership
import ovh.tomus.iut.flotte.Models.ContainershipType
import ovh.tomus.iut.flotte.Models.Port


class AddBoatActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    var boats = db.collection("containerShips")
    var harbours = db.collection("harbours")
    var types = db.collection("containership-type")
    var harbourList = arrayListOf<Port>()
    var typeList = arrayListOf<ContainershipType>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addboat)

        getHarbours()
        getTypes()
    }

    fun getHarbours() {
        harbours.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (harbour in task.result!!) {
                    val geopoint = harbour.data["localization"] as GeoPoint
                    harbourList.add(Port(harbour.reference.path, harbour.data["name"].toString(), geopoint.latitude, geopoint.longitude))
                }
                val adapter = ArrayAdapter(this, R.layout.spinner_item, harbourList.toTypedArray())
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                harbourspinner.adapter = adapter
            }
        }
    }

    fun getTypes() {
        types.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (type in task.result!!) {
                    typeList.add(ContainershipType(type.reference.path, type.data["name"].toString(), type.data["length"].toString().toInt(), type.data["height"].toString().toInt(), type.data["width"].toString().toInt()))
                }
                val adapter = ArrayAdapter(this, R.layout.spinner_item, typeList.toTypedArray())
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                typespinner.adapter = adapter
            }
        }
    }

    fun addBoat(view: View) {

        val boatName: String = boatname.text.toString()
        val captainName: String = captainname.text.toString()
        val latitude : String = latitudeInput.text.toString()
        val longitude : String = longitudeInput.text.toString()

        if (boatName.isNotBlank() && captainName.isNotBlank() && latitudeInput.text.isNotBlank() && longitudeInput.text.isNotBlank()) {

            if (latitude.toDouble() >= -90 && latitude.toDouble() <= 90 && longitude.toDouble() >= -180 && longitude.toDouble() <= 180) {
                val containerShip = Containership("", boatName, captainName, latitude.toDouble(), longitude.toDouble(), harbourspinner.selectedItem as Port, typespinner.selectedItem as ContainershipType, arrayListOf())
                boats.add(containerShip)

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
