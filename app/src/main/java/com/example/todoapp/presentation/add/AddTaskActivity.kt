package com.example.todoapp.presentation.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.R
import com.example.todoapp.data.Task
import com.example.todoapp.databinding.ActivityAddTaskBinding
import com.example.todoapp.presentation.ViewModelFactory
import com.example.todoapp.utils.DatePickerFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTaskActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {
    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var viewModel: AddViewModel
    private var dateMils: Long = System.currentTimeMillis()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Add Task"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.ivDate.setOnClickListener { showDatePicker() }

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[AddViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        binding.apply {
            return when (item.itemId) {
                R.id.save -> {
                    if (edtTitle.text?.isNotEmpty() == true && edtDesc.text?.isNotEmpty() == true) {
                        val newTask = Task(
                            title = edtTitle.text.toString(),
                            description = edtDesc.text.toString(),
                            date = dateMils,
                        )
                        viewModel.addTask(newTask)
                        finish()
                    } else {
                        Toast.makeText(this@AddTaskActivity, "Fill not empty", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
    }

    fun showDatePicker(){
        val dialogFragment = DatePickerFragment()
        dialogFragment.show(supportFragmentManager, "DatePicker")
    }

    override fun onDialogDataSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.tvDate.text = dateFormat.format(calendar.time)
        dateMils = calendar.timeInMillis
    }
}