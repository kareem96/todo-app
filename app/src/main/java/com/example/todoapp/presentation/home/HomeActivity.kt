package com.example.todoapp.presentation.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.Task
import com.example.todoapp.databinding.ActivityHomeBinding
import com.example.todoapp.presentation.ViewModelFactory
import com.example.todoapp.presentation.adapter.TaskAdapter
import com.example.todoapp.presentation.add.AddTaskActivity
import com.example.todoapp.presentation.settings.SettingsActivity
import com.example.todoapp.utils.Event
import com.example.todoapp.utils.TaskFilterType
import com.google.android.material.snackbar.Snackbar

class HomeActivity : AppCompatActivity() {
    private lateinit var mAdapter: TaskAdapter
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.flAddTask.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        viewModel.task.observe(this) { setupRecyclerView(it) }
        viewModel.snackBar.observe(this, Observer(this::showSnackBar))
        initSetup()
    }

    private fun showSnackBar(event: Event<Int>){
        val message = event.getContentIfNotHandled() ?: return
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun setupRecyclerView(tasks: PagedList<Task>) {
        mAdapter = TaskAdapter { task, isComplete ->
            viewModel.completeTask(task, isComplete)
        }
        with(binding) {
            rvTask.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.VERTICAL, false)
            rvTask.adapter = mAdapter
            mAdapter.submitList(tasks)
        }
    }

    private fun initSetup() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(0, ItemTouchHelper.RIGHT)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val task = (viewHolder as TaskAdapter.ViewHolder).getTask
                viewModel.deleteTask(task)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.rvTask)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.filter -> {
                showFilteringMenu()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showFilteringMenu() {
        val v = findViewById<View>(R.id.filter) ?: return
        PopupMenu(this, v).run {
            menuInflater.inflate(R.menu.filter_task, menu)
            setOnMenuItemClickListener {
                viewModel.filter(
                    when (it.itemId) {
                        R.id.active -> TaskFilterType.ACTIVE_TASK
                        R.id.completed -> TaskFilterType.COMPLETED_TASK
                        else -> TaskFilterType.ALL_TASK
                    }
                )
                true
            }
            show()
        }
    }
}