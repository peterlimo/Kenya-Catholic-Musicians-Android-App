package com.example.kcmav1.learn

import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kcmav1.R
import com.github.barteksc.pdfviewer.PDFView
import com.google.firebase.firestore.FirebaseFirestore
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class ViewPdfActivity : AppCompatActivity() {
    lateinit var db:FirebaseFirestore
    lateinit var pdf_url:String
    lateinit var pdfView:PDFView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pdf)
        pdfView=findViewById(R.id.pdfView)
        db= FirebaseFirestore.getInstance()
        db.collection("learn")
            .document("pdfmaterials")
            .collection("beginner")
            .document("grade1")
            .get()
            .addOnSuccessListener { 
                pdf_url=it.get("url").toString()
               val m= RetrivedPdffromFirebase()
                m.execute(pdf_url)
            }
    }
   inner class RetrivedPdffromFirebase :AsyncTask<String, Void, InputStream>() {
        // we are calling async task and performing
        // this task to load pdf in background.


        override fun onPostExecute(inputStream: InputStream) {
            // after loading stream we are setting
            // the pdf in your pdf view.
            pdfView.fromStream(inputStream).load()
        }

        override fun doInBackground(vararg params: String): InputStream? {
            // below line is for declaring
            // our input stream.
            var pdfStream: InputStream? = null
            try {
                // creating a new URL and passing
                // our string in it.
                val url = URL(params[0])

                // creating a new http url connection and calling open
                // connection method to open http url connection.
                val httpURLConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
                if (httpURLConnection.responseCode === 200) {
                    // if the connection is successful then
                    // we are getting response code as 200.
                    // after the connection is successful
                    // we are passing our pdf file from url
                    // in our pdfstream.
                    pdfStream = BufferedInputStream(httpURLConnection.inputStream)
                }
            } catch (e: IOException) {
                // this method is
                // called to handle errors.
                return null
            }
            // returning our stream
            // of PDF file.
            return pdfStream
        }
    }
}