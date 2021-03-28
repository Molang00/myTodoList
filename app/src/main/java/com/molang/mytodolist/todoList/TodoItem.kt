package com.molang.mytodolist.todoList

import io.realm.RealmObject
import java.util.*

open class TodoItem(
    var checked: Boolean = false,
    var label: String = "",
    var targetDate: String = "",
    var created: Date = Date(),
    var updated: Date = Date()
): RealmObject () {

    override fun toString(): String {
        return "TodoItem(checked=$checked, label='$label', targetDate=$targetDate, created=$created, updated=$updated)"
    }
}