package com.example.kcmav1

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import com.example.kcmav1.dashboard.forums.ForumFragment
import com.example.kcmav1.dashboard.hire.HireActivity
import com.example.kcmav1.dashboard.search.HomeFragment
import com.example.kcmav1.learn.LearnerDashWelcome
import com.example.kcmav1.learn.LearnerDashboard
import com.example.kcmav1.utils.AppPreferences
import com.example.kcmav1.utils.toast
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class DashActivity : AppCompatActivity() {
    lateinit var open_hire:MaterialCardView
    lateinit var u_email:String
    lateinit var dash_toolbar:Toolbar
    lateinit var open_forums_activity:MaterialCardView
    lateinit var open_learn:MaterialCardView
    lateinit var db:FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(R.layout.activity_dash)
        AppPreferences.init(this)
        db= FirebaseFirestore.getInstance()
        open_hire=findViewById(R.id.open_hire)
        val open_search=findViewById<MaterialCardView>(R.id.open_search)
        open_forums_activity=findViewById(R.id.open_forums_activity)
        open_learn=findViewById(R.id.open_learn)
        dash_toolbar=findViewById(R.id.dash_toolbar)
        val open_upload=findViewById<MaterialCardView>(R.id.open_downloads)
        setSupportActionBar(dash_toolbar)
        if (AppPreferences.isLogin) {
            u_email=AppPreferences.email
        }
        open_upload.setOnClickListener {
            val i=Intent(applicationContext,UploadYourScriptsActivity::class.java)
            startActivity(i)
        }
   open_search.setOnClickListener {
           val i=Intent(applicationContext,HomeFragment::class.java)
         startActivity(i)
       }
        open_learn.setOnClickListener {
            checkLearner()
        }
        open_forums_activity.setOnClickListener {
            val i=Intent(applicationContext,ForumFragment::class.java)
            startActivity(i)
        }
        open_hire.setOnClickListener {
            val i=Intent(applicationContext,HireActivity::class.java)
            startActivity(i)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dash_menu,menu)
        val menu1:Menu=dash_toolbar.menu
        val item: MenuItem = menu1.findItem(R.id.menu_item1)
            val view:View=MenuItemCompat.getActionView(item)
            val profile:CircleImageView=view.findViewById(R.id.profile_image)
       if (AppPreferences.url.isNotEmpty())
       {
           Picasso.get().load(AppPreferences.url).into(profile)
       }
            profile.setOnClickListener {
                val i=Intent(applicationContext,UserProfileFragment::class.java)
                startActivity(i)
            }

        return super.onCreateOptionsMenu(menu)
    }


    private fun checkLearner(){
        val pd= ProgressDialog(this)
        pd.show()
        pd.setMessage("Please Wait")
        db.collection("learn")
            .document("students")
            .collection("student")
            .document(u_email).get().addOnSuccessListener {
                if (it.exists())
                {
                    pd.dismiss()
                    startActivity(Intent(applicationContext, LearnerDashboard::class.java))
                }
                else
                {
                    pd.dismiss()
                    toast("Get started to enjoy our learning tutorials")
                    startActivity(Intent(applicationContext, LearnerDashWelcome::class.java))
                }
            }
    }

}