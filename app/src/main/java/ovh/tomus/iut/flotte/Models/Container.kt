package ovh.tomus.iut.flotte.Models

import java.io.Serializable

class Container (var id:String, var length:Int, var height:Int, var width:Int) : Serializable {

    override fun toString(): String {
        return this.id + "\n(" + this.length + "x" + this.height + "x" + this.width + ")"
        //return this.id
    }
}
