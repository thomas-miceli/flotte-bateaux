package ovh.tomus.iut.flotte.Models

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import java.io.Serializable

class Containership (
    @get:Exclude var id: String,
    var boatName: String,
    var captainName: String,
    @get:Exclude var latitude: Double,
    @get:Exclude var longitude: Double,
    @get:Exclude var port: Port,
    @get:Exclude var boatType: ContainershipType,
    @get:Exclude var containersList: ArrayList<Container>
) : Serializable{

    fun getLocalization() : GeoPoint {
        return GeoPoint(this.latitude, this.longitude)
    }

    fun getPort() : DocumentReference {
        return FirebaseFirestore.getInstance().document(this.port.id)
    }

    fun getBoatType() : DocumentReference {
        return FirebaseFirestore.getInstance().document(this.boatType.id)
    }

    fun getContainers() : ArrayList<DocumentReference> {
        val refs = ArrayList<DocumentReference>()
        for (container in this.containersList) {
            refs.add(FirebaseFirestore.getInstance().document(container.id))
        }
        return refs
    }

    override fun toString(): String {
        return this.boatName
    }
}

