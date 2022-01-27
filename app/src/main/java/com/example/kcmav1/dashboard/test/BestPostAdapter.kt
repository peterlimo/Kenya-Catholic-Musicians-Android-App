package com.example.kcmav1.dashboard.test

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kcmav1.R
import com.example.kcmav1.model.Message
import com.example.kcmav1.recyclers.ForumListAdapter
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class BestPostAdapter(var list:List<Message>,var listener:OnIconClickListener): RecyclerView.Adapter<BestPostAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {

        var messag: TextView = itemView.findViewById(R.id.sent_message_textview)
        var time: TextView = itemView.findViewById(R.id.time)
        val user_profile: CircleImageView =itemView.findViewById(R.id.mesenger)
        val senderr: TextView =itemView.findViewById(R.id.current_diocese)
        val email: TextView =itemView.findViewById(R.id.my_email)

        val no_of_loves: TextView =itemView.findViewById(R.id.no_of_loves)
        val no_of_responses:TextView=itemView.findViewById(R.id.no_of_messages)
        val message_layout:LinearLayout=itemView.findViewById(R.id.message_layout)
        //display_fields
        //clickable fields
        val love_btn:ImageView=itemView.findViewById(R.id.love_btn)
        val unlove_btn:ImageView=itemView.findViewById(R.id.unlove_btn)
        val chat_btn: ImageView =itemView.findViewById(R.id.open_chats)
        val share_btn:ImageView=itemView.findViewById(R.id.share_chat)
        fun bind(message: Message) {
            if (message.url!="null")
            {
                val image_message:ImageView=itemView.findViewById(R.id.image_message)
                Picasso.get().load(message.url).into(image_message)
            }
            if(message.text.isEmpty() && message.url!="null" )
            {
                message_layout.visibility=View.GONE
            }
            if (message.isLike)
            {
                love_btn.setImageResource(R.drawable.loe_full_blue)
            }
            else
            {
                love_btn.setImageResource(R.drawable.love_outlined_blue)
            }
            if (message.isUnLike)
            {
                unlove_btn.setImageResource(R.drawable.love_full_red)
            }
            else
            {
                unlove_btn.setImageResource(R.drawable.love_outlined_red)
            }
            messag.text = message.text
            time.text=message.time
            no_of_loves.text=message.loves
            no_of_responses.text=message.responses
            email.setText("@/"+message.diocese)
            senderr.setText("ME")
            Picasso.get().load(message.senderProf).into(user_profile)
            chat_btn.setOnClickListener {
                listener.onChatIconClicked(adapterPosition,it)
            }
            love_btn.setOnClickListener {
                listener.onLoveIconClicked(adapterPosition,it,love_btn)
            }
            unlove_btn.setOnClickListener {
                listener.onUnLoveIconClicked(adapterPosition,it,unlove_btn)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType==1) {
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_send_message_item,parent,false))
        }
        else{
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_send_image_msg_item,parent,false))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    interface  OnIconClickListener{
        fun onChatIconClicked(position:Int,v:View)
        fun onLoveIconClicked(position:Int,v:View,imageView: ImageView)
        fun onUnLoveIconClicked(position:Int,v:View,imageView: ImageView)
        fun onShareIconClicked(position:Int,v:View)
    }

    override fun getItemViewType(position: Int): Int {
        val item=list[position].url
        return if (item == "null") {
            1
        }
        else {
            2
        }
    }
}
