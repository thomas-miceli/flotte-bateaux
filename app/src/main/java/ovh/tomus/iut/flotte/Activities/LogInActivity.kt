package ovh.tomus.iut.flotte.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ovh.tomus.iut.flotte.Models.User
import ovh.tomus.iut.flotte.R
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*


class LogInActivity : AppCompatActivity()  {

    val RC_SIGN_IN = 101

    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    val db = FirebaseFirestore.getInstance()

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("510201343666-r8ml27a28nsrenpqu48njs5kfc3qsbe9.apps.googleusercontent.com")
        .requestEmail()
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //googleButton.setOnClickListener {}
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
        if (email.isNotBlank() && mdp.isNotBlank()) {
            firebaseAuth.signInWithEmailAndPassword(email, mdp).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Connexion réussie", Toast.LENGTH_LONG).show()

                    val uid = firebaseAuth.currentUser!!.uid
                    val users = db.collection("users")

                    users.get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (document in task.result!!) {
                                if (uid == document.data["id"].toString()) {
                                    val pseudo = document.data["pseudo"].toString()
                                    whenLoggedIn(uid, pseudo, firebaseAuth.currentUser!!.email!!)
                                }
                            }
                        }

                    }


                } else {
                    Toast.makeText(applicationContext, "Echec de connexion", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun signWithGoogle(view: View) {
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                Log.d("TAG", account.toString())

                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e)
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val users = db.collection("users")

                    users.whereEqualTo("id", firebaseAuth.currentUser!!.uid).get().addOnSuccessListener { task ->
                        if (!task.isEmpty) {
                            // Si l'id est renseignée dans la BD
                            val pseudo = task.documents.get(0).data!!["pseudo"].toString()
                            whenLoggedIn(firebaseAuth.currentUser!!.uid, pseudo, firebaseAuth.currentUser!!.email!!)
                        } else {
                            finalizeGoogle(firebaseAuth.currentUser!!.uid, firebaseAuth.currentUser!!.email!!)
                        }
                    }
                } else {
                    Toast.makeText(applicationContext, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun whenLoggedIn(uid: String, pseudo: String, mail: String) {
        val page = Intent(this, FirstActivity::class.java)
        val user = User(uid, pseudo, "Aoker", "Ahokaient", mail)

        page.putExtra("USER", user)
        Toast.makeText(applicationContext, "Connecté", Toast.LENGTH_LONG).show()

        startActivity(page)
        finish()
    }

    private fun finalizeGoogle(uid: String, mail: String) {
        val page = Intent(this, SignUpGoogleActivity::class.java)
        page.putExtra("uid", uid)
        page.putExtra("mail", mail)

        startActivity(page)
        finish()
    }


}
