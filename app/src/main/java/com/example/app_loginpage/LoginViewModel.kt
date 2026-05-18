package com.example.app_loginpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    private val _loginResult = MutableLiveData<Result<String>>()
    val loginResult: LiveData<Result<String>> = _loginResult

    fun onLoginClicked(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _loginResult.value = Result.failure(Exception("Email and password cannot be empty"))
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginResult.value = Result.success("Login Successful")
                } else {
                    _loginResult.value = Result.failure(task.exception ?: Exception("Login failed"))
                }
            }
    }

    fun onGoogleLogin(idToken: String) {
        val credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                _loginResult.postValue(Result.success("Google Login Successful"))
            }
            .addOnFailureListener { e ->
                _loginResult.postValue(Result.failure(e))
            }
    }

    fun onForgotPasswordClicked() {
        // Logic handled in Activity
    }

    fun onSignUpClicked() {
        // Logic handled in Activity
    }
}
