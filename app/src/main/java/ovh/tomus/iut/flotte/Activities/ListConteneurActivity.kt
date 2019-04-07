package ovh.tomus.iut.flotte.Activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_listconteneurs.*
import ovh.tomus.iut.flotte.Models.Container
import ovh.tomus.iut.flotte.Models.Containership
import ovh.tomus.iut.flotte.R
import android.content.Intent
import android.app.Activity
import android.widget.Toast


class ListConteneurActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    private lateinit var containership: Containership
    private lateinit var containersList: ArrayList<Container>

    // Taille que prend la somme des conteneurs sur le bateau
    private var usedLength = 0
    private var usedHeight = 0
    private var usedWidth = 0

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

        // Calcul de la taille pour les conteneurs déjà présents
        for (container in containership.containersList) {
            usedLength += container.length
            usedHeight += container.height
            usedWidth += container.width
        }

        setCapacityLabels()
    }

    @SuppressLint("SetTextI18n")
    fun setCapacityLabels(){
        length.text = usedLength.toString() + "/" + containership.boatType.lenght.toString()
        height.text = usedHeight.toString() + "/" + containership.boatType.height.toString()
        width.text = usedWidth.toString() + "/" + containership.boatType.width.toString()
    }

    fun addContainers() {
        title = "Conteneurs disponibles"

        var adapter = ArrayAdapter<Container>(this, R.layout.listview_item_add, containersList.toTypedArray())
        listview_container.adapter = adapter
        list_empty_container.visibility = if (adapter.isEmpty) View.VISIBLE else View.GONE
        listview_container.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val container = parent!!.getItemAtPosition(position) as Container

            if (containership.boatType.lenght >= usedLength + container.length &&
                containership.boatType.height >= usedHeight + container.height &&
                containership.boatType.width >= usedWidth + container.width) {

                usedLength += container.length
                usedHeight += container.height
                usedWidth += container.width

                setCapacityLabels()

                containership.containersList.add(container)
                containersList.remove(container)

                adapter = ArrayAdapter<Container>(this, R.layout.listview_item_add, containersList.toTypedArray())
                listview_container.adapter = adapter
                list_empty_container.visibility = if (adapter.isEmpty) View.VISIBLE else View.GONE

                db.document(containership.id).set(containership)
            } else {
                Toast.makeText(this, "Le conteneur ne rentre pas sur le bateau", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun listContainers() {
        title = "Conteneurs du bateau"

        var adapter = ArrayAdapter<Container>(this, R.layout.listview_item_delete, containership.containersList.toTypedArray())
        listview_container.adapter = adapter
        list_empty_container.visibility = if (adapter.isEmpty) View.VISIBLE else View.GONE
        listview_container.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val container = parent!!.getItemAtPosition(position) as Container

            containership.containersList.remove(container)
            containersList.add(container)

            usedLength -= container.length
            usedHeight -= container.height
            usedWidth -= container.width

            setCapacityLabels()

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
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
