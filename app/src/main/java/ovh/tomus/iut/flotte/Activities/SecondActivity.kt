package ovh.tomus.iut.flotte.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.*
import com.google.firebase.firestore.model.value.IntegerValue
import com.google.firebase.firestore.model.value.ObjectValue
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.android.synthetic.main.activity_signup.*
import ovh.tomus.iut.flotte.Models.Containership
import ovh.tomus.iut.flotte.Models.ContainershipType
import ovh.tomus.iut.flotte.Models.Port
import ovh.tomus.iut.flotte.Models.User
import ovh.tomus.iut.flotte.R
//import android.support.test.orchestrator.junit.BundleJUnitUtils.getResult

class SecondActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()

    val boats: CollectionReference? = null

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
                        textView.text = "${textView.text} \n--------------------\nID: ${boat.id}\nNom: ${boat.data["boatName"]}\nCapitaine: ${boat.data["captainName"]}\nGeo: ${boat.data["localization"]}\nPort: " + e.data!!["name"].toString()
                    }
                }
            }
        }
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
