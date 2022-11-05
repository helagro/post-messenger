package se.helagro.postmessenger

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

class PostListAdapter(context: Context, postItems: ArrayList<PostItem>) :
    ArrayAdapter<PostItem>(context, android.R.layout.simple_list_item_1, postItems), PostItemsListener {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return super.getView(position, convertView, parent)
    }

    override fun update() {
        notifyDataSetChanged()
    }
}