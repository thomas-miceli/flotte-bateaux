package ovh.tomus.iut.flotte.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.activity_main.*
import ovh.tomus.iut.flotte.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val crashButton = Button(this)
        crashButton.text = "Crash!"
        crashButton.setOnClickListener {
            Crashlytics.getInstance().crash() // Force a crash
        }

        addContentView(crashButton, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT))
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
        }
        startActivity(game)


    }
}
