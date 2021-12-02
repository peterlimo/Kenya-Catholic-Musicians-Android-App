package com.example.kcmav1.recyclers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kcmav1.R
import com.example.kcmav1.model.Room

class ForumListAdapter( var forumList :List<Room>,val listener:OnItemClickListener):RecyclerView.Adapter<ForumListAdapter.MyViewHolder>() {

   inner class MyViewHolder(itemView:View) :RecyclerView.ViewHolder(itemView),View.OnClickListener{

       fun bind(forum: Room){
           val title=itemView.findViewById<TextView>(R.id.textView_title)
           val rsender=itemView.findViewById<TextView>(R.id.current_sender)
           val rmessage=itemView.findViewById<TextView>(R.id.current_message)
           title.text=forum.name
           rsender.text=forum.sender
           rmessage.text=forum.curentMessage
                        }
     init {
         itemView.setOnClickListener (this)
     }
       override fun onClick(v: View?) {
           listener.onItemClick(adapterPosition)}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_forum_list_items,parent,false))

    }

    override fun getItemCount(): Int {
       return forumList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem=forumList[position]

//        holder.itemView.findViewById<TextView>(R.id.textView_id).text=currentItem.id.toString()
        holder.bind(currentItem)

    }

//    fun setData(forum:List<Forum>){
//        this.forumList=forum
//        notifyDataSetChanged()
//    }


    interface  OnItemClickListener{
        fun onItemClick(position: Int)
    }
}