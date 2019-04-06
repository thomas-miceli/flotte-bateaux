package ovh.tomus.iut.flotte.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signupgoogle.*
import ovh.tomus.iut.flotte.Models.User
import ovh.tomus.iut.flotte.R

class SignUpGoogleActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signupgoogle)

        textViewMail.text = intent.getStringExtra("mail")
    }

    fun submit(view: View) {
        val users = db.collection("users")
        val uid = intent.getStringExtra("uid")
        val mail = intent.getStringExtra("mail")
        val pseudo = inputPseudo.text.toString()

        val data = HashMap<String, Any>()
        data["id"] = uid
        data["pseudo"] = pseudo
        users.add(data)

        val page = Intent(this, MainActivity::class.java)
        val user = User(uid, pseudo, mail)

        page.putExtra("USER", user)
        Toast.makeText(applicationContext, "Connecté", Toast.LENGTH_LONG).show()

        startActivity(page)
        finish()
    }

}
