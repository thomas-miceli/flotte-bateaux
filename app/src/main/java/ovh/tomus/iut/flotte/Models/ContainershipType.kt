package ovh.tomus.iut.flotte.Models

import java.io.Serializable

class ContainershipType(val id: String, var name: String, var lenght: Int, var height: Int, var width: Int) : Serializable {

    override fun toString(): String {
        return this.name
    }
}