package com.example.kcmav1.learn

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kcmav1.R
import com.example.kcmav1.model.Recent
import com.example.kcmav1.recyclers.RecentTutorialsAdapter
import com.google.android.material.card.MaterialCardView

class LearnerDashboard : AppCompatActivity() {
    lateinit var openVids:MaterialCardView
    lateinit var openPdf:MaterialCardView
    lateinit var openExam:MaterialCardView
    lateinit var recyclerView: RecyclerView
    val list=ArrayList<Recent>()
    lateinit var adapter:RecentTutorialsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learner_dashboard)
        recyclerView=findViewById(R.id.recent_learning_list)
        openPdf=findViewById(R.id.openPDf)
        openVids=findViewById(R.id.openVideos)
        openExam=findViewById(R.id.openExams)
        adapter= RecentTutorialsAdapter(list)
        recyclerView.layoutManager=LinearLayoutManager(applicationContext,RecyclerView.VERTICAL,false)

        openVids.setOnClickListener {
startActivity(Intent(applicationContext,VideoTutor::class.java))
        }
        openPdf.setOnClickListener {
startActivity(Intent(applicationContext,PdfTutor::class.java))
        }
        openExam.setOnClickListener {
            startActivity(Intent(applicationContext,ExamActivity::class.java))
        }
    }
}