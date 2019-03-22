package ovh.tomus.iut.flotte.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_boat.*
import kotlinx.android.synthetic.main.activity_first.*
import ovh.tomus.iut.flotte.Models.*
import ovh.tomus.iut.flotte.R

class BoatActivity : AppCompatActivity() {
    val port =  Port(1, "Xd", 50.0f, 80.0f)
    val containershipType = ContainershipType(1, 50, 410, 60)
    val collection : Collection<Container> = listOf()
    val containership = Containership(1, "xD", "xD2", 50.0f, 40.0f, port, containershipType, collection)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boat)

        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        boatName.text = containership.nomBateau + " (" + containership.idBateau + ")"
        captainName.text = containership.captainName
        coords.text = "[" + containership.latitude.toString() + "° N, " + containership.longitude.toString() + "° E]"
        portText.text = port.toString()

    }

}