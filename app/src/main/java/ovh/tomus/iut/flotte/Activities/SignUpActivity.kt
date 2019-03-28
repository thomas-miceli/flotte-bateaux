package ovh.tomus.iut.flotte.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_signup.*
import ovh.tomus.iut.flotte.R

class SignUpActivity : AppCompatActivity() {

    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
    }

    fun submit(view: View) {
        val email = inputMail.text.toString()
        val mdp = inputMdp.text.toString()
        val pseudo = inputPseudo.text.toString()
        if (email.isNotBlank() && mdp.isNotBlank() && pseudo.isNotBlank()) {
            firebaseAuth.createUserWithEmailAndPassword(email, mdp).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val data = HashMap<String, Any>()
                    data["id"] = firebaseAuth.currentUser!!.uid
                    data["pseudo"] = pseudo
                    db.collection("users").add(data)
                    Toast.makeText(applicationContext, "Utilisateur créé avec succès", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Erreur de création de l'utilisateur", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}
