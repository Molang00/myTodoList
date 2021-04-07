package com.molang.mytodolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.molang.mytodolist.todoList.*
import com.molang.mytodolist.util.getLastLoginTimestamp
import com.molang.mytodolist.util.initPref
import com.molang.mytodolist.util.setLastLoginTimestamp
import io.realm.*
import java.text.SimpleDateFormat
import java.util.*

class SplashActivity : AppCompatActivity() {
    private var TIME_OUT:Long = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initRealm()
        initPref(this)

        updateDailyTodoItemByLastLoginTimestamp()
        updateWorkTodoItemByLastLoginTimestamp()
        setLastLoginTimestamp()
        startMainActivity()
    }

    private fun initRealm() {
        Realm.init(this)

        val config = RealmConfiguration.Builder()
                .schemaVersion(1)
                .migration { realm, oldVersion, newVersion ->
                    val realmSchema = realm.schema
//                    if(oldVersion < 2L) {
                    val mTodoItemSchema = realmSchema.get("TodoItem")
                    mTodoItemSchema?.addField("type", String::class.java, FieldAttribute.REQUIRED)
//                    }
                }
                .build()
        Realm.setDefaultConfiguration(config)

        initTodoItemRealmManager(Realm.getDefaultInstance())
    }

    private fun updateDailyTodoItemByLastLoginTimestamp() {
        val currentCalendar = Calendar.getInstance()
        currentCalendar.set(Calendar.HOUR_OF_DAY, 0)
        currentCalendar.set(Calendar.MINUTE, 0)
        currentCalendar.set(Calendar.SECOND, 0)
        currentCalendar.set(Calendar.MILLISECOND, 0)
        var lastLoginTimeStampCalendar = Calendar.getInstance()
        lastLoginTimeStampCalendar.time = getLastLoginTimestamp()
        
        while (currentCalendar.time.after(lastLoginTimeStampCalendar.time)) {
            val dailyTodoItemsAtYesterday = getTodoItemRealmManagerInstance()
                    .findAllByTargetDateAndType(
                            SimpleDateFormat(targetDateFormat)
                                    .format(lastLoginTimeStampCalendar.time),
                            DAILY_TYPE
                    )
            lastLoginTimeStampCalendar.add(Calendar.DATE, 1)
            getTodoItemRealmManagerInstance().createAll(
                    dailyTodoItemsAtYesterday
                            .map{
                                TodoItem(
                                        checked = false,
                                        type = it.type,
                                        label = it.label,
                                        targetDate = SimpleDateFormat(targetDateFormat)
                                                .format(lastLoginTimeStampCalendar.time)
                                )
                            }
            )
        }
    }

    private fun updateWorkTodoItemByLastLoginTimestamp() {
        val currentCalendar = Calendar.getInstance()
        currentCalendar.set(Calendar.HOUR_OF_DAY, 0)
        currentCalendar.set(Calendar.MINUTE, 0)
        currentCalendar.set(Calendar.SECOND, 0)
        currentCalendar.set(Calendar.MILLISECOND, 0)
        var lastLoginTimeStampCalendar = Calendar.getInstance()
        lastLoginTimeStampCalendar.time = getLastLoginTimestamp()

        if (currentCalendar.time.after(lastLoginTimeStampCalendar.time)) {
            val dailyTodoItemsAtYesterday = getTodoItemRealmManagerInstance()
                    .findAllByTargetDateAndTypeAndChecked(
                            SimpleDateFormat(targetDateFormat)
                                    .format(
                                            Date(currentCalendar.time.time - 24*60*60*1000)
                                    ),
                            WORK_TYPE,
                            false
                    )
            getTodoItemRealmManagerInstance().createAll(
                    dailyTodoItemsAtYesterday
                            .map{
                                TodoItem(
                                        checked = false,
                                        type = it.type,
                                        label = it.label,
                                        targetDate = SimpleDateFormat(targetDateFormat)
                                                .format(currentCalendar.time)
                                )
                            }
            )
        }
    }

    private fun startMainActivity() {
        Handler().postDelayed({
            // You can declare your desire activity here to open after finishing splash screen. Like MainActivity
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        },TIME_OUT)
    }
}