package com.example.kcmav1.model

import android.widget.ImageView
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Message (val mesid:String,
                    val text:String,
                    var sender:String,
                    var viewType:Int,
                    var time:String,
                    val diocese:String,
                    val mdy: String,
                    val gid:String,
                    val loves:String,
                    val responses:String,
                    val isLike:Boolean,
                    val isUnLike:Boolean,
                    val url:String,
                    val senderProf:String


)