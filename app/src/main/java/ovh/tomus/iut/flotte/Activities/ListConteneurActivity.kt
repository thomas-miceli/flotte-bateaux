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
import android.content.Intent
import android.app.Activity


class ListConteneurActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    private lateinit var containership: Containership
    private lateinit var containersList: ArrayList<Container>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listconteneurs)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        containership = intent.getSerializableExtra("CONTAINERSHIP") as Containership
        containersList = intent.getSerializableExtra("CONTAINERS") as ArrayList<Container>

        when (intent.getStringExtra("MODE")) {
            "add" -> addContainers()
            "list" -> listContainers()
        }
    }

    fun addContainers() {
        title = "Conteneurs disponibles"

        var adapter = ArrayAdapter<Container>(this, R.layout.listview_item_add, containersList.toTypedArray())
        listview_container.adapter = adapter
        list_empty_container.visibility = if (adapter.isEmpty) View.VISIBLE else View.GONE
        listview_container.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val containerObj = parent!!.getItemAtPosition(position) as Container

            containership.containersList.add(containerObj)
            containersList.remove(containerObj)

            adapter = ArrayAdapter<Container>(this, R.layout.listview_item_add, containersList.toTypedArray())
            listview_container.adapter = adapter
            list_empty_container.visibility = if (adapter.isEmpty) View.VISIBLE else View.GONE

            db.document(containership.id).set(containership)
        }




    }

    fun listContainers() {
        title = "Conteneurs du bateau"

        var adapter = ArrayAdapter<Container>(this, R.layout.listview_item_delete, containership.containersList.toTypedArray())
        listview_container.adapter = adapter
        list_empty_container.visibility = if (adapter.isEmpty) View.VISIBLE else View.GONE
        listview_container.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val containerObj = parent!!.getItemAtPosition(position) as Container

            containership.containersList.remove(containerObj)
            containersList.add(containerObj)

            adapter = ArrayAdapter<Container>(this, R.layout.listview_item_add, containership.containersList.toTypedArray())
            listview_container.adapter = adapter
            list_empty_container.visibility = if (adapter.isEmpty) View.VISIBLE else View.GONE

            db.document(containership.id).set(containership)
        }
    }

    override fun onBackPressed() {
        val i = Intent()
        println("DANS LE BATEAU :" + containership.containersList)
        println("DISPOS : " + containersList)

        i.putExtra("CONTAINERSHIP", containership)
        i.putExtra("CONTAINERS", containersList)
        setResult(Activity.RESULT_OK, i)
        finish()
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
