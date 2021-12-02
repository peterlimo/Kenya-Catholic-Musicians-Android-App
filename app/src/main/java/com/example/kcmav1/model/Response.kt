package com.example.kcmav1.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Response (val sender:String,
                     val message:String,
                     val date:String,
                     @ServerTimestamp val timeAt:Date?=null)