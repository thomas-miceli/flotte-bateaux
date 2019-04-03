package ovh.tomus.iut.flotte.Models

import java.io.Serializable

class Containership (
    var id: String,
    var nomBateau: String,
    var captainName: String, var latitude: Double, var longitude: Double, var port: Port?, var type: ContainershipType, var containers: ArrayList<Container>
) : Serializable{

    override fun toString(): String {
        return this.nomBateau
    }
}

