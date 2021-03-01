package com.krdevteam.buzzmap.util.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


abstract class BaseRecyclerViewAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder> {



    var listItems: ArrayList<T>
    lateinit var listener: ItemClickListener<T>

    constructor(
        listItems: ArrayList<T>,
        listener: ItemClickListener<T>
    ) {
        this.listItems = listItems
        this.listener = listener
        this.listener = listener
    }

    constructor(listener: ItemClickListener<T>) {
        listItems = ArrayList()
    }

    interface ItemClickListener<T> {

        fun onItemClick(v: View, item: T, position: Int)
    }

    fun setItems(listItems: ArrayList<T>) {
        this.listItems = listItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return getViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = listItems[position]
        holder.itemView.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View?)
            { listener.onItemClick(holder.itemView, item, position) }
        })
        (holder as Binder<T>).bind(listItems[position])
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutId(position, listItems[position])
    }

    protected abstract fun getLayoutId(position: Int, obj: T): Int

    abstract fun getViewHolder(view: View, viewType: Int):RecyclerView.ViewHolder

    internal interface Binder<T> {
        fun bind(data: T)
    }
}