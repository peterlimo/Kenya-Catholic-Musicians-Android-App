package com.example.kcmav1.learn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.kcmav1.DashActivity
import com.example.kcmav1.R
import com.example.kcmav1.model.Learner
import com.example.kcmav1.utils.AppPreferences
import com.example.kcmav1.utils.toast
import com.google.firebase.firestore.FirebaseFirestore

class LearnerDashWelcome : AppCompatActivity() {
    lateinit var db:FirebaseFirestore
    lateinit var get_started:Button
    lateinit var userEmail:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learner_dash_welcome)
        get_started=findViewById(R.id.get_started)
        db= FirebaseFirestore.getInstance()
        AppPreferences.init(this)
        if (AppPreferences.isLogin){
            userEmail=AppPreferences.email;
        }

        get_started.setOnClickListener {
            AddLearner()
        }
    }

    private fun AddLearner() {
        val learner=Learner(userEmail,false,0)
            db.collection("learn")
                .document("students")
                .collection("student")
                .document(userEmail)
                .set(learner)
        .addOnSuccessListener {
            toast("Joined Succesfully")
            val i= Intent(applicationContext, LearnerDashboard::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
            finishAffinity()
            finish()
        }
    }


}