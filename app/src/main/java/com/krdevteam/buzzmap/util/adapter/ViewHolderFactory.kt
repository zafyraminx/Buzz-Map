package com.ed2e.ed2eapp.adapter

import android.graphics.Color
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.krdevteam.buzzmap.R
import com.krdevteam.buzzmap.entity.News
import com.krdevteam.buzzmap.util.adapter.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.list_item_news.view.*
import kotlinx.android.synthetic.main.list_item_profile.view.*
import java.text.SimpleDateFormat
import java.util.*

object ViewHolderFactory {

    fun create(view: View, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.list_item_news -> NewsViewHolder(view)
            else -> {
                ProfileViewHolder(view)
            }
        }
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), BaseRecyclerViewAdapter.Binder<News> {
//        var textView: TextView
        init {
//            textView = itemView.findViewById(R.id.)
        }
        override fun bind(news: News) {
            itemView.list_item_news_textView_title.text = news.title
            itemView.list_item_news_textView_description.text = news.details

            val sdf = SimpleDateFormat("dd-MMM-yyyy hh:mm", Locale.US)
            val dateStr = sdf.format(news.dateTime.toDate())
            itemView.list_item_news_textView_date.text = dateStr
            var url = ""
            if (news.imageURL.size >= 1)
            {
                url = news.imageURL[0]
                Glide
                        .with(itemView)
                        .load(url)
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(itemView.list_item_news_imageView)
            }
        }
    }

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), BaseRecyclerViewAdapter.Binder<String> {
        override fun bind(data: String) {
            if (data.toLowerCase(Locale.ROOT) == "logout"){
                itemView.list_item_profile_textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
                itemView.list_item_profile_textView.gravity = Gravity.CENTER
                itemView.list_item_profile_textView.setTextColor(Color.RED)
            }
            itemView.list_item_profile_textView.text = data
        }
    }
}
