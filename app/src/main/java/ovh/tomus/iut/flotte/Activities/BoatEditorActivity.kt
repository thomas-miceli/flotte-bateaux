package ovh.tomus.iut.flotte.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_boat_editor.*

import ovh.tomus.iut.flotte.Models.Container
import ovh.tomus.iut.flotte.R

class BoatEditorActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private var harbours = db.collection("harbours")
    private var harbourList = mutableMapOf<String, String>()
    var containers=db.collection("container")
    var containerList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boat_editor)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        getHarbours()
        getContainers()
    }

    fun getHarbours() {
        harbours.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (harbour in task.result!!) {
                    harbourList[harbour.data["name"].toString()] = harbour.id
                }
                val adapter = ArrayAdapter(
                    this, android.R.layout.simple_spinner_item, harbourList.keys.toTypedArray()
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                harbourspinner.adapter = adapter
            }
        }
    }

    fun getContainers() {
        containers.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (container in task.result!!) {
                    containerList.add(container.reference.path)
                }
                val adapter = ArrayAdapter(
                    this, android.R.layout.simple_spinner_item, containerList.toTypedArray()
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                containerspinner.adapter = adapter
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun updateData (view: View){
        println(intent.getStringExtra("DOCREF"))
        val boatAttribute = mutableMapOf<String,Any>()
        val boatName = edit_boatname.text.toString()
        val boatType = edit_boattype.text.toString()
        val boatcontainers = containers.document(containerList.get(containerspinner.selectedItem.toString().toInt()))

        val captainName = edit_captainname.text.toString()
        val containerShipRef = db.document(intent.getStringExtra("DOCREF"))


        if (boatName.isNotEmpty()) boatAttribute["boatName"] = boatName
        if (captainName.isNotEmpty()) boatAttribute["captainName"] = captainName
        //if (boatType.isNotEmpty()) boatAttribute["boatType"] = boatType
        //condition (la taille totale des conteneurs n'excede pas celle du bateau) on ajoute le conteneur !

        boatAttribute["port"] = harbours.document(harbourList.get(harbourspinner.selectedItem).toString())

        for (attribute in boatAttribute)
            containerShipRef.update(attribute.key, attribute.value)

        finish()

    }

}
