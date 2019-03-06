package ovh.tomus.iut.flotte.Models

import java.io.Serializable

data class User (var id : String, var pseudo : String, var name: String, var firstName : String, var email : String) : Serializable {

}