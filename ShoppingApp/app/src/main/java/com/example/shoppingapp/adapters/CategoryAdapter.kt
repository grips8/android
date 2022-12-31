package com.example.shoppingapp.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.R
import com.example.shoppingapp.databinding.CategoryRowItemBinding
import com.example.shoppingapp.models.Category
import com.example.shoppingapp.services.DBService

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    private var dataset: MutableList<Category> = mutableListOf()
    private lateinit var mService: DBService

    @SuppressLint("NotifyDataSetChanged")
    fun initService(service: DBService) {
        mService = service
        dataset = mService.getAllCategories()
        notifyDataSetChanged()
        Log.d("Product adapter: ", "launching initService()")
    }

    class ViewHolder(val binding: CategoryRowItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.model = category
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: CategoryRowItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.category_row_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category: Category = dataset[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}