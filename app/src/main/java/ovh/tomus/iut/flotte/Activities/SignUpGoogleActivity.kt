package ovh.tomus.iut.flotte.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signupgoogle.*
import ovh.tomus.iut.flotte.Models.User
import ovh.tomus.iut.flotte.R

class SignUpGoogleActivity : AppCompatActivity() {

    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signupgoogle)

        textViewMail.text = intent.getStringExtra("mail")
    }

    fun submit(view: View) {
        val page = Intent(this, FirstActivity::class.java)
        val user = User(intent.getStringExtra("uid"), inputPseudo.text.toString(), "Aoker", "Ahokaient", intent.getStringExtra("mail"))

        page.putExtra("USER", user)
        Toast.makeText(applicationContext, "Connecté", Toast.LENGTH_LONG).show()

        startActivity(page)
        finish()
    }

}