package ovh.tomus.iut.flotte.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.add_boat.*
import ovh.tomus.iut.flotte.R

class AddBoat : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_boat)
    }

    fun addBoat(view: View) {
        val boats = db.collection("containerShips")
        val boatName : String = boatname.text.toString()
        val captainName : String = captainname.text.toString()
        //val localization : GeoPoint =
        //val harbour : Port =

        val data = HashMap<String, Any>()
        data["boatName"] = boatName
        data["captainName"] = captainName
        //data["localization"] = localization
        //data["port"] = harbour
        boats.add(data)
    }

}
