package com.example.shoppingapp.interfaces

import com.example.shoppingapp.models.Product

interface RecyclerRowInterface {
    fun onClick(product: Product)
}