package com.example.shoppingapp.adapters

import android.annotation.SuppressLint
import android.util.Log
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
import com.example.shoppingapp.services.DBService

class BasketAdapter : RecyclerView.Adapter<BasketAdapter.ViewHolder>() {
    private var productDataSet: MutableList<BasketProduct> = mutableListOf()
    private lateinit var mService: DBService

    @SuppressLint("NotifyDataSetChanged")
    fun initService(service: DBService) {
        mService = service
        productDataSet = mService.getAllBasketProducts()
        notifyDataSetChanged()
    }

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
        view.findViewById<ImageButton>(R.id.bProductAddQty).setOnClickListener { increaseQty(position, view.findViewById(R.id.bProductQty)) }
        view.findViewById<ImageButton>(R.id.bProductRemoveQty).setOnClickListener { decreaseQty(position, view.findViewById(R.id.bProductQty)) }
        val basketProduct: BasketProduct = productDataSet[position]
        holder.bind(basketProduct)
    }

    override fun getItemCount(): Int {
        return productDataSet.size
    }

    private fun increaseQty(position: Int, textView: TextView) {
        Log.d("qty before: ", productDataSet[position].quantity.toString())
        productDataSet[position] = mService.addLatterProductToBasket(productDataSet[position])
        Log.d("qty after: ", productDataSet[position].quantity.toString())
        textView.text = productDataSet[position].quantity.toString()
    }

    private fun decreaseQty(position: Int, textView: TextView) {
        val basketProduct: BasketProduct? = mService.removeProductFromBasket(productDataSet[position])
        if (basketProduct == null) {
            productDataSet.removeAt(position)
            this.notifyItemRemoved(position)
            this.notifyItemRangeChanged(position, itemCount)
        }
        else {
            productDataSet[position] = basketProduct
            textView.text = productDataSet[position].quantity.toString()
        }
    }
}