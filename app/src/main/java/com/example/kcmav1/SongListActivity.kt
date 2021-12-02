package com.example.kcmav1

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kcmav1.model.DList
import com.example.kcmav1.model.Type
import com.example.kcmav1.model.UploadSongData
import com.example.kcmav1.recyclers.TypeListAdapter
import com.example.kcmav1.recyclers.ViewSongsOfTypeAdapter
import com.google.firebase.firestore.FirebaseFirestore

class SongListActivity : AppCompatActivity(),ViewSongsOfTypeAdapter.OnItemClickListener{
    lateinit var textView: TextView
    lateinit var song_type:String
    lateinit var db:FirebaseFirestore
  val songitem=ArrayList<DList>()
    lateinit var toolbar: Toolbar
    lateinit var recyclerView: RecyclerView
    lateinit var adapter:ViewSongsOfTypeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_list)
        song_type=intent.getStringExtra("type").toString()
        toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView=findViewById(R.id.song_list_recycler)
        adapter= ViewSongsOfTypeAdapter(songitem,this)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val progress= ProgressDialog(this)
        db= FirebaseFirestore.getInstance()
        progress.show()
        progress.setTitle("Please wait!")
        progress.setMessage("fetching documents")
        db.collection("files")
            .document(song_type)
            .collection("songs")
                .addSnapshotListener { value, e ->

                    if (e != null) {
                        return@addSnapshotListener
                    }
                    progress.dismiss()
                    songitem.clear()
                    for (document in value!!) {
                        if (document.exists()) {
                            var d = document.get("title").toString()
                            var song_composer = document.get("composer").toString()
                            var song_views = document.get("views").toString()
                            var dnumber = document.get("downloads").toString()
                            var stype = document.get("type").toString()
                            songitem.add(DList(d, song_views, dnumber, song_composer, stype, document.id))
                            recyclerView.adapter = adapter
                        }
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


    override fun onItemClickme(position:Int){
       val user= songitem.get(position)
        val _title:String=user.title
        val _stype:String=user.type
        val _id:String=user.id
       val intent= Intent(this, SongDetailActivity::class.java)
       intent.putExtra("title",_title)
       intent.putExtra("type",_stype)
        intent.putExtra("id",_id)
        startActivity(intent)
    }

}