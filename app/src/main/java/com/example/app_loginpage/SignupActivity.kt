package com.example.app_loginpage

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {

    private lateinit var viewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)

        viewModel = ViewModelProvider(this).get(SignupViewModel::class.java)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signup_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.signupButton).setOnClickListener {
            val email = findViewById<EditText>(R.id.usernameInput).text.toString()
            val password = findViewById<EditText>(R.id.passwordInput).text.toString()
            val confirmPassword = findViewById<EditText>(R.id.confirmPasswordInput).text.toString()

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.onSignupClicked(email, password)
        }

        viewModel.signupResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
                finish()
            }
            result.onFailure { exception ->
                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_LONG).show()
            }
        }

        findViewById<TextView>(R.id.loginText).setOnClickListener {
            viewModel.onLoginClicked()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.googleSignupButton).setOnClickListener {
            signUpWithGoogle()
        }
    }

    private fun signUpWithGoogle() {
        val credentialManager = CredentialManager.create(this)
        
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("779306655315-0gftnj84qeqbg03ljcf97fde6pkb9vbv.apps.googleusercontent.com")
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(this@SignupActivity, request)
                val credential = result.credential
                
                if (credential is GoogleIdTokenCredential) {
                    Toast.makeText(this@SignupActivity, "Verifying with Google...", Toast.LENGTH_SHORT).show()
                    viewModel.onGoogleSignup(credential.idToken)
                }
            } catch (e: Exception) {
                android.util.Log.e("GoogleAuth", "Error: ${e.message}")
                Toast.makeText(this@SignupActivity, "Google Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }
}