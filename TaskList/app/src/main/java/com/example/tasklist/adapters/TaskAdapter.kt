package com.example.tasklist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tasklist.models.DataTask
import com.example.tasklist.R
import com.example.tasklist.databinding.TaskRowItemBinding

class TaskAdapter(private val dataSet: MutableList<DataTask>) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    class ViewHolder(private val binding: TaskRowItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataTask: DataTask) {
            binding.model = dataTask
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: TaskRowItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.task_row_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataTask: DataTask = dataSet[position]
        holder.bind(dataTask)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun delete(int: Int) {
        dataSet.removeAt(int)
        notifyItemRemoved(int)
    }
}