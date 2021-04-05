package com.molang.mytodolist.util

import android.content.Context
import android.content.SharedPreferences
import java.util.*

lateinit var pref: SharedPreferences
lateinit var editor: SharedPreferences.Editor

fun initPref(ctx: Context) {
    pref = ctx.applicationContext.getSharedPreferences("MyTodoListPref", Context.MODE_PRIVATE)
    editor = pref.edit()
}

const val LAST_LOGIN_TIMESTAMP = "last_login_timestamp"
fun setLastLoginTimestamp() {
    editor.putLong(LAST_LOGIN_TIMESTAMP, Date().time)
    editor.apply()
}
fun getLastLoginTimestamp(): Date {
    return Date(pref.getLong(LAST_LOGIN_TIMESTAMP, 0))
}