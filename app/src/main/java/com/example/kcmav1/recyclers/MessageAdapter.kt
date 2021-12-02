package com.example.kcmav1.recyclers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kcmav1.R
import com.example.kcmav1.model.Text

class MessageAdapter (val text:List<Text>): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }
    inner class View2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var message: TextView = itemView.findViewById(R.id.received_text)
        fun bind(position: Int) {
            val mess = text[position]
            message.text = mess.text
        }
    }
   inner class MessageViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
fun bind(text: Text)
{
val message=itemView.findViewById<TextView>(R.id.received_text)
    val sender=itemView.findViewById<TextView>(R.id.time_received)
    message.text=text.text
    sender.text=text.sender
}

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageAdapter.MessageViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_received_message_item, parent, false)
        return MessageViewHolder(v)
    }

    override fun onBindViewHolder(holder: MessageAdapter.MessageViewHolder, position: Int) {
        holder.bind(text[position])
    }

    override fun getItemCount(): Int {
      return text.size
    }
}