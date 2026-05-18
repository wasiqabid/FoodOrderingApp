package com.example.app_loginpage

data class CartItem(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val quantity: Int = 1,
    val imageResId: Int = 0,
    val imageUrl: String = ""
)