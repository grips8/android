package com.example.shoppingapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.R
import com.example.shoppingapp.databinding.BasketRowItemBinding
import com.example.shoppingapp.models.BasketProduct
import com.example.shoppingapp.models.Product

class BasketAdapter : RecyclerView.Adapter<BasketAdapter.ViewHolder>() {
    private val productDataSet: MutableList<BasketProduct> = mutableListOf(
        BasketProduct(). apply {
            product = Product().apply {
                name = "product1"
                price = 25.0
                description = "lorem ipsum dolor sit amet consectuar"
            }
            quantity = 1
        },
        BasketProduct(). apply {
            product = Product().apply {
            name = "product2"
            price = 23.0
            description = "lorem ipsum dolor sit amet consectuar"
            }
            quantity = 1
        },
        BasketProduct(). apply {
            product = Product().apply {
                name = "product3"
                price = 21.0
                description = "lorem ipsum dolor sit amet consectuar"
            }
            quantity = 1
        },
        BasketProduct(). apply {
            product = Product().apply {
                name = "product4"
                price = 37.0
                description = "lorem ipsum dolor sit amet consectuar"
            }
            quantity = 1
        }
    )

    class ViewHolder(val binding: BasketRowItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(basketProduct: BasketProduct) {
            binding.model = basketProduct
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: BasketRowItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.basket_row_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view: View = holder.binding.root
        view.findViewById<ImageButton>(R.id.bProductAddQty).setOnClickListener { changeQty(position, 1, view.findViewById(R.id.bProductQty)) }
        view.findViewById<ImageButton>(R.id.bProductRemoveQty).setOnClickListener { changeQty(position, -1, view.findViewById(R.id.bProductQty)) }
        val basketProduct: BasketProduct = productDataSet[position]
        holder.bind(basketProduct)
    }

    override fun getItemCount(): Int {
        return productDataSet.size
    }

    private fun changeQty(position: Int, value: Int, textView: TextView) {
        productDataSet[position].quantity += value
        if (productDataSet[position].quantity <= 0) {
            productDataSet.removeAt(position)
            // TODO: remove from DB
            this.notifyItemRemoved(position)
            this.notifyItemRangeChanged(position, itemCount)
        }
        else
            textView.text = productDataSet[position].quantity.toString()
    }
}