package ovh.tomus.iut.flotte.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import ovh.tomus.iut.flotte.R

class LoginActivity : AppCompatActivity() {

    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }

    fun submit(view: View) {
        val email = inputMail.text.toString()
        val mdp = inputMdp.text.toString()
        firebaseAuth.createUserWithEmailAndPassword(email,mdp).addOnCompleteListener{task ->
            if(task.isSuccessful){
                Toast.makeText(applicationContext,"Utilisateur créé avec succès",Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(applicationContext,"Erreur de création de l'utilisateur",Toast.LENGTH_LONG).show()
            }
        }
    }

}