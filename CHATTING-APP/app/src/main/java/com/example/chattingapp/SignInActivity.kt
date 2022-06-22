package com.example.chattingapp

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.chattingapp.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)
        applyBinding()
    }

    private fun applyBinding() {
        supportActionBar?.hide()
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Login To Your Account")
        progressDialog.setMessage("in progress")
        binding.signIn.setOnClickListener {
            progressDialog.show()
            val email = binding.accountEmail.text.toString()
            val password = binding.accountPassword.text.toString()
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                Toast.makeText(
                    this,
                    if (it.isSuccessful) {
                        startActivity(Intent(this, HomeActivity::class.java))
                        "login successful"
                    } else {
                        "login failed, ${it.exception}"
                    },
                    Toast.LENGTH_SHORT
                ).show()
                progressDialog.dismiss()
            }
        }
    }
}