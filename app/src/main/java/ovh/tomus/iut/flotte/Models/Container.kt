package ovh.tomus.iut.flotte.Models

import java.io.Serializable

class Container (var id:String, var length:Int, var height:Int, var width:Int) : Serializable {

    override fun toString(): String {
        return "L:" + this.length + ";H:" + this.height + ";W:" + this.width
        //return this.id
    }
}
