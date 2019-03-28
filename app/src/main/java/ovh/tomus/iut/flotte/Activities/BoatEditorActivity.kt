package ovh.tomus.iut.flotte.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_boat.*
import kotlinx.android.synthetic.main.activity_boat_editor.*
import ovh.tomus.iut.flotte.R

class BoatEditorActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boat_editor)
    }

    fun updateData (view: View){
        var boatAttribute = mutableMapOf<String,String>()
        val boatName = edit_boatname.text.toString()
        val boatType = edit_boattype.text.toString()
        val startHarbor = edit_harborstart.text.toString()
        val captainName = edit_captainname.text.toString()
        val containerShipRef = db.collection("containerShips").document("RvZqocZSKqBNFRNlV5wb")
        if (boatName.isNotEmpty()) boatAttribute["boatName"] = boatName
        if (captainName.isNotEmpty()) boatAttribute["captainName"] = captainName
        if (boatType.isNotEmpty()) boatAttribute["boatType"] = boatType
        if (startHarbor.isNotEmpty()) boatAttribute["startHarbor"] = startHarbor
        println(boatAttribute)
        for (attribute in boatAttribute)
        {
            containerShipRef.update(attribute.key, attribute.value)
            println("passedmaj")
        }
    }

}