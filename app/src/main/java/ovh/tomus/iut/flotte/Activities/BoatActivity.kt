package ovh.tomus.iut.flotte.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_boat.*
import kotlinx.android.synthetic.main.activity_first.*
import kotlinx.android.synthetic.main.activity_signupgoogle.*
import ovh.tomus.iut.flotte.Models.*
import ovh.tomus.iut.flotte.R

class BoatActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boat)

        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        val boat = intent.getSerializableExtra("BOAT") as Containership
        val port = intent.getSerializableExtra("PORT") as Port

        title = boat.nomBateau

        boatName.text = boat.nomBateau + " (" + boat.idBateau + ")"
        captainName.text = boat.captainName
        coords.text = "[" + boat.latitude.toString() + "° N, " + boat.longitude.toString() + "° E]"
        portText.text = port.toString()

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