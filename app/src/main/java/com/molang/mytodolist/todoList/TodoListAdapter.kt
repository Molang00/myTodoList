package com.molang.mytodolist.todoList

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.molang.mytodolist.MainActivity
import com.molang.mytodolist.R

const val DELETE_TODO_ITEM = "delete_todo_item"
const val ADD_TODO_ITEM = "add_todo_item"
const val CHECK_TODO_ITEM = "check_todo_item"
const val UNCHECK_TODO_ITEM = "uncheck_todo_item"

class TodoListAdapter(
    val act: MainActivity,
    val todoList: ArrayList<TodoItem>
): RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        lateinit var data: TodoItem
        val checkBox = view.findViewById<CheckBox>(R.id.cb_check_box)
        val tv_todo = view.findViewById<TextView>(R.id.tv_todo_label)
        val tv_delete = view.findViewById<TextView>(R.id.tv_delete)

        fun onBind(_data: TodoItem) {
            data = _data
            checkBox.isChecked = data.checked
            tv_todo.text = data.label
            tv_delete.setOnClickListener {
                act.notify(DELETE_TODO_ITEM, data)
            }
            checkBox.setOnCheckedChangeListener{it, isChecked ->
                if(isChecked)
                    act.notify(CHECK_TODO_ITEM, data)
                else
                    act.notify(UNCHECK_TODO_ITEM, data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.item_todo_thing,
                parent,
                false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = todoList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(todoList[position])
    }
}