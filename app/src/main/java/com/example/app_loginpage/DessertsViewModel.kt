package com.example.app_loginpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DessertsViewModel : ViewModel() {
    private val _desserts = MutableLiveData<List<MenuItem>>()
    val desserts: LiveData<List<MenuItem>> = _desserts

    init {
        fetchDesserts()
    }

    private fun fetchDesserts() {
        MenuRepository.getItemsByCategory("desserts") { items ->
            _desserts.postValue(items)
        }
    }
}