
package com.example.kcmav1.utils

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast

object AppPreferences {
    private const val NAME = "user"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    //SharedPreferences variables
    private val IS_LOGIN = Pair("is_login", false)
    private val EMAIL = Pair("email", "")
    private val PASSWORD = Pair("password", "")
    private val USERNAME = Pair("name", "")
    private val DIOCESE = Pair("diocese", "")
    private val NUMBER = Pair("number", "")
    private val URL = Pair("url", "")
    private val CHURCH = Pair("church", "")
    private val HIRERABLE= Pair("hire", "")

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    //an inline function to put variable and save it
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    //SharedPreferences variables getters/setters
    var isLogin: Boolean
        get() = preferences.getBoolean(IS_LOGIN.first, IS_LOGIN.second)
        set(value) = preferences.edit {
            it.putBoolean(IS_LOGIN.first, value)
        }
    fun update(item: String,key:String)
    {
        when (key) {
            "name" -> {
                username = item
            }
            "church" -> {
                church=item
            }
            else -> {
                number=item
            }
        }
    }
        fun logOut(){
    preferences.edit().clear().apply()
                }
    var email: String
        get() = preferences.getString(EMAIL.first, EMAIL.second) ?: ""
        set(value) = preferences.edit {
            it.putString(EMAIL.first, value)
        }

    var password: String
        get() = preferences.getString(PASSWORD.first, PASSWORD.second) ?: ""
        set(value) = preferences.edit {
            it.putString(PASSWORD.first, value)
        }
    var diocese: String
        get() = preferences.getString(DIOCESE.first, DIOCESE.second) ?: ""
        set(value) = preferences.edit {
            it.putString(DIOCESE.first, value)
        }
    var number: String
        get() = preferences.getString(NUMBER.first, NUMBER.second) ?: ""
        set(value) = preferences.edit {
            it.putString(NUMBER.first, value)
        }
    var username: String
        get() = preferences.getString(USERNAME.first, USERNAME.second) ?: ""
        set(value) = preferences.edit {
            it.putString(USERNAME.first, value)
        }
    var url: String
        get() = preferences.getString(URL.first, URL.second) ?: ""
        set(value) = preferences.edit {
            it.putString(URL.first, value)
        }
    var church: String
        get() = preferences.getString(CHURCH.first, CHURCH.second) ?: ""
        set(value) = preferences.edit {
            it.putString(CHURCH.first, value)
        }
    var hire: String
        get() = preferences.getString(HIRERABLE.first, HIRERABLE.second) ?: ""
        set(value) = preferences.edit {
            it.putString(HIRERABLE.first, value)
        }
}