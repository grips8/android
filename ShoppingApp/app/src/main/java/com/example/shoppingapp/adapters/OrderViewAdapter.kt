package com.example.shoppingapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.R
import com.example.shoppingapp.databinding.OrderViewRowItemBinding
import com.example.shoppingapp.models.BasketProduct
import com.example.shoppingapp.services.DBService

class OrderViewAdapter(private val b_id: String) : RecyclerView.Adapter<OrderViewAdapter.ViewHolder>()  {
    private var productDataSet: MutableList<BasketProduct> = mutableListOf()
    private lateinit var mService: DBService

    @SuppressLint("NotifyDataSetChanged")
    fun initService(service: DBService) {
        mService = service
        productDataSet = mService.getBasketProductsById(b_id)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: OrderViewRowItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(basketProduct: BasketProduct) {
            binding.model = basketProduct
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: OrderViewRowItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.order_view_row_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val basketProduct: BasketProduct = productDataSet[position]
        holder.bind(basketProduct)
    }

    override fun getItemCount(): Int {
        return productDataSet.size
    }

}