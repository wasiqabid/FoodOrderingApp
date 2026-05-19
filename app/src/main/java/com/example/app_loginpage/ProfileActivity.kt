package com.example.app_loginpage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private lateinit var viewModel: ProfileViewModel
    private var isEditing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nameInput = findViewById<EditText>(R.id.userNameDisplay)
        val editBtn = findViewById<ImageView>(R.id.editNameBtn)

        // Load and observe user data
        viewModel.userName.observe(this) { name ->
            nameInput.setText(name)
        }
        viewModel.loadUserData()

        // Toggle Edit/Save
        editBtn.setOnClickListener {
            if (!isEditing) {
                // Start Editing
                isEditing = true
                nameInput.isEnabled = true
                nameInput.requestFocus()
                editBtn.setImageResource(android.R.drawable.ic_menu_save)
                Toast.makeText(this, "You can now edit your name", Toast.LENGTH_SHORT).show()
            } else {
                // Save Changes
                val newName = nameInput.text.toString().trim()
                if (newName.isNotEmpty()) {
                    viewModel.saveUserName(newName)
                    isEditing = false
                    nameInput.isEnabled = false
                    editBtn.setImageResource(R.drawable.ic_edit)
                    Toast.makeText(this, "Name saved successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Logout Logic
        findViewById<Button>(R.id.logoutBtn).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.orderHistoryBtn).setOnClickListener {
            val intent = Intent(this, OrderHistoryActivity::class.java)
            startActivity(intent)
        }

        // Navigation
        findViewById<ImageView>(R.id.navHome).setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        findViewById<ImageView>(R.id.navCart).setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Cart Badge Observation
        val badge = findViewById<TextView>(R.id.cartBadge)
        CartRepository.cartItems.observe(this) { items ->
            val count = items.sumOf { it.quantity }
            if (count > 0) {
                badge.visibility = View.VISIBLE
                badge.text = count.toString()
            } else {
                badge.visibility = View.GONE
            }
        }
    }
}