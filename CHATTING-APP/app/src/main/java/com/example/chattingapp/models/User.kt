package com.example.chattingapp.models

class User {
    var profilePicture: String?
    var userName: String
    var mail: String?
    var password: String?
    var userId: String
    var lastMessage: String?

    constructor(
        profilePicture: String?, userName: String, mail: String,
        password: String, userId: String, lastMessage: String?
    ) {
        this.profilePicture = profilePicture
        this.userName = userName
        this.mail = mail
        this.password = password
        this.userId = userId
        this.lastMessage = lastMessage
    }

    constructor(userName: String, id: String, profilePicture: String) {
        this.profilePicture = profilePicture
        this.userName = userName
        this.mail = null
        this.password = null
        this.userId = id
        this.lastMessage = null
    }
}