package com.example.kcmav1.viewModels

import android.content.Intent
import android.view.View
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.ViewModel
import com.example.kcmav1.EmailValidation
import com.example.kcmav1.LoginActivity
import com.example.kcmav1.RegisterActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions





class LoginViewModel: ViewModel() {
    val name:String?=null
    val number:String?=null
    val email:String?=null
    val password:String?=null


    //open the next activities
    fun OpenLoginPage(view:View){
        Intent(view.context, LoginActivity::class.java).also {
            view.context.startActivity(it)
        }
    }
    fun OpenRegisterPage(view:View){
        Intent(view.context, RegisterActivity::class.java).also {
          view.context.startActivity(it)

    }
    }}

