package ovh.tomus.iut.flotte.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.content_boat_editor.*
import ovh.tomus.iut.flotte.R

class BoatEditorActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boat_editor)
    }

    fun updateData (view: View){
        val boatName = edit_boatname.text.toString()
        val containerShipRef = db.collection("containerShips").document("JEaTarJj3Yg9aaRJTIct")
        containerShipRef.update("boatName", boatName)
    }


}
