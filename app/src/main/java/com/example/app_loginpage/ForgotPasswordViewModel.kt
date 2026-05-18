package com.example.app_loginpage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    private val _resetResult = MutableLiveData<Result<String>>()
    val resetResult: LiveData<Result<String>> = _resetResult

    fun onSendOtpClicked(email: String) {
        if (email.isEmpty()) {
            _resetResult.value = Result.failure(Exception("Email cannot be empty"))
            return
        }

        Log.d("FirebaseAuth", "Attempting to send reset email to: $email")
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FirebaseAuth", "Reset email sent successfully")
                    _resetResult.value = Result.success("Password reset email sent")
                } else {
                    Log.e("FirebaseAuth", "Failed to send reset email: ${task.exception?.message}")
                    _resetResult.value = Result.failure(task.exception ?: Exception("Failed to send reset email"))
                }
            }
    }

    fun onSignUpClicked() {
        // Handled in Activity
    }
}
