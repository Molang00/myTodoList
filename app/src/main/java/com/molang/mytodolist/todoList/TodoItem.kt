package com.molang.mytodolist.todoList

import io.realm.RealmObject
import java.util.*

const val COMMON_TYPE = "common"
const val WORK_TYPE = "work"
const val DAILY_TYPE = "daily"

open class TodoItem(
    var checked: Boolean = false,
    var type: String = COMMON_TYPE,
    var label: String = "",
    var targetDate: String = "",
    var created: Date = Date(),
    var updated: Date = Date()
): RealmObject () {
    constructor(todoItem: TodoItem)
            : this(
            todoItem.checked,
            todoItem.type,
            todoItem.label,
            todoItem.targetDate,
            todoItem.created,
            todoItem.updated
    )

    override fun toString(): String {
        return "TodoItem(checked=$checked, type='$type', label='$label', targetDate='$targetDate', created=$created, updated=$updated)"
    }

}