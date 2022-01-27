package com.example.kcmav1.dashboard.search

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kcmav1.R
import com.example.kcmav1.SearchResultsActivity
import com.example.kcmav1.SongListActivity
import com.example.kcmav1.model.Type
import com.example.kcmav1.recyclers.TypeListAdapter
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : AppCompatActivity(),TypeListAdapter.OnItemClickListener{
    lateinit var db:FirebaseFirestore
    val type=ArrayList<Type>()
    lateinit var recyclerView: RecyclerView
    lateinit var adapter:TypeListAdapter
    lateinit var open_search_btn:LinearLayout
    lateinit var pd:ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_song_types)
        pd= ProgressDialog(this)
//        toolbar=findViewById(R.id.toolbar)
        recyclerView=findViewById(R.id.song_type_recycler)
        adapter= TypeListAdapter(type,this)
        val back_btn:ImageView=findViewById(R.id.back_btn)
        back_btn.setOnClickListener {
            this.finish()
        }
        open_search_btn=findViewById(R.id.open_search_btn)
        open_search_btn.setOnClickListener{
            startActivity(Intent(applicationContext,SearchResultsActivity::class.java))
        }
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        db= FirebaseFirestore.getInstance()
        getSongTypes()
    }


    override fun onItemClick(position:Int){
        val item= type.get(position)
        val _title:String=item.title
        val intent= Intent(this, SongListActivity::class.java)
        intent.putExtra("type",_title)
        startActivity(intent)
    }


    fun getSongTypes()
    {
        db.collection("files")
                .addSnapshotListener { value, e ->
    if (e!=null){
        return@addSnapshotListener
    }
                    type.clear()
     for (doc in value!!)
     {
         if (doc.exists()) {
             val title=doc.id
             type.add(Type(title))
             recyclerView.adapter=adapter
         }
     }
 }
 }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.search -> {
//                onSearchRequested()
//                true
//            }
//            else -> false
//        }
//    }
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.search_menu, menu)
//        // Associate searchable configuration with the SearchView
//        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        val searchView=MenuItemCompat.getActionView(menu.findItem(R.id.search)) as SearchView
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(ComponentName(this, SearchResultsActivity::class.java)))
//        searchView.isIconifiedByDefault=true
//        return true
//    }

}