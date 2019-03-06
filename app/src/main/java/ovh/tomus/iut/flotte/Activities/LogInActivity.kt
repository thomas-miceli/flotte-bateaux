package ovh.tomus.iut.flotte.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*
import ovh.tomus.iut.flotte.R

class LogInActivity : AppCompatActivity()  {

    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun signup(view: View) {
        val game: Intent
        when (view.getId()) {
            R.id.signupButton -> {
                game = Intent(this, SignUpActivity::class.java)
                startActivity(game)
            }
        }
    }

    fun connect(view: View) {
        val email = inputMail.text.toString()
        val mdp = inputMdp.text.toString()
        firebaseAuth.signInWithEmailAndPassword(email,mdp).addOnCompleteListener{task ->
            if(task.isSuccessful){
                Toast.makeText(applicationContext,"Connexion réussie", Toast.LENGTH_LONG).show()
                val page = Intent(this, FirstActivity::class.java)
                startActivity(page)
            }
            else {
                Toast.makeText(applicationContext,"Echec de connexion", Toast.LENGTH_LONG).show()
            }
        }
    }
}