package com.molang.mytodolist.todoList

import com.molang.mytodolist.todoList.TodoItem
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults
import java.util.*

lateinit var todoItemRealmManager: TodoItemRealmManager

fun initTodoItemRealmManager(realm: Realm) { todoItemRealmManager = TodoItemRealmManager(realm) }

fun getTodoItemRealmManagerInstance(): TodoItemRealmManager { return todoItemRealmManager }

class TodoItemRealmManager(
    val realm: Realm
) {

    fun findAllByTargetDate(targetDate: String): List<TodoItem> {
        return realm.where(TodoItem::class.java)
                .equalTo("targetDate", targetDate)
                .findAll()
                .sort("created")
                .map { TodoItem(it) }
    }

    fun findAllByTargetDateAndType(targetDate: String, type: String): List<TodoItem> {
        return realm.where(TodoItem::class.java)
                .equalTo("targetDate", targetDate)
                .equalTo("type", type)
                .findAll()
                .sort("created")
                .map { TodoItem(it) }
    }

    fun create(newTodoItem: TodoItem): TodoItem {
        realm.beginTransaction()
        realm.insert(
                TodoItem(
                        newTodoItem
                )
        )
        realm.commitTransaction()
        return newTodoItem
    }

    fun createAll(newTodoItems: List<TodoItem>): List<TodoItem> {
        realm.beginTransaction()
        realm.insert(
                newTodoItems
                        .map { TodoItem(it) }
        )
        realm.commitTransaction()
        return newTodoItems
    }

    fun updateChecked(targetItem: TodoItem): TodoItem {
        realm.beginTransaction()
        val target = realm.where(TodoItem::class.java)
                .equalTo("label", targetItem.label)
                .equalTo("created", targetItem.created)
                .findFirst()
        target?.checked = targetItem.checked
        target?.type = targetItem.type
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