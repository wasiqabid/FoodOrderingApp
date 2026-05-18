package com.example.app_loginpage

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider

class AuthActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)

        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.auth_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.signUpButton).setOnClickListener {
            viewModel.onSignUpClicked()
            val intent = android.content.Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.loginButton).setOnClickListener {
            viewModel.onLoginClicked()
            val intent = android.content.Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}