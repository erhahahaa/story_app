package dev.erhahahaa.storyapp.data.model

data class User(val userId: String, val name: String, val email: String, val token: String) {
  override fun toString(): String {
    return "$userId,$name,$email,$token"
  }
}
