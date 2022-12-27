package se.helagro.postmessenger

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.R.drawable.ic_mtrl_checked_circle
import se.helagro.postmessenger.network.NetworkHandler
import se.helagro.postmessenger.network.NetworkHandlerListener
import se.helagro.postmessenger.posthistory.PostHistory
import se.helagro.postmessenger.posthistory.PostHistoryListener
import se.helagro.postmessenger.postitem.PostItem
import se.helagro.postmessenger.postitem.PostItemStatus


class PostHistoryListAdapter(val activity: Activity, private val postHistory: PostHistory) :
    ArrayAdapter<PostItem>(activity, -1, postHistory), PostHistoryListener, NetworkHandlerListener {

    val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val postItem = postHistory[position]
        val viewHolder: PostHistoryViewHolder
        val listItem: View

        if(convertView == null){
            viewHolder = PostHistoryViewHolder()
            listItem = inflater.inflate(R.layout.listitem_post, parent)

            viewHolder.textView = listItem.findViewById(R.id.postLogListText)
            viewHolder.statusBtn = listItem.findViewById(R.id.postLogListImgBtn)

            viewHolder.statusBtn.setOnClickListener{
                val networkHandler = NetworkHandler(NetworkHandler.getEndpoint()!!)
                networkHandler.sendMessage(postItem, this)
            }

            listItem.tag = viewHolder
        } else {
            viewHolder = convertView.tag as PostHistoryViewHolder
            listItem = convertView
        }

        viewHolder.textView.text = postItem.msg
        setStatusBtnStatus(postItem, viewHolder.statusBtn)

        return listItem
    }

    private fun setStatusBtnStatus(postItem: PostItem, statusBtn: ImageButton){
        when(postItem.status){
            PostItemStatus.SUCCESS -> {
                statusBtn.setImageResource(ic_mtrl_checked_circle)
                statusBtn.clearColorFilter()
                statusBtn.isEnabled = false
            }
            PostItemStatus.LOADING -> {
                statusBtn.setImageResource(android.R.color.transparent)
                statusBtn.clearColorFilter()
                statusBtn.isEnabled = false
            }
            PostItemStatus.FAILURE -> {
                statusBtn.setImageResource(android.R.drawable.stat_notify_sync_noanim)
                statusBtn.setColorFilter(Color.argb(255, 255, 0, 0))
                statusBtn.isEnabled = true
            }
        }
    }

    override fun onPostHistoryUpdate() {
        activity.runOnUiThread {
            notifyDataSetChanged()
        }
    }

    override fun onPostItemUpdate(code: Int) {
        postHistory.alertListeners()
    }
}