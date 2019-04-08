package ovh.tomus.iut.flotte.Models

import java.io.Serializable

class Port(var id: String, var name: String, var latitude: Double, var longitude: Double) : Serializable {

    override fun toString(): String {
        return this.name
    }
}
