package com.example.chattingapp

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import com.example.chattingapp.databinding.ActivitySignUpBinding
import com.example.chattingapp.models.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


private const val TAG = "SignUpActivity"

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var googleSignInClient: GoogleSignInClient

    private val REQUEST_CODE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        setContentView(binding.root)
        supportActionBar!!.hide()
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()
        applyBinding()


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    private fun applyBinding() {
        supportActionBar?.hide()
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
                                userName = accountName,
                                mail = accountEmail,
                                password = accountPassword,
                                userId = it.result.user!!.uid,
                                lastMessage = null,
                                profilePicture = null
                            )
                            database.reference.child("user_name").child(user.userId!!)
                                .setValue(user)
                            startActivity(Intent(this, HomeActivity::class.java))
                            "task is successful"
                        } else {
                            "exception occurred = ${it.exception}"
                        },
                        Toast.LENGTH_SHORT
                    ).show()
                    progressDialog.dismiss()
                }
        }
        binding.alreadyHaveAccount.setOnClickListener {
            Log.d(TAG, "on click")
            startActivity(Intent(this, SignInActivity::class.java))
        }
        binding.googleSignIn.setOnClickListener { googleSignIn() }
        if (auth.currentUser != null) {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    private fun googleSignIn() {
        startActivityForResult(googleSignInClient.signInIntent, REQUEST_CODE)
    }

    private fun authenticateGoogleAccount(id: String?) {
        val credential = GoogleAuthProvider.getCredential(id, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            val firebaseUser = auth.currentUser
            val user = User(
                userName = firebaseUser?.displayName!!,
                id = firebaseUser.uid,
                profilePicture = firebaseUser.photoUrl.toString()
            )
            database.reference.child("user_name").child(user.userId).setValue(user)
            Toast.makeText(
                this,
                if (it.isSuccessful) {
                    startActivity(Intent(this, HomeActivity::class.java))
                    "signInWithCredential:success"
                } else {
                    Log.d(TAG, "error = ${it.exception}")
                    "signInWithCredential:failure"
                }, Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            try {
                val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
                Log.d(TAG, "${account.id}")
                authenticateGoogleAccount(account.idToken)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}