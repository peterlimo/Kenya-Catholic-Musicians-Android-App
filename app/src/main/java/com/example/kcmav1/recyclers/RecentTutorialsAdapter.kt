package com.example.kcmav1.recyclers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kcmav1.R
import com.example.kcmav1.model.Recent

class RecentTutorialsAdapter(private val type:List<Recent>): RecyclerView.Adapter<RecentTutorialsAdapter.ViewHolder>() {

    inner class ViewHolder ( itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{


        fun bind(recent: Recent){
            val name=itemView.findViewById<TextView>(R.id.recent_text)
           name.text=recent.name

        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
//           if (position!=RecyclerView.NO_POSITION){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val inflatedView = parent.inflate(R.layout.list_item_recent_tutorial_view, false)
//        return ViewHolder(inflatedView)
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_recent_tutorial_view,parent,false))
    }

    override fun getItemCount(): Int {
        return type.size
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        val recent= type[position]
        holder.bind(recent)
    }


}


