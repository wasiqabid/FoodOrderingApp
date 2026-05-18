package com.example.app_loginpage

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_password)

        viewModel = ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.forgot_password_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.sendOtpButton).setOnClickListener {
            val email = findViewById<android.widget.EditText>(R.id.emailInput).text.toString()
            viewModel.onSendOtpClicked(email)
        }

        viewModel.resetResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Success! Check your email for the reset link.", Toast.LENGTH_LONG).show()
                finish() // Go back to Login Screen
            }
            result.onFailure { exception ->
                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_LONG).show()
            }
        }

        findViewById<TextView>(R.id.signUpText).setOnClickListener {
            viewModel.onSignUpClicked()
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}