package com.felo.himlaya

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SimpleAdapter(var itemNum:Int): RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        return SimpleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.simple_list_item,parent,false))
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
       holder.textView.setText("Pos:$position")
    }

    override fun getItemCount(): Int {
       return itemNum
    }

    fun refreshCount(newItemCount:Int){
        itemNum = newItemCount
        notifyDataSetChanged()
    }
    class SimpleViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        val textView = itemView.findViewById<TextView>(R.id.textView)

    }
}