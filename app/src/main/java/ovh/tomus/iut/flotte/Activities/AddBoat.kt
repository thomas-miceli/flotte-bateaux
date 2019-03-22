package ovh.tomus.iut.flotte.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.add_boat.*
import ovh.tomus.iut.flotte.R
import android.widget.ArrayAdapter
import android.widget.Toast


class AddBoat : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    var boats = db.collection("containerShips")
    var harbours = db.collection("harbours")
    var harbourList = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_boat)

        getHarbours()
    }

    fun getHarbours() {
        harbours.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (harbour in task.result!!) {
                    harbourList[harbour.data["name"].toString()] = harbour.id
                    Toast.makeText(applicationContext,harbour.data["name"].toString(), Toast.LENGTH_SHORT).show()
                }
                val adapter = ArrayAdapter(
                    this, android.R.layout.simple_spinner_item, harbourList.keys.toTypedArray()
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                harbourspinner.adapter = adapter
            }
        }
    }

    fun addBoat(view: View) {
        var boatName : String = boatname.text.toString()
        var captainName : String = captainname.text.toString()
        var localization = GeoPoint(editText7.text.toString().toDouble(), editText8.text.toString().toDouble())
        var harbour : DocumentReference = harbourList.get(harbourspinner.selectedItem) as DocumentReference
        var data = HashMap<String, Any>()

        data["boatName"] = boatName
        data["captainName"] = captainName
        data["localization"] = localization
        data["port"] = harbour
        boats.add(data)

    }

}
