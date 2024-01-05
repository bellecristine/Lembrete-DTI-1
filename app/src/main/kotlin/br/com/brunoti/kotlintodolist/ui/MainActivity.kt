package br.com.brunoti.kotlintodolist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.brunoti.kotlintodolist.databinding.ActivityMainBinding
import br.com.brunoti.kotlintodolist.datasource.TaskDataSource

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }

    /**
     * Nova maneira de iniciar uma activity.
     * Já que `startActivityForResult` foi depreciado.
     */
    private val register =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                updateList()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTasks.layoutManager = LinearLayoutManager(this)
        binding.rvTasks.adapter = adapter

        updateList()

        insertListeners()
    }

    private fun insertListeners() {
        binding.fab.setOnClickListener {
            register.launch(Intent(this, SaveTaskActivity::class.java))
        }

        adapter.listenerEdit = { task ->
            val intent = Intent(this, SaveTaskActivity::class.java)
            intent.putExtra(SaveTaskActivity.TASK_ID, task.id)
            register.launch(intent)
        }

        adapter.listenerDelete = { task ->
            if (TaskDataSource.deleteTask(task)) {
                updateList()
            }
        }
    }

    private fun updateList() {
        val taskList = TaskDataSource.getList()

        binding.includeEmpty.emptyState.visibility =
            if (taskList.isEmpty()) View.VISIBLE
            else View.GONE

        adapter.submitList(taskList.toList())
    }
}
