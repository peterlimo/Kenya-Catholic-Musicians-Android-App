package com.example.kcmav1

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.kcmav1.databinding.ActivityLoginBinding
import com.example.kcmav1.model.MyForums
import com.example.kcmav1.room.ForumViewModel
import com.example.kcmav1.utils.AppPreferences
import com.example.kcmav1.utils.isEmpty
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity()  {
   lateinit var binding:ActivityLoginBinding
   lateinit var db:FirebaseFirestore
    lateinit var forumViewModel: ForumViewModel
    lateinit var mainForum:String
    lateinit var dioceseForum:String
    lateinit var em:String
    lateinit var foum:MyForums
    lateinit var nn:MyForums
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_login)
         forumViewModel=ViewModelProviders.of(this)
            .get(ForumViewModel::class.java)
        AppPreferences.init(this)

        val signibtn: Button=findViewById(R.id.sign_in)
       signibtn.setOnClickListener({
            ValidateData()
        })
        mainForum="The National Music Forum"
    }

    private fun ValidateData() {

        var isdatavalid:Boolean=true
        if (isEmpty(binding.loginEmail)){
            binding.loginEmail.setError("Email is required!")
            isdatavalid=false
        }
        if (isEmpty(binding.loginPassword)){
            binding.loginPassword.setError("Password is required!")
            isdatavalid=false
        }
        if (isdatavalid){
            ifUserExists()
        }
    }

    private fun ifUserExists() {
        val progress=ProgressDialog(this)
        progress.setTitle("Please wait")
        progress.isIndeterminate

        progress.show()
        db= FirebaseFirestore.getInstance()
        val email=findViewById<TextInputEditText>(R.id.login_email)
        val password=findViewById<TextInputEditText>(R.id.login_password)
        db.collection("users").document(email.text.toString()).get().addOnSuccessListener { document->
            if(document.exists()){
                val pass=document?.get("password").toString()

                if(pass==password.text.toString()){

                    db.collection("users").document(email.text.toString()).get().addOnSuccessListener { documents ->
                        if (documents.exists()) {
                            val username = documents.get("name")
                            val number = documents.get("number")
                            val diocese = documents.get("diocease")
                             em=documents.get("email").toString()
                            AppPreferences.isLogin=true
                            AppPreferences.password = password.text.toString()
                            AppPreferences.username=username.toString()
                            AppPreferences.number=number.toString()
                            AppPreferences.diocese=diocese.toString()
                            AppPreferences.email=em
                            AppPreferences.url=""
                            dioceseForum="$diocese Diocese Music Forum"
                            foum=MyForums(mainForum)
                            nn=MyForums(dioceseForum)
                            CheckIfUserInforums()
                            progress.dismiss()

                            val i= Intent(applicationContext,DashActivity::class.java)
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(i)
                            finishAffinity()
                            finish()
                        }
                    }

                }
                else{
                    progress.dismiss()
                    password.setError("Wrong PassWord")
                }

            }
            else {
                progress.dismiss()
                val snackBar = Snackbar.make(binding.root, "Wrong Email or Password!!", Snackbar.LENGTH_LONG)
                snackBar.show()
            }
        }
                .addOnFailureListener {
                    progress.dismiss()
                    Toast.makeText(this,"Check your Network",Toast.LENGTH_LONG)
                }
    }
 fun  CheckIfUserInforums(){

     db.collection("users").document(em)
             .collection("myforums").get().addOnSuccessListener {
                 result->
                 if (result.isEmpty){
                     db.collection("users").document(em)
                             .collection("myforums").document().set(foum)

                     db.collection("users").document(em)
                             .collection("myforums").document().set(nn)
                     val snackBar = Snackbar.make(binding.root, "Login Successful", Snackbar.LENGTH_LONG)
                     snackBar.show()
                 }
                 else
                 {
                     val snackBar = Snackbar.make(binding.root, "Welcome You Are Already Registered To forums", Snackbar.LENGTH_LONG)
                     snackBar.show()
                 }
             }

 }
}