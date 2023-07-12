package com.example.todoapp.presentation.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.R
import com.example.todoapp.data.Task
import com.example.todoapp.databinding.ActivityDetailBinding
import com.example.todoapp.presentation.ViewModelFactory
import com.example.todoapp.utils.DateConverter
import com.example.todoapp.utils.TASK_ID

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Detail Task"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        setupUI()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setupUI() {
        val taskId = intent.getIntExtra(TASK_ID, 0)
        viewModel.setTask(taskId)
        viewModel.task.observe(this) { detailTask(it) }

        binding.btnDelete.setOnClickListener {
            viewModel.deleteTsk()
            finish()
        }
    }

    private fun detailTask(task: Task?) {
        binding.apply {
            task?.apply {
                edtTitleDetail.setText(title)
                edtDescDetail.setText(description)
                edtDateDetail.setText(DateConverter.convertMillsToString(date))
            }
        }
    }
}