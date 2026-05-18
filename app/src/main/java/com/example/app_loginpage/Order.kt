package com.example.app_loginpage

import com.google.firebase.Timestamp

data class Order(
    val id: String = "",
    val userId: String = "",
    val totalAmount: Double = 0.0,
    val items: List<CartItem> = emptyList(),
    val timestamp: Timestamp? = null
)