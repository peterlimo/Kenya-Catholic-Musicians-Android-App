

package com.example.kcmav1

import android.app.ProgressDialog
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.kcmav1.model.UploadSongData
import com.example.kcmav1.utils.AppPreferences
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.File

class UploadYourScriptsActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {
    lateinit var  select_pdf: Button
    lateinit var upload_song: Button
    lateinit var song_name: TextView
    lateinit var song_title:EditText
    lateinit var song_choir:EditText
    lateinit var song_composer:EditText
    lateinit var mstoragerf: StorageReference
    lateinit var uri: Uri
    lateinit var fileurl:String
    lateinit var real_file_uri:String
    lateinit var path:String
    lateinit var user:String
    lateinit var type:Spinner
    lateinit var my_toolbar: Toolbar
    lateinit var displayName:String
    lateinit var selectedDio:String
   lateinit var  progrss:ProgressDialog
    lateinit var dataAdapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_your_scripts)
        progrss= ProgressDialog(this)
        select_pdf=findViewById(R.id.select_pdf)
        upload_song=findViewById(R.id.upload_song_button)
        song_name=findViewById(R.id.song_name)
        song_title=findViewById(R.id.upload_song_title)
        song_choir=findViewById(R.id.upload_song_singers)
        song_composer=findViewById(R.id.upload_song_composer)
        type=findViewById(R.id.type_spinner)

        my_toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(my_toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        type.onItemSelectedListener=this
        AppPreferences.init(this)
        if(AppPreferences.isLogin){
            user= AppPreferences.username
        }

        val progress=ProgressDialog(this)
        progress.setTitle("Loading....")
        progress.show()
        val dio =ArrayList<String>()
        dataAdapter  = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,dio )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        type.adapter = dataAdapter
        val dd=FirebaseFirestore.getInstance()
        dd.collection("files").get().addOnSuccessListener { result->
            progress.dismiss()
            for (documents in result){
                if(documents.exists()){
                    dio.add(documents.id)
                }
            }
            dataAdapter.notifyDataSetChanged()

        }

        //declaring the firebase instance
        mstoragerf= FirebaseStorage.getInstance().getReference("songs")
        select_pdf.setOnClickListener {
            val intent = Intent()
            intent.type = "application*/pdf"
            intent.action = Intent.ACTION_GET_CONTENT
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, 1212)
        }


        upload_song.setOnClickListener {
            upload()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode==1212 && resultCode== RESULT_OK &&data!=null &&data.data!=null){
        uri= data.data!!
                real_file_uri=uri.lastPathSegment.toString()
            val file=File(uri.toString())
            if (uri.toString().startsWith("content://")){
                var cursor: Cursor? = null
                try {
                    cursor = this.getContentResolver().query(uri, null, null, null, null)
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                } finally {
                    cursor?.close()
                }
            }
            else if (uri.toString().startsWith("file://")){
displayName=file.name
            }
                song_name.text= displayName
                song_title.setText(displayName)

        }

        super.onActivityResult(requestCode, resultCode, data)
    }

   fun upload(){
        var mRefrence= mstoragerf.child(uri.lastPathSegment.toString())
       progrss.setTitle("Uploading document!!")
       progrss.show()
        try{
            mRefrence.putFile(uri).addOnSuccessListener {
                taskSnapshot: UploadTask.TaskSnapshot? -> fileurl=taskSnapshot!!.uploadSessionUri.toString()

            mRefrence.downloadUrl.addOnSuccessListener {
                saveSongData(it.toString())
                }
            }.addOnFailureListener{
                progrss.dismiss()
            }.addOnProgressListener {p->

        val progess:Double=(100.0*p.bytesTransferred)/p.totalByteCount
                progrss.setMessage("Uploading.....${progess.toInt()}%")
            }
        }
        catch (e: Exception){
            Toast.makeText(this,e.toString(), Toast.LENGTH_LONG).show()
        }

    }

    private fun saveSongData(songUrl: String) {

        val db = FirebaseFirestore.getInstance()
        val song = UploadSongData(song_title.text.toString().toLowerCase(), user,songUrl, song_composer.text.toString(), song_choir.text.toString(),selectedDio,0,0,"no")

        db.collection("files").document(selectedDio).collection("songs").document().set(song)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Uploaded Successfully!!", Toast.LENGTH_LONG).show()
                    finish()
                    progrss.dismiss()

                }
                .addOnFailureListener {
                    progrss.dismiss()
                }
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedDio=parent?.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}
