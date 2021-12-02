package com.example.kcmav1.recyclers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.kcmav1.R
import com.example.kcmav1.model.DList
import com.example.kcmav1.model.Type
import com.example.kcmav1.model.UploadSongData
//import com.example.kcmav1.utils.inflate

class ViewSongsOfTypeAdapter (private val type:List<DList>, private val listener: OnItemClickListener):RecyclerView.Adapter<ViewSongsOfTypeAdapter.SongsViewHolder>() {

    inner class SongsViewHolder ( itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{


        fun bind(songData: DList){
            val title=itemView.findViewById<TextView>(R.id.song_title)
            val views=itemView.findViewById<TextView>(R.id.song_views)
            val downloads=itemView.findViewById<TextView>(R.id.song_downloads)
            val composer=itemView.findViewById<TextView>(R.id.song_composer)
            title.text=songData.title
            views.text=songData.views
            downloads.text=songData.downloads
            composer.text=songData.composer
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
//           if (position!=RecyclerView.NO_POSITION){
            listener.onItemClickme(position)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsViewHolder {
//        val inflatedView = parent.inflate(R.layout.list_item_all_songs, false)
//        return SongsViewHolder(inflatedView)
        return SongsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_all_songs,parent,false))

    }

    override fun getItemCount(): Int {
        return type.size
    }

    override fun onBindViewHolder(holder: SongsViewHolder, position: Int) {
        val song= type[position]
        holder.bind(song)
    }

    interface OnItemClickListener {
        fun onItemClickme(position: Int) {

        }
    }


}