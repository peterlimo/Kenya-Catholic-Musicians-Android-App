package com.example.kcmav1.learn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kcmav1.R
import com.example.kcmav1.ViewDialog
import com.example.kcmav1.model.PDF
import com.example.kcmav1.recyclers.PdfListAdapter
import com.example.kcmav1.utils.toast
import com.google.firebase.firestore.FirebaseFirestore

class ExamActivity : AppCompatActivity() ,PdfListAdapter.OnItemClickListener{
    lateinit var recyclerView: RecyclerView
    val pdf=ArrayList<PDF>()
    lateinit var db: FirebaseFirestore
    lateinit var adapter: PdfListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam)
        recyclerView=findViewById(R.id.exam_recycler)
        db= FirebaseFirestore.getInstance()
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter= PdfListAdapter(pdf,this)
        fetchExamGrades()
    }

    private fun fetchExamGrades(){
        db.collection("exams")
                .get()
                .addOnSuccessListener {
                    for (document in it)
                    {
                        if (document.exists())
                        {
                            val name=document.id
                            pdf.add(PDF(name))
                            recyclerView.adapter=adapter
                        }
                    }
                }
    }

    override fun onItemClick(position: Int) {
        val item= pdf[position]
        val id=item.name
        val viewDialog=ViewDialog()
        viewDialog.startExamDialog(this,id,"10")
    }

}