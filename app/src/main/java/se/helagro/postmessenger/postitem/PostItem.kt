package se.helagro.postmessenger.postitem

class PostItem(val msg: String) {
    private val INITIAL_STATUS = PostItemStatus.LOADING

    var status = INITIAL_STATUS

    override fun toString(): String {
        return msg
    }
}