package com.example.kcmav1.learn

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.example.kcmav1.R
import com.example.kcmav1.dashboard.test.Quiz
import com.example.kcmav1.model.Result
import com.example.kcmav1.utils.AppPreferences
import com.example.kcmav1.utils.toast
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class TakeExamActivity : Activity() {
    lateinit var title:String
    lateinit var db:FirebaseFirestore
    lateinit var r_group:RadioGroup
    lateinit var c1:RadioButton
    lateinit var c2:RadioButton
    lateinit var q_title:TextView
    lateinit var next:Button
    lateinit var done:Button
    val list= ArrayList<Quiz>()
    var qq= arrayListOf<String>()
    var size=0
    var counter=0;
    var isComplet=false
    var q_number=1;
    var isCounting=true
    lateinit var answer:String
    lateinit var q_no:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_exam)
        next=findViewById(R.id.next)
        done=findViewById(R.id.done)
        c1=findViewById(R.id.choice1)
        c2=findViewById(R.id.choice2)
        r_group=findViewById(R.id.q_choice)
        q_no=findViewById(R.id.q_no)
        q_title=findViewById(R.id.q_title)
        isComplet=false
AppPreferences.init(this)
        title=intent.getStringExtra("title").toString()
        db= FirebaseFirestore.getInstance()
        db.collection("exams")
                .document(title)
                .collection("questions")
                .get().addOnCompleteListener{value->
          if (value.isSuccessful){
              for (doc in value.result){
                  val q=doc.id
                  val a1=doc.get("a1").toString()
                  val a2=doc.get("a2").toString()
                  var quiz=Quiz(q,a1,a2)
                  qq.add(q)
                  list.add(quiz)
                  size=value.result.size()
                  q_title.text = qq[0]
                  q_no.text=="1"
                  db.collection("exams")
                      .document(title)
                      .collection("questions")
                      .document(qq[0])
                      .get().addOnSuccessListener {
                          val a1=it.get("a1").toString()
                          val a2=it.get("a2").toString()
                          answer=it.get("answer").toString()
                          c1.text = a1
                          c2.text = a2
                      }
              }
          }
         }
        val result = Result("0","0")
        db.collection("learn")
            .document("students")
            .collection("student")
            .document(AppPreferences.email)
            .collection("exams")
            .document(title)
            .set(result)
            .addOnSuccessListener {
                toast("quiz started successfully")
            }
         next.setOnClickListener {
             disPlay()
    }

        done.setOnClickListener {
val dialog=ResultDialog()

        }
    }

private  fun disPlay() {
    counter++
    q_number++
    isCounting = true
    c1.text = ""
    c2.text = ""
    if (counter == qq.size) {
        isCounting = false
        q_title.text = "Quiz Completed"
        done.isEnabled = true
        next.isEnabled = false
        c1.visibility = View.GONE
        c2.visibility = View.GONE
    }
//    when(qq[counter])
//    {
//        qq[qq.lastIndex]->
//        {
//            done.visibility=View.VISIBLE
//        }
//    }
    while (counter < qq.size) {
        q_title.text = qq[counter]
        q_no.text = q_number.toString()
        db.collection("exams")
                .document(title)
                .collection("questions")
                .document(qq[counter])
                .get().addOnSuccessListener {
                    val a1 = it.get("a1").toString()
                    val a2 = it.get("a2").toString()
                    answer = it.get("answer").toString()
                    c1.text = a1
                    c2.text = a2
                }
        val id = r_group.checkedRadioButtonId
        val radio = findViewById<RadioButton>(id)
        var choice = radio.text
        if (choice.equals(answer)) {
            db.collection("learn")
                    .document("students")
                    .collection("student")
                    .document(AppPreferences.email)
                    .collection("exams")
                    .document(title)
                    .update("passed", FieldValue.increment(1))
        } else {
            db.collection("learn")
                    .document("students")
                    .collection("student")
                    .document(AppPreferences.email)
                    .collection("exams")
                    .document(title)
                    .update("failed", FieldValue.increment(1))
        }
    }
}


}