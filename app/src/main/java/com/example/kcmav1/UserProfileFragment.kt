package com.example.kcmav1

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.kcmav1.model.Edit
import com.example.kcmav1.model.Hire
import com.example.kcmav1.utils.AppPreferences
import com.example.kcmav1.utils.toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class UserProfileFragment : AppCompatActivity() ,View.OnClickListener{
    lateinit var text_email:TextView
    lateinit var text_name:TextView
    lateinit var text_number:TextView
    lateinit var text_diocese:TextView
    lateinit var profile_church:TextView
    lateinit var my_toolbar:Toolbar
    lateinit var logout:Button
    lateinit var select_profile:CircleImageView
    lateinit var mstoragerf: StorageReference
    lateinit var uri: Uri
    lateinit var fileurl:String
    lateinit var db:FirebaseFirestore
    lateinit var  progrss:ProgressDialog
    lateinit var u_email:String
    lateinit var edt_name:ImageView
    lateinit var edt_profile:ImageView
    lateinit var edt_number:ImageView
    lateinit var edt_church:ImageView
    lateinit var hire_btn:Button
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_user_profile)
        my_toolbar=findViewById(R.id.prof_toolbar)
        setSupportActionBar(my_toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        AppPreferences.init(this)
        progrss= ProgressDialog(this)
        text_email = findViewById(R.id.profile_email)
        text_name = findViewById(R.id.profile_username)
        text_number = findViewById(R.id.profile_number)
        text_diocese = findViewById(R.id.profile_diocese)
        profile_church=findViewById(R.id.profile_church)
        edt_name=findViewById(R.id.edit_username)
        edt_number=findViewById(R.id.edit_number)
        edt_profile=findViewById(R.id.edit_profile_photo)
        edt_church=findViewById(R.id.edit_church)
        edt_profile.setOnClickListener(this)
        edt_number.setOnClickListener(this)
        edt_name.setOnClickListener(this)
        edt_church.setOnClickListener(this)
        logout=findViewById(R.id.logout_user)
        select_profile=findViewById(R.id.profile_image)
        hire_btn=findViewById(R.id.available_for_hire_button)
        hire_btn.setOnClickListener(this)
        db= FirebaseFirestore.getInstance()
        setDataTofields()

        logout.setOnClickListener {
            AppPreferences.logOut()
            AppPreferences.isLogin == false
            Toast.makeText(applicationContext, "Logged out successfully", Toast.LENGTH_LONG).show()
            val i = Intent(applicationContext, LoginActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
            finishAffinity()
            finish()
        }
    }

    private fun selectProfileImage() {
        val intent = Intent()
        intent.type = "image/jpg"
        intent.action = Intent.ACTION_PICK
        startActivityForResult(intent, 1212)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1212 && resultCode== RESULT_OK &&data!=null &&data.data!=null){
            uri= data.data!!
            uploadProfileImage(uri)
        }
    }


    private fun uploadProfileImage(filePath: Uri) {
        mstoragerf= FirebaseStorage.getInstance().getReference("profile")
        var mRefrence= mstoragerf.child(uri.lastPathSegment.toString())
        progrss.setTitle("Loading image")
        progrss.show()
        try{
            mRefrence.putFile(filePath).addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? -> fileurl=taskSnapshot!!.uploadSessionUri.toString()

                mRefrence.downloadUrl.addOnSuccessListener {
                    progrss.dismiss()
                    saveProfileData(it.toString())
                }
            }.addOnFailureListener{
                progrss.dismiss()
            }
                    .addOnProgressListener { p->

                        val progess:Double=(100.0*p.bytesTransferred)/p.totalByteCount
                        progrss.setMessage("Uploading.....${progess.toInt()}%")
                    }
        }
        catch (e: Exception){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }

    }
    private fun saveProfileData(url: String) {
        val data=HashMap<String, Any>()
        data["url"]=url
        db.collection("users")
                .document(u_email)
                .collection("profile")
                .document("full")
                .set(data)
                .addOnSuccessListener {
                    AppPreferences.url=url
                    setDataTofields()
                    progrss.dismiss()
                    toast("Profile Image successfully Added!")
                }
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.edit_profile_photo -> {
                selectProfileImage()
            }
            R.id.edit_number -> {
                showEditSheet(AppPreferences.number, "number")
            }
            R.id.edit_username -> {
                showEditSheet(AppPreferences.username, "name")
            }
            R.id.edit_church->
            {
                showEditSheet(AppPreferences.church, "church")
            }
            R.id.available_for_hire_button->
            {
                showHireSheet()
            }
        }
    }

    private fun showHireSheet() {
        val bottomSheet = BottomSheetDialog(this)
        bottomSheet.setContentView(R.layout.hire_bottom_sheet)
        val c_desc = bottomSheet.findViewById<EditText>(R.id.c_desc)
        val c_church = bottomSheet.findViewById<TextView>(R.id.c_church)
        val c_email = bottomSheet.findViewById<TextView>(R.id.c_email)
        val c_done = bottomSheet.findViewById<Button>(R.id.c_done)
        c_church!!.text = AppPreferences.church
        c_email!!.text = AppPreferences.email
        c_done!!.setOnClickListener {
            val hire = Hire(AppPreferences.email, AppPreferences.church, c_desc!!.text.toString())
            db.collection("hires")
                    .document()
                    .set(hire)
                    .addOnSuccessListener {
                        bottomSheet.dismiss()
                        toast("Request Successful")
                    }
        }
        bottomSheet.setCancelable(true)
        bottomSheet.show()
    }
    private fun showEditSheet(value: String, key: String) {
val bottomSheet= BottomSheetDialog(this)
        bottomSheet.setContentView(R.layout.edit_bottom_sheeet)
        val edt=bottomSheet.findViewById<EditText>(R.id.edit_text)
        val dn=bottomSheet.findViewById<Button>(R.id.done_btn)
        if (value!=null) {
            edt!!.setText(value)
        }
        dn!!.setOnClickListener {
            var kee=""
            val user: MutableMap<String, Any> = HashMap()
            if (key == "number") {
                kee="number"
            }
            else if (key == "church")
            {
              kee="church"
            }
            else{
                kee="name"
            }
            AppPreferences.update( edt!!.text.toString(),kee)
            user[kee] = edt!!.text.toString();
            db.collection("users")
                    .document(u_email)
                    .update(user).addOnSuccessListener {
                        bottomSheet.dismiss()
                        toast("Details Updated Successfully!!")
                        setDataTofields()
                    }
                    .addOnFailureListener {
                        toast("Failed to update details!!")
                    }
        }
        bottomSheet.setCancelable(true)
        bottomSheet.show()
    }
   fun setDataTofields()
    {
        if (AppPreferences.isLogin) {
            text_email.text =  AppPreferences.email
            text_number.text = AppPreferences.number
            text_name.text = AppPreferences.username
            text_diocese.text = AppPreferences.diocese
            u_email=AppPreferences.email
        }
        if (AppPreferences.church.isEmpty())
        {
            profile_church.text="Please Add Current Church"
        }
        else
        {
            profile_church.text=AppPreferences.church
        }
        if (AppPreferences.url.isNotEmpty())
        {
            Picasso.get().load(AppPreferences.url).into(select_profile)
        }
        else{
            toast("Please add Profile Picture!")
        }
    }
}






