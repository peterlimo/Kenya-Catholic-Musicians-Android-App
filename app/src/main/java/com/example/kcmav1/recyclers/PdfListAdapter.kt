package com.example.kcmav1.recyclers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kcmav1.R
import com.example.kcmav1.model.PDF

class PdfListAdapter(var forumList :List<PDF>, val listener:OnItemClickListener): RecyclerView.Adapter<PdfListAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

        fun bind(pdf: PDF){
            val title=itemView.findViewById<TextView>(R.id.pdf_text)
            title.text=pdf.name
        }
        init
        {
            itemView.setOnClickListener (this)
        }
        override fun onClick(v: View?) {
            listener.onItemClick(adapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_pdf_list_items,parent,false))
    }

    override fun getItemCount(): Int {
        return forumList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem=forumList[position]
        holder.bind(currentItem)
    }

    interface  OnItemClickListener{
        fun onItemClick(position: Int)
    }
}