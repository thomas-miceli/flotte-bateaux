package ovh.tomus.iut.flotte.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_boat.*

import ovh.tomus.iut.flotte.Models.*
import ovh.tomus.iut.flotte.R

class BoatActivity : AppCompatActivity() {

    private lateinit var boat : Containership
    private lateinit var port : Port
    private lateinit var docref : String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boat)

        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        boat = intent.getSerializableExtra("BOAT") as Containership
        port = intent.getSerializableExtra("PORT") as Port
        docref = intent.getStringExtra("DOCREF")

        title = boat.nomBateau

        boatName.text = boat.nomBateau + " (" + boat.idBateau + ")"
        captainName.text = boat.captainName
        coords.text = "[" + boat.latitude.toString() + "° N, " + boat.longitude.toString() + "° E]"
        portText.text = "Port de " + port.name + port.latitude + " " + port.longitude
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

    fun edit(view : View) {
        val page = Intent(this, BoatEditorActivity::class.java)

        page.putExtra("BOAT", boat)
        page.putExtra("DOCREF", docref)

        startActivity(page)
    }

}
