package com.example.kcmav1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kcmav1.model.Response
import com.example.kcmav1.recyclers.ResponsesAdapter
import com.example.kcmav1.utils.AppPreferences
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ResponsesActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var gid:String
    lateinit var mid:String
    lateinit var una:String
    lateinit var message_note:TextView
    lateinit var recyclerView: RecyclerView
    lateinit var db:FirebaseFirestore
    lateinit var response_text:EditText
    lateinit var response_btn:LinearLayout
    lateinit var sender:String
    lateinit var adapter:ResponsesAdapter
    val list=ArrayList<Response>()
    lateinit var sent_message:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_responses)
        message_note=findViewById(R.id.message_note)
        recyclerView=findViewById(R.id.responsess)
        response_btn=findViewById(R.id.response_btn)
        response_text=findViewById(R.id.response_text)
        sent_message=findViewById(R.id.sent_message)
        adapter= ResponsesAdapter(list)
        toolbar=findViewById(R.id.response_toolbar)
        setSupportActionBar(toolbar)

        mid=intent.getStringExtra("mid").toString()
        gid=intent.getStringExtra("gid").toString()
        una=intent.getStringExtra("uname").toString()
        val mis=intent.getStringExtra("cms").toString()
        val ts:TextView=findViewById(R.id.current_sender)
        ts.setText(una)
        sent_message.setText(mis)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
AppPreferences.init(this)
        if (AppPreferences.isLogin)
        {
          sender=AppPreferences.username
        }
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        db= FirebaseFirestore.getInstance()
        db.collection("rooms")
                .document(gid)
                .collection("messages")
                .document(mid)
                .collection("responses")
                .orderBy("timeAt",Query.Direction.ASCENDING)
                .addSnapshotListener { value, e ->
                    if (e!=null){
                        return@addSnapshotListener
                        message_note.visibility=View.VISIBLE
                        message_note.setText("No Responses Found")
                    }
                    list.clear()
                    for (doc in value!!)
                    {
                        if(doc.exists()) {
                            val sende = doc.get("sender").toString()
                            val mess = doc.get("message").toString()
                            val time = doc.get("date").toString()
                            val mid = ""
                            if (sende.equals(sender)) {
                                mid == "ME"
                            } else {
                                mid == sende
                            }
                            list.add(Response(sende, mess, time))
                            recyclerView.adapter = adapter
                            recyclerView.scrollToPosition(list.size - 1)
                        }
                        else{

                        }
                    }
                }
        response_btn.setOnClickListener {
            Respond()
        }
    }

    private fun Respond() {
        val mdy= SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss")
        val monthayyear:String=mdy.format(Date()).toString()
        val response=response_text.text.toString()
        val data= Response(sender,response,monthayyear)
        db.collection("rooms")
            .document(gid)
            .collection("messages")
            .document(mid)
            .update("responses", FieldValue.increment(1)).addOnSuccessListener {
                db.collection("rooms")
                    .document(gid)
                    .collection("messages")
                    .document(mid)
                    .collection("responses")
                    .add(data)
                    .addOnSuccessListener {
                        response_text.setText("")
                    }
            }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}