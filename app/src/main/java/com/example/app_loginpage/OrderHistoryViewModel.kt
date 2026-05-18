package com.example.app_loginpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class OrderHistoryViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> = _orders

    fun fetchOrderHistory() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("orders")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val ordersList = documents.map { doc ->
                    doc.toObject(Order::class.java).copy(id = doc.id)
                }.sortedByDescending { it.timestamp } // Sort locally to avoid needing a Firestore index
                _orders.postValue(ordersList)
            }
            .addOnFailureListener {
                _orders.postValue(emptyList())
            }
    }
}