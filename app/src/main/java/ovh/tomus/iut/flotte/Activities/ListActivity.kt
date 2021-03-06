package ovh.tomus.iut.flotte.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_list.*
import ovh.tomus.iut.flotte.R
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.view.View
import ovh.tomus.iut.flotte.Models.*


class ListActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()

    var harboursArray = ArrayList<Port>()
    var containershipsArray = ArrayList<Containership>()
    var containersArray = ArrayList<Container>()
    var containershipTypes = ArrayList<ContainershipType>()

    var resume: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onPause() {
        super.onPause()
        resume = true
    }

    override fun onResume() {
        super.onResume()
        if (resume) {
            recreate()
            resume = false
        } else {
            list_empty.visibility = View.VISIBLE
            list_empty.text = "Importation des bateaux..."
            getHarbours()
        }
    }

    fun getHarbours() {
        db.collection("harbours").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (item in task.result!!) {
                    val geopoint: GeoPoint = item["localization"] as GeoPoint
                    val port = Port(
                        item.reference.path,
                        item["name"].toString(),
                        geopoint.latitude,
                        geopoint.longitude
                    )
                    harboursArray.add(port)
                }
                getContainers()
            }
        }
    }

    fun getContainers() {
        db.collection("containers").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (item in task.result!!) {
                    val container = Container(
                        item.reference.path,
                        item["length"].toString().toInt() as Int,
                        item["height"].toString().toInt() as Int,
                        item["width"].toString().toInt() as Int
                    )
                    containersArray.add(container)
                }
                getContainerShipTypes()
            }
        }
    }

    fun getContainerShipTypes() {
        db.collection("containership-type").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (item in task.result!!) {
                    val containertype = ContainershipType(
                        item.reference.path,
                        item["name"].toString(),
                        item["length"].toString().toInt(),
                        item["height"].toString().toInt(),
                        item["width"].toString().toInt()
                    )
                    containershipTypes.add(containertype)
                }
                getContainerShips()
            }
        }
    }

    fun getContainerShips() {
        db.collection("containerShips").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (item in task.result!!) {
                    val geopoint: GeoPoint = item["localization"] as GeoPoint
                    val containerShip = Containership(
                        item.reference.path,
                        item["boatName"].toString(),
                        item["captainName"].toString(),
                        geopoint.latitude,
                        geopoint.longitude,
                        getContainerShipHarbour((item["port"] as DocumentReference).path),
                        getContainerShipType((item["boatType"] as DocumentReference).path),
                        getContainersOfBoat(item["containers"] as ArrayList<DocumentReference>)
                    )
                    containershipsArray.add(containerShip)
                }

                list_empty.text = "Aucun bateau"

                val adapter = ArrayAdapter<Containership>(this, R.layout.listview_item, containershipsArray)
                listview.setAdapter(adapter)

                val page = Intent(this, BoatActivity::class.java)

                list_empty.visibility = if (adapter.isEmpty) View.VISIBLE else View.GONE

                listview.onItemClickListener = object : OnItemClickListener {
                    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val item = parent!!.getItemAtPosition(position) as Containership

                        page.putExtra("CONTAINERSHIP", item)
                        page.putExtra("HARBOURS", harboursArray)
                        page.putExtra("CONTAINERSHIPTYPES", containershipTypes)
                        page.putExtra("CONTAINERS", containersArray)
                        startActivityForResult(page, 1)
                    }
                }
            }
        }
    }

    fun getContainerShipHarbour(reference: String): Port {
        lateinit var port: Port
        for (item in harboursArray) {
            val path = item.id
            if (path == reference) port = item
        }
        return port
    }

    fun getContainerShipType(reference: String): ContainershipType {
        lateinit var type: ContainershipType
        for (item in containershipTypes) {
            if (item.id == reference) type = item
        }
        return type
    }

    fun getContainersOfBoat(containersRef: ArrayList<DocumentReference>): ArrayList<Container> {
        val containersOfBoat: ArrayList<Container> = ArrayList()
        for (containerRef in containersRef) {
            val toRemove = ArrayList<Container>()
            for (item in containersArray) {
                if (item.id == containerRef.path) {
                    containersOfBoat.add(item)
                    toRemove.add(item)
                }
            }
            containersArray.removeAll(toRemove)
        }
        return containersOfBoat
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
