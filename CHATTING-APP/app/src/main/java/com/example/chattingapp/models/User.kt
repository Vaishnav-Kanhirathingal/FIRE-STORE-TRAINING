package com.example.chattingapp.models

class User(
    val profilePicture: String?,
    val userName: String,
    val mail: String,
    val password: String,
    val userId: String?,
    val lastMessage: String?
) {
}