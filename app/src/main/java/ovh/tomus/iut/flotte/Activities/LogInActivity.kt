package ovh.tomus.iut.flotte.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.core.FirestoreClient
import kotlinx.android.synthetic.main.activity_signup.*
import ovh.tomus.iut.flotte.Models.User
import ovh.tomus.iut.flotte.R

class LogInActivity : AppCompatActivity()  {

    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    val db = FirebaseFirestore.getInstance()

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

                val uid = firebaseAuth.currentUser!!.uid
                var pseudo = ""

                val users = db.collection("users")

                users.get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            if (uid == document.data["id"].toString()) {
                                pseudo = document.data["pseudo"].toString()
                            }
                        }
                    }
                    val user = User(uid, pseudo, "Aoker", "Ahokaient", "Ahrr")

                    page.putExtra("USER", user)

                    Toast.makeText(applicationContext, pseudo, Toast.LENGTH_LONG).show()

                    startActivity(page)
                    finish()

                }


            }
            else {
                Toast.makeText(applicationContext,"Echec de connexion", Toast.LENGTH_LONG).show()
            }
        }
    }
}