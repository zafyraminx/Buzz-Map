package com.krdevteam.buzzmap.util.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.krdevteam.buzzmap.R
import com.krdevteam.buzzmap.entity.News
import kotlinx.android.synthetic.main.list_item_news.view.*
import java.text.SimpleDateFormat
import java.util.*

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
        holder.itemView.list_item_news_textView_title.text = news.title
        holder.itemView.list_item_news_textView_description.text = news.details

        val sdf = SimpleDateFormat("dd-MMM-yyyy hh:mm", Locale.US)
        val dateStr = sdf.format(news.dateTime.toDate())
        holder.itemView.list_item_news_textView_date.text = dateStr
//        holder.itemView.list_item_news_imageView.setImageURI()
        var url = ""
        if (news.imageURL.size >= 1)
        {
            url = news.imageURL[0]
            Glide
                .with(holder.itemView)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.itemView.list_item_news_imageView)
        }
    }


    class ViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder(
        inflater.inflate(R.layout.list_item_news, parent, false)
    ) {

    }

}