package ovh.tomus.iut.flotte.Activities

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_second.*
import ovh.tomus.iut.flotte.R

class SecondActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        getContainerShips()
    }

    @SuppressLint("SetTextI18n")
    fun getContainerShips() {
        val boats = db.collection("containerShips")

        boats.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (boat in task.result!!) {
                    db.document((boat.data["port"] as DocumentReference).path).get().addOnSuccessListener {e->
                        showBoat(boat.id,boat.data["boatName"].toString(), boat.data["captainName"].toString(), boat.data["localization"].toString(), e.data!!["name"].toString())
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun showBoat(id: String, name: String, captain: String, localization: String, port: String){
        textView.text = "${textView.text} \n--------------------\nID: $id\nNom: $name\nCapitaine: $captain\nGeo: $localization\nPort: $port"
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
}
