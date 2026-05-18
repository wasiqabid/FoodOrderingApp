package com.example.app_loginpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PizzaViewModel : ViewModel() {
    private val _pizzas = MutableLiveData<List<MenuItem>>()
    val pizzas: LiveData<List<MenuItem>> = _pizzas

    init {
        fetchPizzas()
    }

    private fun fetchPizzas() {
        MenuRepository.getItemsByCategory("pizza") { items ->
            _pizzas.postValue(items)
        }
    }
}