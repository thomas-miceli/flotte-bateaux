package ovh.tomus.iut.flotte.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.activity_main.*
import ovh.tomus.iut.flotte.Models.User
import ovh.tomus.iut.flotte.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        crash.setOnClickListener {
            Crashlytics.getInstance().crash() // Force a crash
        }

        val user = intent.getSerializableExtra("USER") as User

        textView.text = user.pseudo
    }

    fun click(view: View) {
        lateinit var intent : Intent
        when (view.getId()) {
            R.id.btnList -> {
                intent = Intent(this, ListActivity::class.java)
            }
            R.id.btnAdd -> {
                intent = Intent(this, AddBoatActivity::class.java)
            }
        }
        startActivity(intent)


    }
}
