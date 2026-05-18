package com.example.app_loginpage

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

object MenuRepository {
    private val db = FirebaseFirestore.getInstance()

    fun getItemsByCategory(category: String, callback: (List<MenuItem>) -> Unit) {
        db.collection("items")
            .get()
            .addOnSuccessListener { documents ->
                // Fetching all and filtering locally to avoid case-sensitivity issues with Firebase
                val items = documents.mapNotNull { doc ->
                    try {
                        val item = doc.toObject(MenuItem::class.java).copy(id = doc.id)
                        // Log for debugging (you can see this in Logcat)
                        Log.d("FirestoreData", "Found item: ${item.name} in category: ${item.category}")
                        item
                    } catch (e: Exception) {
                        Log.e("FirestoreError", "Error parsing item: ${e.message}")
                        null
                    }
                }.filter { it.category.lowercase().trim() == category.lowercase().trim() }
                
                callback(items)
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreError", "Failed to fetch items: ${e.message}")
                callback(emptyList())
            }
    }
}