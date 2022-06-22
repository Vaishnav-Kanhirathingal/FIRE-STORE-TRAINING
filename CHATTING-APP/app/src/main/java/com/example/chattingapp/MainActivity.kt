package com.example.chattingapp

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chattingapp.databinding.ActivityMainBinding
import com.example.chattingapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        applyBinding()
    }

    private fun applyBinding() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Creating Your Account")
        progressDialog.setMessage("in progress")

        binding.signUp.setOnClickListener {
            progressDialog.show()
            val accountName = binding.accountName.text.toString()
            val accountEmail = binding.accountEmail.text.toString()
            val accountPassword = binding.accountPassword.text.toString()
            auth.createUserWithEmailAndPassword(accountEmail, accountPassword)
                .addOnCompleteListener {
                    Toast.makeText(
                        this,
                        if (it.isSuccessful) {
                            val user = User(
                                profilePicture = null,
                                userName = accountName,
                                mail = accountEmail,
                                password = accountPassword,
                                userId = it.result.user?.uid,
                                lastMessage = null
                            )
                            database.reference.child("user_name").child(user.userId!!)
                                .setValue(user)
                            "task is successful"
                        } else {
                            "exception occurred = ${it.exception}"
                        },
                        Toast.LENGTH_SHORT
                    ).show()
                    progressDialog.dismiss()
                }
        }
    }
}