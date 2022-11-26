package com.example.tasklist.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tasklist.models.DataTask
import com.example.tasklist.R
import com.example.tasklist.databinding.TaskRowItemBinding
import com.example.tasklist.interfaces.RecyclerRowInterface
import com.example.tasklist.utils.DBHelper

class TaskAdapter(context: Context, private val recyclerRowInterface: RecyclerRowInterface) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {
    private val dbHelper = DBHelper(context)
    private val dataSet = dbHelper.getAllTasks()

    class ViewHolder(private val binding: TaskRowItemBinding, private val recyclerRowInterface: RecyclerRowInterface) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position: Int = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    recyclerRowInterface.onClick(position)
                }
            }
        }
        fun bind(dataTask: DataTask) {
            binding.model = dataTask
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: TaskRowItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.task_row_item, parent, false)
        return ViewHolder(binding, recyclerRowInterface)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataTask: DataTask = dataSet[position]
        holder.bind(dataTask)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun delete(int: Int) {
        dbHelper.deleteTask(dataSet[int].id)
        dataSet.removeAt(int)
        notifyItemRemoved(int)
    }

    fun getTaskData(int: Int) : DataTask {
        return dataSet[int]
    }
}