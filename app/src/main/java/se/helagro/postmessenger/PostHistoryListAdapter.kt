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


class PostHistoryListAdapter :
    ArrayAdapter<PostItem>, PostHistoryListener, NetworkHandlerListener {

    val inflater = LayoutInflater.from(context)
    val postHistory: PostHistory

    constructor(context: Context, postHistory: PostHistory) :
            super(context, -1, postHistory) {
        this.postHistory = postHistory
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val postItem = postHistory[position]
        val listItem = inflater.inflate(R.layout.listitem_post, null)
        val textView: TextView = listItem.findViewById(R.id.postLogListText)
        textView.text = postItem.msg


        //=========== STATUS BUTTON ===========

        val statusBtn: ImageButton = listItem.findViewById(R.id.postLogListImgBtn)
        statusBtn.setOnClickListener{
            val networkHandler  = NetworkHandler(NetworkHandler.getEndpoint()!!)
            networkHandler .sendMessage(postItem, this)
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
        if(!isMainThread()){
            return
        }
        notifyDataSetChanged()
    }

    fun isMainThread(): Boolean {
        return Looper.getMainLooper() != Looper.myLooper()
    }

    override fun onUpdate(code: Int) {
        postHistory.alertListeners()
    }
}