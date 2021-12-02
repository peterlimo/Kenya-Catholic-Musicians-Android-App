package com.example.kcmav1.recyclers


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kcmav1.R
import com.example.kcmav1.model.Video
import com.squareup.picasso.Picasso

class VideoTutorialsAdapter( var forumList :List<Video>,val listener:OnItemClickListener):RecyclerView.Adapter<VideoTutorialsAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView:View) :RecyclerView.ViewHolder(itemView),View.OnClickListener{

        fun bind(video: Video){

            val title=itemView.findViewById<TextView>(R.id.video_title)
            val image=itemView.findViewById<ImageView>(R.id.image_thumb)
            title.text=video.title
            Picasso.get().load(video.imglink).into(image)
        }
        init {
            itemView.setOnClickListener (this)
        }
        override fun onClick(v: View?) {
            listener.onItemClick(adapterPosition)}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_videotutorials,parent,false))

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