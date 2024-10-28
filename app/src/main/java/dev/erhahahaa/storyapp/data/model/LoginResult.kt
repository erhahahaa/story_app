package dev.erhahahaa.storyapp.data.model

import kotlinx.serialization.Serializable

@Serializable data class LoginResult(val userId: String, val name: String, val token: String)
