package ovh.tomus.iut.flotte.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.add_boat.*
import ovh.tomus.iut.flotte.R
import android.widget.ArrayAdapter
import android.widget.Spinner


class AddBoat : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    val boats = db.collection("containerShips")
    val harbours = db.collection("harbours")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_boat)
        getHarbours()
    }

    fun getHarbours() {
        val harbourList = mutableMapOf<String, String>()
        harbours.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (harbour in task.result!!) {
                    harbourList[harbour.data["name"].toString()] = harbour.id
                }
                val spinner = findViewById<Spinner>(R.id.harbourlist)
                val adapter = ArrayAdapter(
                    this, android.R.layout.simple_spinner_item, harbourList.keys.toTypedArray()
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
        }
        val spinner = findViewById<Spinner>(R.id.harbourlist)
        spinner.selectedItem
    }

    fun addBoat(view: View) {
        val spinner = findViewById<Spinner>(R.id.harbourlist)

        val boatName : String = boatname.text.toString()
        val captainName : String = captainname.text.toString()
        val localization = GeoPoint(latitude.text.toString().toDouble(), longitude.text.toString().toDouble())
        val harbour : String = spinner.selectedItem.toString()
        val data = HashMap<String, Any>()
        data["boatName"] = boatName
        data["captainName"] = captainName
        data["localization"] = localization
        data["port"] = harbour

        addboat.setOnClickListener {
            boats.add(data)
        }

    }

}
