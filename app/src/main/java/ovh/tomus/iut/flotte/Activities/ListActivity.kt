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
        getContainers()
        getContainerShipTypes()
        getContainerShips()
        getContainerShipContainers()
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
            }
        }
    }

    fun getContainers(){
        db.collection("containers").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (item in task.result!!) {
                    val container = Container(
                        item.reference.path,
                        item["length"] as Int,
                        item["height"] as Int,
                        item["width"] as Int
                    )
                    containersArray.add(container)
                }
            }
        }
    }

    fun getContainerShipTypes(){
        db.collection("containership-type").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (item in task.result!!) {
                    val containertype = ContainershipType(
                        item.reference.path,
                        item["length"] as Int,
                        item["height"] as Int,
                        item["width"] as Int
                    )
                    containershipTypes.add(containertype)
                }
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
                        item["name"].toString(),
                        item["captainName"].toString(),
                        geopoint.latitude,
                        geopoint.longitude,
                        getContainerShipHarbour(item.reference.path),
                        getContainerShipType(item.reference.path)!!,
                        getContainersOfBoat(item["containers"] as ArrayList<String>)
                    )
                    containershipsArray.add(containerShip)
                }

                var boatnames = ArrayList<String>()
                for(containerShip in containershipsArray) boatnames.add(containerShip.nomBateau)

                val adapter = ArrayAdapter<String>(this, R.layout.listview_item, boatnames)
                listview.setAdapter(adapter)

                val page = Intent(this, BoatActivity::class.java)

                list_empty.visibility = if (adapter.isEmpty) View.VISIBLE else View.GONE

                listview.onItemClickListener = object : OnItemClickListener {
                    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val item = parent!!.getItemAtPosition(position)
                        page.putExtra("CONTAINERSHIP", containershipsArray.indexOf(item))
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
        var port: Port? = null
        for(item in harboursArray){
            if(item.id === reference) port = item
        }
        return port!!
    }

    fun getContainerShipType(reference: String): ContainershipType {
        var type: ContainershipType? = null
        for(item in containershipTypes){
            if(item.id === reference) type = item
        }
        return type!!
    }

    fun getContainersOfBoat(containersRef: ArrayList<String>): ArrayList<Container> {
        var containersOfBoat = ArrayList<Container>()
        for(containerRef in containersRef){
            for(item in containersArray){
                if(item.id === containerRef) containersOfBoat.add(item)
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
