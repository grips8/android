package com.example.shoppingapp.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.R
import com.example.shoppingapp.databinding.ProductRowItemBinding
import com.example.shoppingapp.interfaces.ProductRecyclerRowInterface
import com.example.shoppingapp.models.BasketProduct
import com.example.shoppingapp.models.Product
import com.example.shoppingapp.services.DBService

class ProductAdapter(private val productRecyclerRowInterface: ProductRecyclerRowInterface) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    private var productDataSet: MutableList<Product> = mutableListOf()
    private lateinit var mService: DBService

    @SuppressLint("NotifyDataSetChanged")
    fun initService(service: DBService) {
        mService = service
        productDataSet = mService.getAllProducts()
        notifyDataSetChanged()
        Log.d("Product adapter: ", "launching initService()")
    }

    class ViewHolder(val binding: ProductRowItemBinding, private val productRecyclerRowInterface: ProductRecyclerRowInterface) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.model = product
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ProductRowItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.product_row_item, parent, false)
        return ViewHolder(binding, productRecyclerRowInterface)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product: Product = productDataSet[position]
        holder.bind(product)
        holder.binding.root.setOnClickListener {
            productRecyclerRowInterface.onClick(product)
        }
        holder.binding.root.findViewById<ImageButton>(R.id.productAddToBasketButton).setOnClickListener {
            addToBasket(product)
        }
    }

    fun addToBasket(product: Product) {
        val basketProduct: BasketProduct? = mService.checkIfProductIsInBasket(product)
        if (basketProduct !== null)
            mService.addLatterProductToBasket(basketProduct)
        else
            mService.addFirstProductToBasket(product)
        val text: String
        if (basketProduct !== null)
            text = "addLatterProduct"
        else
            text = "AddFirstProduct"

        Toast.makeText(this.mService.applicationContext, text, Toast.LENGTH_SHORT).show()  // possible problems
    }

    override fun getItemCount(): Int {
        return productDataSet.size
    }
}