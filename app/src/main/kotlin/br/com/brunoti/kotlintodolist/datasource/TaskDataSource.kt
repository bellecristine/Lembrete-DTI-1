package br.com.brunoti.kotlintodolist.datasource

import br.com.brunoti.kotlintodolist.model.Task
import java.util.*

object TaskDataSource {

    private var list = mutableListOf<Task>()

    fun getList() = list

    fun insertTask(task: Task): Boolean {
        if (validateTask(task)) {
            if (task.id == 0) {
                list.add(task.copy(id = list.size + 1))
            } else {
                list.remove(task)
                list.add(task)
            }
            return true
        }
        return false
    }

    fun findById(taskId: Int) = list.find { it.id == taskId }

    fun deleteTask(task: Task): Boolean {
        return list.remove(task)
    }

    private fun validateTask(task: Task): Boolean {
        if (task.name.isBlank()) {
            return false
        }

        if (task.date.before(Date())) {
            return false
        }

        return true
    }
}
