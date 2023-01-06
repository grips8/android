package com.example.shoppingapp.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.R
import com.example.shoppingapp.databinding.OrderRowItemBinding
import com.example.shoppingapp.interfaces.OrderRecyclerRowInterface
import com.example.shoppingapp.models.Order
import com.example.shoppingapp.services.DBService

class OrdersAdapter(val orderRecyclerRowInterface: OrderRecyclerRowInterface) : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {
    private var ordersDataSet: MutableList<Order> = mutableListOf()
    private lateinit var mService: DBService

    @SuppressLint("NotifyDataSetChanged")
    fun initService(service: DBService) {
        mService = service
        ordersDataSet = mService.getAllOrders()
        notifyDataSetChanged()
        Log.d("Orders adapter: ", "launching initService()")
    }

    class ViewHolder(val binding: OrderRowItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.model = order
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: OrderRowItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.order_row_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order: Order = ordersDataSet[position]
        holder.bind(order)
        holder.binding.root.setOnClickListener {
            orderRecyclerRowInterface.onClick(order)
        }
    }

    override fun getItemCount(): Int {
        return ordersDataSet.size
    }
}