package com.example.kcmav1

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.kcmav1.databinding.ActivityMainBinding
import com.example.kcmav1.utils.AppPreferences
import com.example.kcmav1.viewModels.LoginViewModel


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        else{
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        val viewModel = ViewModelProviders.of(this)
            .get(LoginViewModel::class.java)
        binding.viewModel = viewModel
        AppPreferences.init(this)
        if (AppPreferences.isLogin){
            val i=Intent(this,DashActivity::class.java)
            startActivity(i)
            finish()
        }
        else
        {
            Toast.makeText(applicationContext,"No User Logged in",Toast.LENGTH_LONG)
        }
    }

fun test(){



}

}