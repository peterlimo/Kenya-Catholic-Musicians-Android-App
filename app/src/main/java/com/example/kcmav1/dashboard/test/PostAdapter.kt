package com.example.kcmav1.dashboard.test

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.kcmav1.R
import com.example.kcmav1.ResponsesActivity
import com.example.kcmav1.model.Liker
import com.example.kcmav1.model.Message
import com.example.kcmav1.utils.AppPreferences
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


class PostAdapter(context: Context, list: ArrayList<Message>) :
    RecyclerView.Adapter<PostAdapter.View1ViewHolder>() {
    lateinit var db: FirebaseFirestore
    companion object {


    }

    private val context: Context = context
    var list: ArrayList<Message> = list

    inner class View1ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        lateinit var love_btn: ImageView
        var message: TextView = itemView.findViewById(R.id.sent_message_textview)
        var time: TextView = itemView.findViewById(R.id.time)
        val senderr: TextView = itemView.findViewById(R.id.current_diocese)
        val email: TextView = itemView.findViewById(R.id.my_email)
        var isss: Boolean = true

        val chat_btn: ImageView = itemView.findViewById(R.id.open_chats)
        val no_of_loves: TextView = itemView.findViewById(R.id.no_of_loves)

        fun bind(position: Int) {
            love_btn = itemView.findViewById(R.id.love_btn)
            val recyclerViewModel = list[position]
            message.text = recyclerViewModel.text
            time.text = recyclerViewModel.time
            isss = recyclerViewModel.isLike
            no_of_loves.text = recyclerViewModel.loves
            val mid = recyclerViewModel.mesid
            val ggid = recyclerViewModel.gid
            val uname = recyclerViewModel.sender
            val currentM = recyclerViewModel.text
            email.setText("@/" + recyclerViewModel.diocese)
            senderr.setText("ME")
            chat_btn.setOnClickListener {
                OpenResponses(ggid, mid, it, currentM, uname)
            }
            love_btn.setOnClickListener {
                Like(it.context, adapterPosition)
            }
        }

        private fun OpenResponses(
            ggid: String,
            mid: String,
            it: View,
            currentM: String,
            uname: String
        ) {
            val i = Intent(it.context, ResponsesActivity::class.java)
            i.putExtra("cms", currentM)
            i.putExtra("gid", ggid)
            i.putExtra("mid", mid)
            i.putExtra("uname", uname)
            context.startActivity(i)
        }



        private fun Like(contex: Context?, position: Int) {

            if (contex != null) {
                AppPreferences.init(contex.applicationContext)
            }
            val email = AppPreferences.email
            val c = list[position]
            val id = c.mesid
            val fid = c.gid
            db = FirebaseFirestore.getInstance()
            db.collection("rooms")
                .document(fid)
                .collection("messages")
                .document(id)
                .collection("likes")
                .document(email)
                .get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        db.collection("rooms")
                            .document(fid)
                            .collection("messages")
                            .document(id)
                            .update("loves", FieldValue.increment(-1))
                        db.collection("rooms")
                            .document(fid)
                            .collection("messages")
                            .document(id)
                            .collection("likes")
                            .document(email).delete()

                    } else {
                        updateLike(itemView, context, id, fid)
                    }
                }
        }


    }


    private fun updateLike(context1: View, context: Context?, id: String, gid: String) {

        db = FirebaseFirestore.getInstance()
        if (context != null) {
            AppPreferences.init(context1.context)
        }
        val email = AppPreferences.email
        val liker = Liker(email)
        db.collection("rooms")
            .document(gid)
            .collection("messages")
            .document(id)
            .update("loves", FieldValue.increment(1))
            .addOnSuccessListener {
                db.collection("rooms")
                    .document(gid)
                    .collection("messages")
                    .document(id)
                    .collection("likes")
                    .document(email)
                    .set(liker)
            }


    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): View1ViewHolder {

            return View1ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.list_item_send_message_item, parent, false)
            )

    }

    override fun getItemCount(): Int {
        return list.size
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: View1ViewHolder, position: Int) {

            holder.bind(position)


    }




}
