package com.example.app_loginpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MenuViewModel : ViewModel() {
    private val _popularItems = MutableLiveData<List<MenuItem>>()
    val popularItems: LiveData<List<MenuItem>> = _popularItems

    init {
        fetchPopularItems()
    }

    private fun fetchPopularItems() {
        // Fetching all and just taking first few for 'Popular' section
        com.google.firebase.firestore.FirebaseFirestore.getInstance().collection("items")
            .limit(10)
            .get()
            .addOnSuccessListener { documents ->
                val items = documents.toObjects(MenuItem::class.java).mapIndexed { index, item ->
                    item.copy(id = documents.documents[index].id)
                }
                _popularItems.postValue(items)
            }
    }
}