package com.example.kcmav1

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kcmav1.model.DList
import com.example.kcmav1.recyclers.ViewSongsOfTypeAdapter
import com.google.firebase.firestore.FirebaseFirestore

class SearchResultsActivity : AppCompatActivity(), ViewSongsOfTypeAdapter.OnItemClickListener {
    lateinit var search_results:RecyclerView
    val songitem=ArrayList<DList>()
    lateinit var edt_search:EditText
    lateinit var adapter:ViewSongsOfTypeAdapter
    lateinit var pd:ProgressDialog
    lateinit var toolbar: Toolbar
    lateinit var back_btn:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)

        search_results=findViewById(R.id.search_results)
        pd=ProgressDialog(this)
        edt_search=findViewById(R.id.edt_search)
        back_btn=findViewById(R.id.back_btn)
        edt_search.requestFocus()
        back_btn.setOnClickListener {
            this.finish()
        }
         val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query=edt_search.text.toString()
                search(query)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                output.text = s
//                if (start == 12) {
//                    Toast.makeText(applicationContext, "Maximum Limit Reached", Toast.LENGTH_SHORT)
//                            .show()
//                }
            }
        }
        edt_search.addTextChangedListener(textWatcher)
//        toolbar=findViewById(R.id.result_toolbar)
//        setSupportActionBar(toolbar)
//        supportActionBar?.setHomeButtonEnabled(true)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        if (Intent.ACTION_SEARCH == intent.action) {
//            val query = intent.getStringExtra(SearchManager.QUERY)
//
//        }

        edt_search.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = edt_search.text.toString()
                search(query)
                true
            }
            false
        }

        search_results.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter= ViewSongsOfTypeAdapter(songitem, this)
    }


    private fun search(text: String) {

        val db=FirebaseFirestore.getInstance()
        db.collectionGroup("songs")
                .addSnapshotListener{value,e->
                    if (e!=null)
                    {
                        return@addSnapshotListener
                    }
                    songitem.clear()
                    for (document in value!!) {
                        if (document.exists()) {
                            var song_title = arrayListOf(document.getString("title")+document.getString("composer")+document.getString("type"))
                            val tt = document.getString("title").toString().toLowerCase()
                            if (tt == text || tt.startsWith(text) || tt.contains(text) || tt.endsWith(text) || tt.contentEquals(text)) {
                                val song_composer = document.get("composer").toString()
                                val song_views = document.get("views").toString()
                                val dnumber = document.get("downloads").toString()
                                val stype = document.get("type").toString()
                                songitem.add(DList(tt, song_views, dnumber, song_composer, stype, document.id))
                                search_results.adapter = adapter

                            }

//                            val match = song_title.filter { it in text!! }
//                            match.forEach { d ->
//
//
//                            }


                        }


//                .get()
//                .addOnCompleteListener { task ->
//
//                    for (document in task.result!!) {
//
//                        else {
//                            tv_query.setText("Result not available")
//                        }
//                    }
//                }
//    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        this.finish()
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
//        return true
//    }
                    }}}
    override fun onItemClickme(position: Int) {
        val user= songitem.get(position)
        val _title:String=user.title
        var _type:String=user.type
        val _id=user.id
        val intent= Intent(this, SongDetailActivity::class.java)
        intent.putExtra("title", _title)
        intent.putExtra("type", _type)
        intent.putExtra("id",_id)
        startActivity(intent)
    }
}