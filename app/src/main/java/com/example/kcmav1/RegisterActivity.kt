package com.example.kcmav1

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.kcmav1.databinding.ActivityRegisterBinding
import com.example.kcmav1.model.User
import com.example.kcmav1.utils.isEmpty
import com.example.kcmav1.utils.toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore


class RegisterActivity : AppCompatActivity() ,AdapterView.OnItemSelectedListener{
    lateinit var db:FirebaseFirestore
    lateinit var email1:TextInputEditText
    lateinit var username:TextInputEditText
    lateinit var phone_no:TextInputEditText
    lateinit var password:TextInputEditText
    lateinit var binding:ActivityRegisterBinding
    lateinit var spinner: Spinner
    lateinit var selectedDio:String
    lateinit var dataAdapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        spinner=findViewById(R.id.diocese_spinner)

        spinner.prompt="Choose Diocese"

        spinner.onItemSelectedListener=this
        val progress=ProgressDialog(this)
        progress.setTitle("Please wait!!")
        progress.setMessage("Getting Dioceses...")
        progress.show()
        val dio =ArrayList<String>()
        dataAdapter  = ArrayAdapter(this, android.R.layout.simple_spinner_item,dio )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = dataAdapter
        val dd=FirebaseFirestore.getInstance()
        dd.collection("dioceses").get().addOnSuccessListener { result->
            progress.dismiss()
            for (documents in result){
                if(documents.exists()){
                   dio.add(documents.get("name").toString())
                }
            }
dataAdapter.notifyDataSetChanged()

        }
        binding.signUp.setOnClickListener {
            validateData()
        }
    }

     fun validateData() {
        username=findViewById(R.id.username)
        phone_no=findViewById(R.id.person_number)
        email1=findViewById(R.id.person_email)
        password=findViewById(R.id.person_password)

        var isdatavalid=true
        if (isEmpty(binding.username)){
            username.setError("Name is required!")
            isdatavalid=false
        }
        if (isEmpty(phone_no)){
            phone_no.setError("Phone number is required!")
            isdatavalid=false
        }
        if (isEmpty(email1)){
            email1.setError("Email is required!")
            isdatavalid=false
        }
        if (isEmpty(password)){
            password.setError("Password is required!")
            isdatavalid=false
        }
        if (isdatavalid){
            SubmitData()
        }
    }



    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedDio=parent?.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        toast("Please select diocese")
    }

   fun SubmitData()
   {
        db= FirebaseFirestore.getInstance()

            val user=User(binding.username.text.toString(), binding.personNumber.text.toString(), binding.personEmail.text.toString(), binding.personPassword.text.toString(),selectedDio,"")
            db.collection("users").document(binding.personEmail.text.toString()).set(user).addOnCompleteListener {

//                Statistics.increment("no_of_users")
            val snackBar = Snackbar.make(binding.root, "Registered Successfully, Proceed to Login!!", Snackbar.LENGTH_LONG)
            snackBar.show()
            val i=Intent(applicationContext, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
                .addOnFailureListener(this) {
                    val snackBar = Snackbar.make(binding.root, "Registration Failed", Snackbar.LENGTH_LONG)
                    snackBar.show()
                }

   }

}