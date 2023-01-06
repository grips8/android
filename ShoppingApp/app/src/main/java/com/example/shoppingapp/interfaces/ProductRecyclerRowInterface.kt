package com.example.shoppingapp.interfaces

import com.example.shoppingapp.models.Product

interface ProductRecyclerRowInterface {
    fun onClick(product: Product)
}