package com.molang.mytodolist

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.molang.mytodolist.todoList.*
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var mainActivity: MainActivity
    lateinit var todoItemRealmManager: TodoItemRealmManager

    lateinit var targetDate: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainActivity = this
        todoItemRealmManager = TodoItemRealmManager(Realm.getDefaultInstance())

        targetDate = Date()
        initUI()
    }

    private fun initUI() {
        setInitValues()
        setTodoRecyclerView()
        calculateRateAndCnt()
        setOnClickListeners()
    }

    private fun setInitValues() {
        tv_target_date.text = SimpleDateFormat("yyyy년 M월 d일").format(targetDate)
    }

    val todoList = ArrayList<TodoItem>()
    lateinit var todoListAdapter: TodoListAdapter
    private fun setTodoRecyclerView() {
        todoList.clear()
        todoList.addAll(
            todoItemRealmManager
                .findAllByTargetDate(
                    tv_target_date
                        .text
                        .toString()
                )
        )

        todoListAdapter = TodoListAdapter(
            this,
            todoList
        )
        rv_todo_list.adapter = todoListAdapter
    }

    fun setOnClickListeners() {
        tv_prev_date.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                targetDate = Date(targetDate.time-24*60*60*1000)
                initUI()
            }
        })
        tv_next_date.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                targetDate = Date(targetDate.time+24*60*60*1000)
                initUI()
            }
        })
        btn_create_todo_item.setOnClickListener(object: View.OnClickListener{
            @SuppressLint("ShowToast")
            override fun onClick(v: View?) {

                val todoItemLabel = et_new_todo_item.text.toString()
                Log.i("onClickListener", "+$todoItemLabel+")
                if(todoItemLabel == "") {
                    Toast.makeText(mainActivity, "내용을 채워주세요", Toast.LENGTH_SHORT).show()
                    return
                }

                val newTodoItem = TodoItem(
                                    checked = false,
                                    label = todoItemLabel,
                                    targetDate = tv_target_date.text.toString()
                                )
                et_new_todo_item.text.clear()
                notify(ADD_TODO_ITEM, newTodoItem)
            }
        })
    }

    fun notify(code: String, item: TodoItem) {
        when(code){
            ADD_TODO_ITEM -> {
                todoItemRealmManager.create(item)
                todoList.add(item)
                todoListAdapter.notifyDataSetChanged()
            }
            DELETE_TODO_ITEM -> {
                todoList.remove(item)
                todoListAdapter.notifyDataSetChanged()
                todoItemRealmManager.delete(item)
            }
            CHECK_TODO_ITEM ->
                todoItemRealmManager.updateChecked(item, true)
            UNCHECK_TODO_ITEM ->
                todoItemRealmManager.updateChecked(item, false)
        }
        calculateRateAndCnt()
    }

    @SuppressLint("SetTextI18n")
    private fun calculateRateAndCnt() {
        val doneCnt = todoList.filter { it.checked }.size
        val totalCnt = todoList.size
        val doneRate = 1.0*doneCnt/totalCnt * 100
        tv_done_cnt.text = doneCnt.toString()
        tv_total_cnt.text = totalCnt.toString()
        if(totalCnt == 0)
            tv_done_rate.text = "-"
        else
            tv_done_rate.text = String.format("%.2f", doneRate) + "%"

        if(doneRate == 100.0)
            tv_cheerup.text = "You've done everything!!"
        else
            tv_cheerup.text = "You can do it!"
    }
}