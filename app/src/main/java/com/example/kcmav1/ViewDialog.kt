package com.example.kcmav1

import android.app.Dialog
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import com.example.kcmav1.learn.ExamActivity
import com.example.kcmav1.learn.TakeExamActivity


class ViewDialog {
    fun startExamDialog(activity: ExamActivity, title: String, no: String)
    {
val dialog = Dialog(activity)
        dialog.setContentView(R.layout.alert_start_exam_dialog)
        var titlee=dialog.findViewById<TextView>(R.id.dialog_title)
        var message=dialog.findViewById<TextView>(R.id.dialog_message)
        val button=dialog.findViewById<Button>(R.id.dialog_ok_btn)
        titlee.text = title
        message.text="This paper contains $no Questions"
        button.setOnClickListener {
            dialog.dismiss()
            val i= Intent(activity, TakeExamActivity::class.java)
            i.putExtra("title", title)
            activity.startActivity(i)
        }
        dialog.setCancelable(true)
        dialog.show()
    }
}