package com.example.app_loginpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BurgersViewModel : ViewModel() {
    private val _burgers = MutableLiveData<List<MenuItem>>()
    val burgers: LiveData<List<MenuItem>> = _burgers

    init {
        fetchBurgers()
    }

    private fun fetchBurgers() {
        MenuRepository.getItemsByCategory("burgers") { items ->
            _burgers.postValue(items)
        }
    }
}