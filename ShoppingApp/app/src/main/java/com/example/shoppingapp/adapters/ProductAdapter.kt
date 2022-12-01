package com.example.shoppingapp.adapters

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.R
import com.example.shoppingapp.databinding.ProductRowItemBinding
import com.example.shoppingapp.interfaces.RecyclerRowInterface
import com.example.shoppingapp.models.Product

class ProductAdapter(private val recyclerRowInterface: RecyclerRowInterface) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    private val productDataSet: Array<Product> = arrayOf(
        Product().apply {
            name = "product1"
            price = 25.0
            description = "lorem ipsum dolor sit amet consectuar"
        },
        Product().apply {
            name = "product2"
            price = 23.0
            description = "lorem ipsum dolor sit amet consectuar"
        },
        Product().apply {
            name = "product3"
            price = 21.0
            description = "lorem ipsum dolor sit amet consectuar"
        },
        Product().apply {
            name = "product4"
            price = 37.0
            description = "lorem ipsum dolor sit amet consectuar"
        },

        )

    class ViewHolder(val binding: ProductRowItemBinding, private val recyclerRowInterface: RecyclerRowInterface) : RecyclerView.ViewHolder(binding.root) {
//        init {
//            binding.root.setOnClickListener {
//                val position: Int = adapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    recyclerRowInterface.onClick(position)
//                }
//            }
//        }
        fun bind(product: Product) {
            binding.model = product
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ProductRowItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.product_row_item, parent, false)
        return ViewHolder(binding, recyclerRowInterface)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product: Product = productDataSet[position]
        holder.bind(product)
        holder.binding.root.setOnClickListener {
            recyclerRowInterface.onClick(product)
        }
    }

    override fun getItemCount(): Int {
       return productDataSet.size
    }
}