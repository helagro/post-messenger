package se.helagro.postmessenger

class PostLogItem {
    val msg: String

    constructor(msg: String){
        this.msg = msg
    }

    override fun toString(): String {
        return msg
    }
}