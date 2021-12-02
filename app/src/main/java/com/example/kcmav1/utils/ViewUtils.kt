package com.example.kcmav1.utils

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.kcmav1.R
import com.google.android.material.textfield.TextInputEditText

fun Context.toast(message:String){
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}


fun Context.isEmpty(text:TextInputEditText):Boolean{
    val Str:CharSequence=text.text.toString()
    return  TextUtils.isEmpty(Str)
}
fun Context.showDialog(title:String,msg:String) {
    val customDialog = Dialog(this)
    customDialog.setContentView(R.layout.alert_dialog)
    customDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    val yesBtn = customDialog.findViewById(R.id.ok_btn) as Button
    val tttle=customDialog.findViewById<TextView>(R.id.dialog_title)
    tttle.setText(title)
    val mssg=customDialog.findViewById<TextView>(R.id.dialog_message)
    mssg.setText(msg)
    yesBtn.setOnClickListener {
        //Do something here
        customDialog.dismiss()
    }

    customDialog.show()
}
