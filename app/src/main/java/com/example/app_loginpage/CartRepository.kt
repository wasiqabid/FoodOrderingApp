package com.example.app_loginpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object CartRepository {
    private val _cartItems = MutableLiveData<MutableList<CartItem>>(mutableListOf())
    val cartItems: LiveData<MutableList<CartItem>> = _cartItems

    fun addItem(item: CartItem) {
        val currentList = _cartItems.value?.toMutableList() ?: mutableListOf()
        val index = currentList.indexOfFirst { it.id == item.id }
        
        if (index != -1) {
            val existingItem = currentList[index]
            currentList[index] = existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
            currentList.add(item.copy(quantity = 1))
        }
        _cartItems.postValue(currentList)
    }

    fun removeItem(itemId: String) {
        val currentList = _cartItems.value?.toMutableList() ?: mutableListOf()
        val index = currentList.indexOfFirst { it.id == itemId }
        
        if (index != -1) {
            val existingItem = currentList[index]
            if (existingItem.quantity > 1) {
                currentList[index] = existingItem.copy(quantity = existingItem.quantity - 1)
            } else {
                currentList.removeAt(index)
            }
        }
        _cartItems.postValue(currentList)
    }

    fun getQuantity(itemId: String): Int {
        return _cartItems.value?.find { it.id == itemId }?.quantity ?: 0
    }

    fun clearCart() {
        _cartItems.postValue(mutableListOf())
    }
}