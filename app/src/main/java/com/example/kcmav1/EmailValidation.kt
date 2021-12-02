package com.example.kcmav1

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.kcmav1.utils.toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class EmailValidation : AppCompatActivity() {
    lateinit var    mGoogleSignInClient:GoogleSignInClient
    lateinit var option:Bundle
    lateinit var sign:Button
    lateinit var t_email:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_validation)
        option= Bundle()
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
         mGoogleSignInClient= GoogleSignIn.getClient(this, gso);
        sign=findViewById(R.id.sign);
        t_email=findViewById(R.id.t_email)
        sign.setOnClickListener {
            val signInIntent:Intent= mGoogleSignInClient.signInIntent

            ActivityCompat.startActivityForResult(this,signInIntent,100,option)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 100) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task =GoogleSignIn.getSignedInAccountFromIntent(intent)
                      handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>?) {

        try {
            val account: GoogleSignInAccount = completedTask!!.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            val personName: String = account.displayName
            val personGivenName: String = account.givenName
            val personFamilyName: String = account.familyName
            val personEmail: String = account.email
            val personId: String = account.id
            val personPhoto: Uri =account.photoUrl
           t_email.setText(personEmail+personGivenName+personId+"")
        }
    }
}