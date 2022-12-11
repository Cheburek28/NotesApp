package com.example.androiddev.entities

data class User (
    private val nickName : String,
    private val password : String
) {
    fun get_nickname() : String = nickName
    fun get_password() : String = password
}