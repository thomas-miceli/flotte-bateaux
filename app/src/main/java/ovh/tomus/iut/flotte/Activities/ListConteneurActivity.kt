package ovh.tomus.iut.flotte.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.activity_listconteneurs.*
import ovh.tomus.iut.flotte.Models.Container
import ovh.tomus.iut.flotte.Models.Containership
import ovh.tomus.iut.flotte.R

class ListConteneurActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val containers = db.collection("containers")

    private lateinit var containership: Containership

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listconteneurs)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        containership = intent.getSerializableExtra("CONTAINERSHIP") as Containership

        when (intent.getStringExtra("MODE")) {
            "add" -> addContainers()
            "list" -> listContainers()
        }
    }

    fun addContainers() {
        title = "Conteneurs disponibles"
        containers.get().addOnCompleteListener { e ->
            if (e.isSuccessful) {
                val refcontainers = arrayListOf<Container>()
                for (container in e.result!!) {
                    if (container.data["containerShip"] == null) {
                        val containerObj = Container(container.reference.path, container.data["length"] as Int, container.data["height"] as Int, container.data["width"] as Int)

                        refcontainers.add(containerObj)
                    }
                }

                var adapter = ArrayAdapter<Container>(this, R.layout.listview_item_add, refcontainers.toTypedArray())
                listview_container.adapter = adapter

                list_empty_container.visibility = if (adapter.isEmpty) View.VISIBLE else View.GONE

                listview_container.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                    val containerObj = parent!!.getItemAtPosition(position) as Container

                    // Ajout
                    containership.containers.add(containerObj)


                    refcontainers.remove(containerObj)

                    adapter = ArrayAdapter<Container>(this, R.layout.listview_item_add, refcontainers.toTypedArray())
                    listview_container.adapter = adapter

                    list_empty_container.visibility = if (adapter.isEmpty) View.VISIBLE else View.GONE


                    db.document(containership.id).set(containership)
                }


            }
        }
    }

    fun listContainers() {
        title = "Conteneurs du bateau"
        containers.get().addOnCompleteListener { e ->
            if (e.isSuccessful) {
                val refcontainers = arrayListOf<Container>()
                for (container in e.result!!) {
                    if (container.data["containerShip"] != null) {
                        if ((container.data["containerShip"] as DocumentReference).path == containership.id) {
                            val containerObj = Container(container.reference.path, container.data["length"] as Int, container.data["height"] as Int, container.data["width"] as Int)

                            refcontainers.add(containerObj)
                        }
                    }
                }

                var adapter = ArrayAdapter<Container>(this, R.layout.listview_item_delete, refcontainers.toTypedArray())
                listview_container.adapter = adapter

                list_empty_container.visibility = if (adapter.isEmpty) View.VISIBLE else View.GONE

                listview_container.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                    val containerObj = parent!!.getItemAtPosition(position) as Container

                    // Suppression
                    containership.containers.remove(containerObj)


                    refcontainers.remove(containerObj)

                    adapter = ArrayAdapter<Container>(this, R.layout.listview_item_add, refcontainers.toTypedArray())
                    listview_container.adapter = adapter

                    list_empty_container.visibility = if (adapter.isEmpty) View.VISIBLE else View.GONE


                    db.document(containership.id).set(containership)
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