package ovh.tomus.iut.flotte.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_list.*
import ovh.tomus.iut.flotte.R

class ListConteneurActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val containers = db.collection("containers")

    private lateinit var docref : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listconteneurs)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        docref = intent.getStringExtra("DOCREF")

        addContainers()


    }

    fun addContainers() {
        containers.get().addOnCompleteListener { e ->
            if (e.isSuccessful) {
                val refcontainers = arrayListOf<String>()
                for (container in e.result!!) {
                    if (container.data["containerShip"] == null) {
                        refcontainers.add(container.reference.path)
                    }
                }

                var adapter = ArrayAdapter<String>(this, R.layout.listview_item, refcontainers.toTypedArray())
                listview.adapter = adapter

                listview.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                    val item = parent!!.getItemAtPosition(position)
                    refcontainers.remove(item.toString())

                    adapter = ArrayAdapter<String>(this, R.layout.listview_item, refcontainers.toTypedArray())
                    listview.adapter = adapter

                    val updates = HashMap<String, Any>(); updates["containerShip"] = docref
                    // Suppression
                    db.document(item.toString()).update(updates)
                }


            }
        }
    }

    fun listContainers() {
        containers.get().addOnCompleteListener { e ->
            if (e.isSuccessful) {
                val refcontainers = arrayListOf<String>()
                for (container in e.result!!) {
                    if (container.data["containerShip"] != null) {
                        if ((container.data["containerShip"] as DocumentReference).path == docref) {
                            refcontainers.add(container.reference.path)
                        }
                    }
                }

                var adapter = ArrayAdapter<String>(this, R.layout.listview_item, refcontainers.toTypedArray())
                listview.adapter = adapter

                listview.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                    val item = parent!!.getItemAtPosition(position)
                    refcontainers.remove(item.toString())

                    adapter = ArrayAdapter<String>(this, R.layout.listview_item, refcontainers.toTypedArray())
                    listview.adapter = adapter

                    val updates = HashMap<String, Any>(); updates["containerShip"] = FieldValue.delete()
                    // Suppression
                    db.document(item.toString()).update(updates)
                }


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
}