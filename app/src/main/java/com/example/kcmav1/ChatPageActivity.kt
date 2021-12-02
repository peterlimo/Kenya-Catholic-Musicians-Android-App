package com.example.kcmav1

import android.app.*
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kcmav1.dashboard.test.BestPostAdapter
import com.example.kcmav1.model.Liker
import com.example.kcmav1.model.Message
import com.example.kcmav1.model.Text
import com.example.kcmav1.utils.AppPreferences
import com.example.kcmav1.utils.toast
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


class ChatPageActivity : AppCompatActivity() ,BestPostAdapter.OnIconClickListener{
    var type by Delegates.notNull<Int>()
    lateinit var send_btn:LinearLayout
    lateinit var button_pick_image:LinearLayout
    lateinit var message_text:EditText
    lateinit var message_recyler:RecyclerView
    lateinit var db:FirebaseFirestore
    lateinit var groupId:String
    lateinit var userId:String
    lateinit var toolbar: Toolbar
    lateinit var uri:Uri
  lateinit var senderProf:String
    lateinit var fileurl:String
    lateinit var userEmail:String
    var isLiked:Boolean=true
    var viewType =0
     var isHavingImage:Boolean=true
    var isImageMess:Boolean=true
    val text=ArrayList<Message>()
    lateinit var currentDiocese:String
    lateinit var send:Text
    lateinit var mstoragerf: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_page)
        send_btn=findViewById(R.id.button_send_message)
        message_text=findViewById(R.id.edit_text_message)
        message_recyler=findViewById(R.id.recycler_gchat)
        button_pick_image=findViewById(R.id.button_pick_image)
        toolbar=findViewById(R.id.toolbar)
        button_pick_image.setOnClickListener { pickImage() }
        setSupportActionBar(toolbar)
        ///getting the group id
        groupId=intent.getStringExtra("room").toString()
        isHavingImage=false
        supportActionBar?.title=groupId
        //initializing progress dialog
        val progress=ProgressDialog(this)
        //initializing the firebase
        db=FirebaseFirestore.getInstance()
        //code to fetch messages and display on the recyclerview
        val adapter= BestPostAdapter( text,this)
        if (AppPreferences.url.isNotEmpty())
        {
            senderProf=AppPreferences.url
        }
        message_recyler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
//        db.collection("rooms").document(groupId).collection("messages").orderBy("time", Query.Direction.DESCENDING).get().addOnSuccessListener {
//            result->
            db.collection("rooms").document(groupId).collection("messages").orderBy("sentAt", Query.Direction.ASCENDING)
                     .addSnapshotListener { value, e ->
                        if (e!=null){
                            return@addSnapshotListener
                        }
                         text.clear()
                        for (doc in value!!)
                        {
                            if (doc.exists())
                            {
                                val did=doc.id
                                db.collection("rooms").document(groupId).collection("messages").document(did)
                                        .collection("likes")
                                        .document(userEmail)
                                        .get().addOnSuccessListener {
                                            if (it.exists())
                                            {
                                                isLiked=true
                                                val mess=doc.get("text").toString()
                                                val user=doc.get("sender").toString()
                                                val time=doc.get("time").toString()
                                                val fname=doc.get("forum_name").toString()
                                                val timeat=doc.get("sentAt").toString()
                                                val responses=doc.get("responses").toString()
                                                val date_m_year=doc.get("date_m_year").toString()
                                                val loves=doc.get("loves").toString()
                                                val imageUrl=doc.get("url").toString()
                                                val senderPro=doc.get("senderProf").toString()
                                                isImageMess = !(imageUrl.equals("null") && imageUrl.length==4)
                                                when(isImageMess){
                                                    true->
                                                    {
                                                        viewType=3
                                                    }
                                                    false->
                                                    {
                                                        viewType=1
                                                    }
                                                }
                                                text.add(Message(did,mess, user, viewType,time,currentDiocese,date_m_year,fname,loves,responses,isLiked,false,imageUrl,senderPro))
                                                message_recyler.adapter=adapter
                                                message_recyler.scrollToPosition(text.size-1)
                                            }

                                        }

                            }
                            else
                            {
                                toast("No messages found")
                            }
                        }
                         adapter.notifyDataSetChanged()
                    }



        AppPreferences.init(this)
        if (AppPreferences.isLogin){
          userId= AppPreferences.username
            currentDiocese=AppPreferences.diocese
            userEmail=AppPreferences.email
        }

