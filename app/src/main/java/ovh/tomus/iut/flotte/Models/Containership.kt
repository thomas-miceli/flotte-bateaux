package ovh.tomus.iut.flotte.Models

import java.io.Serializable

class Containership (var idBateau: Int,var nomBateau: String,var captainName: String, var latitude: Double, var longitude: Double, var port: Port, var type: ContainershipType, var containers: Collection<Container> ) : Serializable{

}

