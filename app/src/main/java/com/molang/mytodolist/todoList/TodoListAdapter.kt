package com.molang.mytodolist.todoList

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.molang.mytodolist.MainActivity
import com.molang.mytodolist.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

const val DELETE_TODO_ITEM = "delete_todo_item"
const val ADD_TODO_ITEM = "add_todo_item"
const val CHANGED_TODO_ITEM = "changed_todo_item"

class TodoListAdapter(
    val act: MainActivity,
    val todoList: ArrayList<TodoItem>
): RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {
    val todoTypes =
            Arrays.asList(
                    COMMON_TYPE,
                    DAILY_TYPE,
                    WORK_TYPE
            )

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        lateinit var data: TodoItem
        val clMainInfo = view.findViewById<ConstraintLayout>(R.id.cl_main_info)
        val checkBox = view.findViewById<CheckBox>(R.id.cb_check_box)
        val tvTodoLabel = view.findViewById<TextView>(R.id.tv_todo_label)
        val tvDelete = view.findViewById<TextView>(R.id.tv_delete)
        val clDetailInfo = view.findViewById<ConstraintLayout>(R.id.cl_detail_info)
        val tvUpdatedValue = view.findViewById<TextView>(R.id.tv_updated_value)
        val spTypeValue = view.findViewById<Spinner>(R.id.sp_type_value)

        @SuppressLint("SimpleDateFormat")
        fun onBind(_data: TodoItem) {
            data = _data

            checkBox.isChecked = data.checked
            tvTodoLabel.text = data.label
            tvUpdatedValue.text =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            .format(data.updated)

            clMainInfo.setOnClickListener {
                if(clDetailInfo.visibility == View.VISIBLE)
                    clDetailInfo.visibility = View.GONE
                else clDetailInfo.visibility = View.VISIBLE
            }

            tvDelete.setOnClickListener {
                act.notify(DELETE_TODO_ITEM, data)
            }

            checkBox.setOnCheckedChangeListener{it, isChecked ->
                data.checked = isChecked
                act.notify(CHANGED_TODO_ITEM, data)
            }

            ArrayAdapter<String>(
                    act,
                    android.R.layout.simple_spinner_item,
                    todoTypes
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spTypeValue.adapter = adapter
                spTypeValue.setSelection(adapter.getPosition(data.type))
            }
            spTypeValue.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
//                    TODO("Not yet implemented")
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                    TODO("Not yet implemented")
                    data.type = spTypeValue.getItemAtPosition(position) as String
                    act.notify(CHANGED_TODO_ITEM, data)
                }
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