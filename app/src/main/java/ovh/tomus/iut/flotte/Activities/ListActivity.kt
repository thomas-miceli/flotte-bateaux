package ovh.tomus.iut.flotte.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.MenuItem
import android.widget.Button
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_list.*
import ovh.tomus.iut.flotte.R
import android.support.constraint.ConstraintSet
import android.view.View.generateViewId
import ovh.tomus.iut.flotte.Models.*


class ListActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        val layout = findViewById<ConstraintLayout>(R.id.layout)

        getContainerShips()
    }

    @SuppressLint("SetTextI18n")
    fun getContainerShips() {
        db.collection("containerShips").get().addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                for (boat in task.result!!) {
                    showBoat(boat.data["boatName"].toString(), boat.data["captainName"].toString(), boat.reference.path)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun showBoat(nomBateau : String, nomCapitaine : String, docref : String){
        val button = Button(this)

        button.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        button.text = nomBateau + " - " + nomCapitaine
        button.id = generateViewId()

        button.setOnClickListener {
            val page = Intent(this, BoatActivity::class.java)

            page.putExtra("DOCREF", docref)

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
