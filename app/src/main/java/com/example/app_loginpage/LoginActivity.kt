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

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.loginButton).setOnClickListener {
            val email = findViewById<EditText>(R.id.usernameInput).text.toString()
            val password = findViewById<EditText>(R.id.passwordInput).text.toString()
            
            viewModel.onLoginClicked(email, password)
        }

        viewModel.loginResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Welcome Back!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
                finish()
            }
            result.onFailure { exception ->
                Toast.makeText(this, "Login Error: ${exception.message}", Toast.LENGTH_LONG).show()
            }
        }

        findViewById<TextView>(R.id.forgotPasswordText).setOnClickListener {
            viewModel.onForgotPasswordClicked()
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        findViewById<TextView>(R.id.signUpText).setOnClickListener {
            viewModel.onSignUpClicked()
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.googleLoginButton).setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun signInWithGoogle() {
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
                val result = credentialManager.getCredential(this@LoginActivity, request)
                val credential = result.credential
                
                if (credential is GoogleIdTokenCredential) {
                    Toast.makeText(this@LoginActivity, "Verifying with Google...", Toast.LENGTH_SHORT).show()
                    viewModel.onGoogleLogin(credential.idToken)
                }
            } catch (e: Exception) {
                android.util.Log.e("GoogleAuth", "Error: ${e.message}")
                Toast.makeText(this@LoginActivity, "Google Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }
}