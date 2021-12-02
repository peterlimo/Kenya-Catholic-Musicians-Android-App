package com.example.kcmav1.recyclers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kcmav1.R
import com.example.kcmav1.model.Hire

class HireAdapter(var forumList :List<Hire>, val listener:OnItemClickListener):RecyclerView.Adapter<HireAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView:View) :RecyclerView.ViewHolder(itemView){

        fun bind(hire: Hire){
            val email=itemView.findViewById<TextView>(R.id.h_email)
            val church=itemView.findViewById<TextView>(R.id.h_church)
            val desc=itemView.findViewById<TextView>(R.id.h_desc)
            email.text=hire.email
            church.text=hire.church
            desc.text=hire.desc
            val btn=itemView.findViewById<Button>(R.id.h_btn)
            btn.setOnClickListener {
                listener.onHireClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_hire_me,parent,false))

    }

    override fun getItemCount(): Int {
        return forumList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem=forumList[position]
        holder.bind(currentItem)

    }

    interface  OnItemClickListener{
        fun onHireClick(position: Int)
    }
}