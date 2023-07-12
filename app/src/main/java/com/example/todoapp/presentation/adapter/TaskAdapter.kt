package com.example.todoapp.presentation.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.Task
import com.example.todoapp.databinding.ItemTaskBinding
import com.example.todoapp.presentation.component.TitleView
import com.example.todoapp.presentation.detail.DetailActivity
import com.example.todoapp.utils.DateConverter
import com.example.todoapp.utils.TASK_ID

class TaskAdapter(private val onCheckChange: (Task, Boolean) -> Unit) :
    PagedListAdapter<Task, TaskAdapter.ViewHolder>(DIFF_CALLBACK) {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemTaskBinding.bind(itemView)
        lateinit var getTask: Task
        fun bind(task: Task) {
            getTask = task
            binding.tvTitle.text = task.title
            binding.tvDate.text = DateConverter.convertMillsToString(task.date)
            binding.cbItem.setOnClickListener { onCheckChange(task, !task.isCompleted) }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(TASK_ID, task.id)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = getItem(position) as Task
        holder.bind(task)
        when {
            task.isCompleted -> {
                holder.binding.cbItem.isChecked = true
                holder.binding.tvTitle.state = TitleView.DONE

            }

            task.date < System.currentTimeMillis() -> {
                holder.binding.cbItem.isChecked = false
                holder.binding.tvTitle.state = TitleView.OVERDUE
            }

            else -> {
                holder.binding.cbItem.isChecked = false
                holder.binding.tvTitle.state = TitleView.NORMAL
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }
        }
    }
}