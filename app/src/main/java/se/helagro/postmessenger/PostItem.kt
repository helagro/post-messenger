package se.helagro.postmessenger

class PostItem(val msg: String) {
    var status = PostItemStatus.LOADING

    override fun toString(): String {
        return msg
    }
}