package com.example.kcmav1.dashboard.hire

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kcmav1.R
import com.example.kcmav1.model.Hire
import com.example.kcmav1.recyclers.HireAdapter
import com.example.kcmav1.utils.AppPreferences
import com.google.firebase.firestore.FirebaseFirestore


class HireActivity : AppCompatActivity() ,HireAdapter.OnItemClickListener{
lateinit var recyclerView: RecyclerView
val list=ArrayList<Hire>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hire)
        recyclerView=findViewById(R.id.hire_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
       val toolbar = findViewById<Toolbar>(R.id.hire_toolbar)
        setSupportActionBar(toolbar)
        AppPreferences.init(this)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val adapter=HireAdapter(list, this)
        recyclerView.adapter=adapter
        val db=FirebaseFirestore.getInstance()
        db.collection("hires")
                .get()
                .addOnSuccessListener {
                    for (doc in it)
                    {
                        if (doc.exists())
                        {
                            val item=Hire(doc.getString("email").toString(), doc.getString("church").toString(), doc.get("desc").toString())
                            list.add(item)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
    }

    override fun onHireClick(position: Int) {
        val item=list[position]
        val email = Intent(Intent.ACTION_SEND)
        email.putExtra(Intent.EXTRA_EMAIL, item.email)
        email.putExtra(Intent.EXTRA_SUBJECT,"HIRE FOR CHOIR TRAINING")
        email.putExtra(Intent.EXTRA_TEXT,"I am ${AppPreferences.username} from ${AppPreferences.church} And i am requesting you for a hire as a music trainer your church.For more information please contact me on ${AppPreferences.email}/${AppPreferences.number}.Thank you")
        email.type = "message/rfc822"
        startActivity(Intent.createChooser(email, "Choose an Email client :"))
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