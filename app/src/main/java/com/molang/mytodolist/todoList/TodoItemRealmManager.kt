package com.molang.mytodolist.todoList

import com.molang.mytodolist.todoList.TodoItem
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults
import java.util.*

class TodoItemRealmManager(
    val realm: Realm
) {

    fun findAllByTargetDate(targetDate: String): List<TodoItem> {
        return realm.where(TodoItem::class.java)
            .equalTo("targetDate", targetDate)
            .findAll()
            .sort("created")
    }

    fun findByItem(target: TodoItem): TodoItem? {
        return realm.where(TodoItem::class.java)
            .equalTo("label", target.label)
            .equalTo("created", target.created)
            .findFirst()
    }

    fun create(newTodoItem: TodoItem): TodoItem {
        realm.beginTransaction()
        realm.insert(newTodoItem)
        realm.commitTransaction()
        return newTodoItem
    }

    fun updateChecked(targetItem: TodoItem, isChecked: Boolean): TodoItem {
        realm.beginTransaction()
        targetItem.checked = isChecked
        realm.commitTransaction()
        return targetItem
    }

    fun delete(target: TodoItem) {
        realm.beginTransaction()
        realm.where(TodoItem::class.java)
            .equalTo("label", target.label)
            .equalTo("created", target.created)
            .findAll()
            .deleteAllFromRealm()
        realm.commitTransaction()
    }
}