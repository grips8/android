package com.example.shoppingapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.shoppingapp.R
import com.example.shoppingapp.databinding.ActivityProductDetailsBinding
import com.example.shoppingapp.models.Category
import com.example.shoppingapp.models.Product

class ProductDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityProductDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_details)
        val product = Product().apply {
            name = intent.getStringExtra("name").toString()
            price = intent.getDoubleExtra("price", 0.0)
            description = intent.getStringExtra("description").toString()
            category = Category().apply { name = intent.getStringExtra("categoryName").toString() }
            }
        binding.model = product
    }
}