//get the phone timestamp

//code to send message to room

        send_btn.setOnClickListener {
            val tsLong = System.currentTimeMillis() / 1000
            val ts = tsLong.toString()
            val dateFormat = SimpleDateFormat("HH:mm")
            val mdy=SimpleDateFormat("yyyy-MM-dd")
            val monthayyear:String=mdy.format(Date()).toString()
            val currentDateTime: String = dateFormat.format(Date()).toString() // Find todays date
            val message=message_text.text.toString()

            when (isHavingImage){
                true->
                {
                    mstoragerf = FirebaseStorage.getInstance().getReference("messageImage")
                    var mRefrence = mstoragerf.child(uri.lastPathSegment.toString())

                    mRefrence.putFile(uri).addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
                        fileurl = taskSnapshot!!.uploadSessionUri.toString()

                        mRefrence.downloadUrl.addOnSuccessListener {
//                            sendImageMessage(it.toString())
                            send = Text(message, userId, 1, currentDateTime, monthayyear, currentDiocese, 0, groupId, 0, it.toString(),senderProf)
                            db.collection("rooms").document(groupId)
                                    .collection("messages")
                                    .add(send)
                                    .addOnSuccessListener {
                                        toast("Message sent successfully")
                                        isHavingImage=false
                                        message_text.setText("")
                                    }
                                    .addOnFailureListener {

                                    }
                        }
                    }.addOnFailureListener {

                    }
                }
                false->
                {
                    send = Text(message, userId, 1, currentDateTime, monthayyear, currentDiocese, 0, groupId, 0,"null",senderProf)
                    db.collection("rooms").document(groupId)
                            .collection("messages")
                            .add(send)
                            .addOnSuccessListener {
                                toast("Message sent successfully")
                                message_text.setText("")
                            }
                            .addOnFailureListener {

                            }
                }

            }



   }
}



    private fun pickImage() {
    val i=Intent()
    i.action=Intent.ACTION_GET_CONTENT
    i.type="image/jpg"
    startActivityForResult(Intent.createChooser(i,"Choose image to send"),1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==1 && resultCode==RESULT_OK && data!==null && data.data!=null)
        {
            uri= data.data!!
            isHavingImage=true
        }
    }

    override fun onChatIconClicked(position: Int, v: View) {
       val item=text[position]
        val mid=item.mesid
        val ggid=item.gid
        val uname=item.sender
        val currentM=item.text
        openResponses(ggid,mid,currentM,uname)
    }

    override fun onLoveIconClicked(position: Int, v: View, imageView: ImageView) {
//        imageView.setImageResource(R.drawable.love_outlined_blue)
    }

    override fun onUnLoveIconClicked(position: Int, v: View, imageView: ImageView) {
        imageView.setImageResource(R.drawable.love_full_red)
    }
    override fun onShareIconClicked(position: Int, v: View) {
        toast("Share Clicked")
    }
    private fun Like(position: Int,imageView: ImageView) {

        val c=text[position]
        val id=c.mesid
        val fid=c.gid
       db = FirebaseFirestore.getInstance()
       db.collection("rooms")
                .document(fid)
                .collection("messages")
                .document(id)
                .collection("likes")
               .document(userEmail)
               .get()
               .addOnSuccessListener {
                   if(it.exists()) {
                      toast("You have already liked this post!")
                   }
                        else
                        {
                            updateLike(id,fid,imageView)
                        }

                }
    }
//                           db.collection("rooms")
//                                    .document(fid)
//                                    .collection("messages")
//                                    .document(id)
//                                    .update("loves", FieldValue.increment(-1))
//db.collection("rooms")
//.document(fid)
//.collection("messages")
//.document(id)
//.collection("likes")
//.document(email).delete()
    private fun updateLike(id: String, gid: String,imageView: ImageView) {
    db = FirebaseFirestore.getInstance()
        val liker= Liker(userEmail)
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
                            .document(userEmail)
                            .set(liker)
                            .addOnSuccessListener {
                                imageView.setImageResource(R.drawable.loe_full_blue)
                            }
                }
    }
    fun openResponses(ggid: String, mid: String,currentM: String, uname: String) {
        val i=Intent(applicationContext,ResponsesActivity::class.java)
        i.putExtra("cms",currentM)
        i.putExtra("gid",ggid)
        i.putExtra("mid",mid)
        i.putExtra("uname",uname)
        startActivity(i)
    }
}