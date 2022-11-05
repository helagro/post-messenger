package se.helagro.postmessenger

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView


class PostListAdapter :
    ArrayAdapter<PostItem>, PostItemsListener {

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
        when(postItem.status){
            PostItemStatus.SUCCESS -> statusBtn.setImageResource(com.google.android.material.R.drawable.ic_mtrl_checked_circle)
        }

        return listItem
    }

    override fun update() {
        notifyDataSetChanged()
    }
}