package com.example.kcmav1.recyclers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kcmav1.R
import com.example.kcmav1.dashboard.search.HomeFragment
import com.example.kcmav1.model.Type
//import com.example.kcmav1.utils.inflate

class TypeListAdapter(private val type:List<Type>, private val listener: OnItemClickListener):RecyclerView.Adapter<TypeListAdapter.TypeViewHolder>() {

   inner class TypeViewHolder (itemView: View):RecyclerView.ViewHolder(itemView),View.OnClickListener{


       fun bind(type:Type){

         val title=itemView.findViewById<TextView>(R.id.type_title)
           title.text=type.title
       }

       init {
           itemView.setOnClickListener(this)
       }

       override fun onClick(v: View?) {
           val position = adapterPosition
           listener.onItemClick(position)
       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
//        val inflatedView = parent.inflate(R.layout.list_item_song_type, false)
//
//        return TypeViewHolder(inflatedView)
        return TypeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_song_type,parent,false))

    }

    override fun getItemCount(): Int {
        return type.size
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
      val title= type[position]
        holder.bind(title)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int);
    }
}