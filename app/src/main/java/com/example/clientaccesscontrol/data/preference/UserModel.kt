package com.example.clientaccesscontrol.data.preference

data class UserModel(
    val username: String,
    val password: String,
    val ipAdress: String,
    val token: String,
    val isLogin: Boolean = false
)