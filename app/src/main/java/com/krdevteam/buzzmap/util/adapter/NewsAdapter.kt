package com.krdevteam.buzzmap.util.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.krdevteam.buzzmap.R
import com.krdevteam.buzzmap.entity.News
import kotlinx.android.synthetic.main.list_item_chat.view.*
import timber.log.Timber

class NewsAdapter(private val list: List<News>): RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val news = list[position]
        Timber.d("${news.title}")

//        if (news.user == uid) {
//            holder.itemView.textview_chat_sent.text = chatMessage.text
//            holder.itemView.textview_chat_received.visibility = View.GONE
//        } else {
//            holder.itemView.textview_chat_received.text = chatMessage.text
//            holder.itemView.textview_chat_sent.visibility = View.GONE
//        }

        holder.itemView.textview_chat_received.text = news.title
    }


    class ViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder(
        inflater.inflate(R.layout.list_item_chat, parent, false)
    ) {

        private var chatTextSent: TextView? = null
        private var chatTextReceived: TextView? = null

        init {
            chatTextSent = itemView.findViewById(R.id.textview_chat_sent)
            chatTextReceived = itemView.findViewById(R.id.textview_chat_received)
        }

    }

}