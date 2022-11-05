package se.helagro.postmessenger

import android.content.Context
import android.graphics.Color
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView


class PostListAdapter :
    ArrayAdapter<PostItem>, PostItemsListener, PostHandlerListener {

    val inflater = LayoutInflater.from(context)
    val postItems: PostItems

    constructor(context: Context, postItems: PostItems) :
            super(context, -1, postItems) {
        this.postItems = postItems
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val postItem = postItems[position]
        val listItem = inflater.inflate(R.layout.listitem_post, null)
        val textView: TextView = listItem.findViewById(R.id.postLogListText)
        textView.text = postItem.msg

        val statusBtn: ImageButton = listItem.findViewById(R.id.postLogListImgBtn)
        statusBtn.setOnClickListener{
            val postHandler = PostHandler(PostHandler.getEndpoint()!!)
            postHandler.sendMessage(postItem, this)
        }
        when(postItem.status){
            PostItemStatus.SUCCESS -> {
                statusBtn.setImageResource(com.google.android.material.R.drawable.ic_mtrl_checked_circle)
                statusBtn.clearColorFilter()
                statusBtn.isEnabled = false
            }
            PostItemStatus.LOADING -> {
                statusBtn.setImageResource(android.R.color.transparent)
                statusBtn.isEnabled = false
            }
            PostItemStatus.FAILURE -> {
                statusBtn.setImageResource(android.R.drawable.stat_notify_sync_noanim)
                statusBtn.setColorFilter(Color.argb(255, 255, 0, 0))
                statusBtn.isEnabled = true
            }
        }

        return listItem
    }

    override fun update() {
        if(Looper.getMainLooper() != Looper.myLooper()){
            return
        }
        notifyDataSetChanged()
    }

    override fun onUpdate(code: Int) {
        postItems.alertListeners()
    }
}