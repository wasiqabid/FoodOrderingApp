package com.example.app_loginpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SignupViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    private val _signupResult = MutableLiveData<Result<String>>()
    val signupResult: LiveData<Result<String>> = _signupResult

    fun onSignupClicked(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _signupResult.value = Result.failure(Exception("Email and password cannot be empty"))
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _signupResult.value = Result.success("Signup Successful")
                } else {
                    _signupResult.value = Result.failure(task.exception ?: Exception("Signup failed"))
                }
            }
    }

    fun onGoogleSignup(idToken: String) {
        val credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                _signupResult.postValue(Result.success("Google Signup Successful"))
            }
            .addOnFailureListener { e ->
                _signupResult.postValue(Result.failure(e))
            }
    }

    fun onLoginClicked() {
        // Handled in Activity
    }
}
