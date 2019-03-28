package ovh.tomus.iut.flotte.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_boat.*
import kotlinx.android.synthetic.main.activity_boat_editor.*
import ovh.tomus.iut.flotte.R

class BoatEditorActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private var harbours = db.collection("harbours")
    private var harbourList = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boat_editor)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        getHarbours()
    }

    fun getHarbours() {
        harbours.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (harbour in task.result!!) {
                    harbourList[harbour.data["name"].toString()] = harbour.id
                }
                val adapter = ArrayAdapter(
                    this, android.R.layout.simple_spinner_item, harbourList.keys.toTypedArray()
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                harbourspinner.adapter = adapter
            }
        }
    }

    fun updateData (view: View){
        val boatAttribute = mutableMapOf<String,Any>()
        val boatName = edit_boatname.text.toString()
        val boatType = edit_boattype.text.toString()

        val captainName = edit_captainname.text.toString()
        val containerShipRef = db.collection("containerShips").document("DoIRXz31Lnugf63og9N3")

        if (boatName.isNotEmpty()) boatAttribute["boatName"] = boatName
        if (captainName.isNotEmpty()) boatAttribute["captainName"] = captainName
        //if (boatType.isNotEmpty()) boatAttribute["boatType"] = boatType

        boatAttribute["port"] = harbours.document(harbourList.get(harbourspinner.selectedItem).toString())

        for (attribute in boatAttribute)
            containerShipRef.update(attribute.key, attribute.value)

    }

}
