package com.example.app_loginpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val userId = auth.currentUser?.uid

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    fun loadUserData() {
        if (userId == null) return
        
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    _userName.value = document.getString("name") ?: auth.currentUser?.email ?: ""
                } else {
                    _userName.value = auth.currentUser?.email ?: ""
                }
            }
    }

    fun saveUserName(newName: String) {
        if (userId == null) return
        
        db.collection("users").document(userId)
            .set(mapOf("name" to newName))
            .addOnSuccessListener {
                _userName.value = newName
            }
    }
}