package com.example.kcmav1.learn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kcmav1.R
import com.example.kcmav1.model.PDF
import com.example.kcmav1.recyclers.PdfListAdapter
import com.example.kcmav1.utils.toast
import com.google.firebase.firestore.FirebaseFirestore

class PdfTutor : AppCompatActivity() ,PdfListAdapter.OnItemClickListener{
    val pdf=ArrayList<PDF>()
    val pdf1=ArrayList<PDF>()
    lateinit var db:FirebaseFirestore
    lateinit var adapter: PdfListAdapter
    lateinit var adapter1:PdfListAdapter
    lateinit var recyclerView:RecyclerView
    lateinit var recyclerBeginner:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_tutor)

        db= FirebaseFirestore.getInstance()
        recyclerView=findViewById(R.id.advanced_tutorials)
        recyclerBeginner=findViewById(R.id.beginner_tutorials)
        recyclerBeginner.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter= PdfListAdapter(pdf,this)
        adapter1= PdfListAdapter(pdf1,this)

        fetchBiginner()
        fetchAdvanced()
    }

    private fun fetchAdvanced() {
       db.collection("learn")
               .document("pdfmaterials")
               .collection("advanced")
               .get().addOnSuccessListener {
                   val title_av=findViewById<LinearLayout>(R.id.title_adv)
                   for (document in it)
                   {
                      if (document.exists())
                      {
                          title_av.visibility= View.VISIBLE
                          val name=document.id
                          pdf.add(PDF(name))
                          recyclerView.adapter=adapter
                      }
                   }
               }
               .addOnFailureListener {


               }
    }
    private fun fetchBiginner() {
        db.collection("learn")
                .document("pdfmaterials")
                .collection("beginner")

                .get().addOnSuccessListener {
                    val title_bg=findViewById<LinearLayout>(R.id.title_bg)
                    for (document in it)
                    {
                        if (document.exists())
                        {
                            title_bg.visibility=View.VISIBLE
                            val name=document.id
                            pdf1.add(PDF(name))
                            recyclerBeginner.adapter=adapter1
                        }
                    }
                }
                .addOnFailureListener {


                }
    }

    override fun onItemClick(position: Int) {
        val i= Intent(applicationContext,ViewPdfActivity::class.java)
        startActivity(i)
    }
}