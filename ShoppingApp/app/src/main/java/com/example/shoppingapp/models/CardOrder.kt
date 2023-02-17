package com.example.shoppingapp.models

import com.example.shoppingapp.utils.JsonOrder

data class CardOrder(val order: JsonOrder, val cardDetails: CardDetails)