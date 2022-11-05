package se.helagro.postmessenger

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

class PostLogListAdapter(context: Context, postLogItems: ArrayList<PostLogItem>) :
    ArrayAdapter<PostLogItem>(context, android.R.layout.simple_list_item_1, postLogItems) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return super.getView(position, convertView, parent)
    }
}