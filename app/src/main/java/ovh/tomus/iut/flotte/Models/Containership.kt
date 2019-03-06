package ovh.tomus.iut.flotte.Models

class Containership (var idBateau: Int,var nomBateau: String,var captainName: String, var latitude: Float, var longitude: Float, var port: Port, var type: ContainershipType, var containers: Collection<Container> ){

}

