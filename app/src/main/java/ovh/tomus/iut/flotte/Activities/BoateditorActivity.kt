package ovh.tomus.iut.flotte.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import ovh.tomus.iut.flotte.R

import kotlinx.android.synthetic.main.activity_boateditor.*
import kotlinx.android.synthetic.main.content_boateditor.*

class BoateditorActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    //val boatName = findViewById<View>(R.id.editText_boatName)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boateditor)
        setSupportActionBar(toolbar)
        }


    fun updateData (view: View){
        val boatName = editText_boatName.text.toString()
        val containerShipRef = db.collection("containerShips").document("JEaTarJj3Yg9aaRJTIct")
        containerShipRef.update("boatName", boatName)
    }


}
