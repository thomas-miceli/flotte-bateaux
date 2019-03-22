package ovh.tomus.iut.flotte.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.Constraints
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_second.*
import ovh.tomus.iut.flotte.R
import android.support.constraint.ConstraintSet
import android.util.Log
import android.view.View.generateViewId
import android.widget.Toast
import ovh.tomus.iut.flotte.Models.*


class SecondActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        val layout = findViewById<ConstraintLayout>(R.id.layout)

        getContainerShips()
    }

    @SuppressLint("SetTextI18n")
    fun getContainerShips() {
        val boats = db.collection("containerShips")

        boats.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (boat in task.result!!) {
                    db.document((boat.data["port"] as DocumentReference).path).get().addOnSuccessListener {e->
                        showBoat(boat.id,boat.data["boatName"].toString(), boat.data["captainName"].toString(), boat.data["localization"] as GeoPoint, e.data!!["name"].toString())
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun showBoat(id: String, name: String, captain: String, localization: GeoPoint, port: String){
        //titleList.text = "${titleList.text} \n--------------------\nID: $id\nNom: $name\nCapitaine: $captain\nGeo: $localization\nPort: $port"
        val button = Button(this)

        button.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)

        button.text = name

        button.id = generateViewId()

        button.setOnClickListener {
            val port =  Port(1, "Xd", 50.0f, 80.0f)
            val containershipType = ContainershipType(1, 50, 410, 60)
            val collection : Collection<Container> = listOf()
            val containership = Containership(10, name, captain, localization.latitude, localization.longitude, port, containershipType, collection)

            val page = Intent(this, BoatActivity::class.java)

            page.putExtra("BOAT", containership)
            page.putExtra("PORT", port)

            startActivity(page)

        }

        layout.addView(button)
        val set = ConstraintSet()
        set.clone(layout)
        set.connect(button.id, ConstraintSet.TOP, button.id+1, ConstraintSet.BOTTOM, 25)
        set.applyTo(layout)
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
