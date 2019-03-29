package ovh.tomus.iut.flotte.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ovh.tomus.iut.flotte.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun click(view: View) {
        lateinit var game : Intent
        when (view.getId()) {
            R.id.btnOne -> {
                game = Intent(this, LogInActivity::class.java)
            }
            R.id.btnTwo -> {
                game = Intent(this, ListActivity::class.java)
            }
            R.id.btnThree -> {
                game = Intent(this, AddBoatActivity::class.java)
            }
            R.id.btnFour -> {
                game = Intent(this, BoatsMapActivity::class.java)
            }
        }
        startActivity(game)


    }
}
