package com.example.app_loginpage

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object CartRepository {
    private val _cartItems = MutableLiveData<MutableList<CartItem>>(mutableListOf())
    val cartItems: LiveData<MutableList<CartItem>> = _cartItems
    
    private var prefs: SharedPreferences? = null
    private val gson = Gson()

    fun init(context: Context) {
        prefs = context.getSharedPreferences("feasto_cart_prefs", Context.MODE_PRIVATE)
        loadCart()
    }

    private fun saveCart() {
        val json = gson.toJson(_cartItems.value)
        prefs?.edit()?.putString("cart_json", json)?.apply()
    }

    private fun loadCart() {
        val json = prefs?.getString("cart_json", null)
        if (json != null) {
            val type = object : TypeToken<MutableList<CartItem>>() {}.type
            val items: MutableList<CartItem> = gson.fromJson(json, type)
            _cartItems.postValue(items)
        }
    }

    fun addItem(item: CartItem) {
        val currentList = _cartItems.value?.toMutableList() ?: mutableListOf()
        val index = currentList.indexOfFirst { it.id == item.id }
        
        if (index != -1) {
            val existingItem = currentList[index]
            currentList[index] = existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
            currentList.add(item.copy(quantity = 1))
        }
        _cartItems.value = currentList
        saveCart()
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
        _cartItems.value = currentList
        saveCart()
    }

    fun getQuantity(itemId: String): Int {
        return _cartItems.value?.find { it.id == itemId }?.quantity ?: 0
    }

    fun clearCart() {
        _cartItems.value = mutableListOf()
        saveCart()
    }
}