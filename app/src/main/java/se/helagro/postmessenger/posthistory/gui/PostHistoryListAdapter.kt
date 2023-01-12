package se.helagro.postmessenger.posthistory.gui

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import se.helagro.postmessenger.R
import se.helagro.postmessenger.network.NetworkMessenger
import se.helagro.postmessenger.network.NetworkRequestListener
import se.helagro.postmessenger.posthistory.PostHistory
import se.helagro.postmessenger.posthistory.PostHistoryListener
import se.helagro.postmessenger.postitem.PostItem
import se.helagro.postmessenger.postitem.PostItemStatus


class PostHistoryListAdapter(
    private val activity: Activity,
    private val networkHandlerListener: NetworkRequestListener
) :
    ArrayAdapter<PostItem>(activity, -1, PostHistory.getInstance().getList()), PostHistoryListener {

    private val postHistory = PostHistory.getInstance()
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    // ========== POST_HISTORY LISTENER ==========

    fun subscribeToPostHistory(){
        postHistory.addListener(this)
    }

    fun unsubscribeFromPostHistory(){
        postHistory.removeListener(this)
    }


    override fun getView(pos: Int, convertView: View?, parent: ViewGroup): View {
        val postItem = postHistory.get(pos)
        val viewHolder: PostHistoryViewHolder
        val view: View

        if (convertView == null) {
            viewHolder = PostHistoryViewHolder()
            view = inflater.inflate(R.layout.post_history_list_item, null)

            viewHolder.textView = view.findViewById(R.id.postLogListText)
            viewHolder.statusBtn = view.findViewById(R.id.postLogListImgBtn)

            viewHolder.statusBtn.setOnClickListener {
                NetworkMessenger.sendMessage(postItem, networkHandlerListener)
                setStatusBtnStatus(postItem, viewHolder.statusBtn)
            }

            view.tag = viewHolder
        } else {
            viewHolder = convertView.tag as PostHistoryViewHolder
            view = convertView
        }

        viewHolder.textView.text = postItem.msg
        setStatusBtnStatus(postItem, viewHolder.statusBtn)

        return view
    }

    private fun setStatusBtnStatus(postItem: PostItem, statusBtn: ImageButton) {
        when (postItem.status) {
            PostItemStatus.SUCCESS -> {
                statusBtn.setImageResource(android.R.drawable.checkbox_on_background)
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
    }

    override fun onPostHistoryUpdate() {
        activity.runOnUiThread {
            notifyDataSetChanged()
        }
    }
}