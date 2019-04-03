package ovh.tomus.iut.flotte.Activities

import android.annotation.SuppressLint
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
import android.widget.Toast
import ovh.tomus.iut.flotte.Models.*

class ListActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()

    var harboursArray = ArrayList<Port>()
    var containershipsArray = ArrayList<Containership>()
    var containersArray = ArrayList<Container>()
    var containershipTypes = ArrayList<ContainershipType>()
    var containershipContainers = ArrayList<ContainerShipContainer>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        getHarbours()
        //getContainers()
        //getContainerShipTypes()
        //getContainerShips()
        //getContainerShipContainers()
    }

    fun getHarbours(){
        db.collection("harbours").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (item in task.result!!) {
                    val geopoint : GeoPoint = item["localization"] as GeoPoint
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

    fun getContainers(){
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

    fun getContainerShipTypes(){
        db.collection("containership-type").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (item in task.result!!) {
                    val containertype = ContainershipType(
                        item.reference.path,
                        item["length"].toString().toInt() as Int,
                        item["height"].toString().toInt() as Int,
                        item["width"].toString().toInt() as Int
                    )
                    containershipTypes.add(containertype)
                }
                getContainerShips()
            }
        }
    }

    fun getContainerShips() {
        db.collection("containerShips").get().addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                for (item in task.result!!){
                    val geopoint : GeoPoint = item["localization"] as GeoPoint
                    val containerShip = Containership( // var containers: Collection<Container>
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
                    for (xD in containerShip.containers) println(containerShip.nomBateau + "---" + xD.id)
                }



                val adapter = ArrayAdapter<Containership>(this, R.layout.listview_item, containershipsArray)
                listview.setAdapter(adapter)

                val page = Intent(this, BoatActivity::class.java)

                list_empty.visibility = if (adapter.isEmpty) View.VISIBLE else View.GONE

                listview.onItemClickListener = object : OnItemClickListener {
                    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val item = parent!!.getItemAtPosition(position) as Containership

                        page.putExtra("CONTAINERSHIP", item)
                        startActivity(page)
                    }
                }
            }
        }
    }

    fun getContainerShipContainers() {
        for(boat in containershipsArray){
            for(container in boat.containers){
                for(item in containersArray){
                    if(item.id === container.id) containershipContainers.add(ContainerShipContainer(boat,item))
                }
            }
        }
    }

    fun getContainerShipHarbour(reference: String): Port {
        lateinit var port: Port
        for(item in harboursArray){
            val path = item.id
            if(path == reference) port = item
        }
        return port
    }

    fun getContainerShipType(reference: String): ContainershipType {
        lateinit var type: ContainershipType
        for(item in containershipTypes){
            if(item.id == reference) type = item
        }
        return type
    }

    fun getContainersOfBoat(containersRef: ArrayList<DocumentReference>): ArrayList<Container> {
        val containersOfBoat : ArrayList<Container> = ArrayList()
        for(containerRef in containersRef){
            for(item in containersArray){
                if(item.id == containerRef.path) containersOfBoat.add(item)
            }
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
