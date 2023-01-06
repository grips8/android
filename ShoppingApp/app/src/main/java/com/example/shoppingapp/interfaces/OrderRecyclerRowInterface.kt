package com.example.shoppingapp.interfaces

import com.example.shoppingapp.models.Order

interface OrderRecyclerRowInterface {
    fun onClick(order: Order)
}