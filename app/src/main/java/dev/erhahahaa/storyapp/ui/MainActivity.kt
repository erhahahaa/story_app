package dev.erhahahaa.storyapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.erhahahaa.storyapp.ui.home.HomeActivity
import dev.erhahahaa.storyapp.ui.login.LoginActivity
import dev.erhahahaa.storyapp.utils.extensions.getViewModelFactory
import dev.erhahahaa.storyapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

  private val viewModel: MainViewModel by lazy {
    val factory = getViewModelFactory()
    factory.create(MainViewModel::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.getToken()
    viewModel.token.observe(this) { token ->
      val targetActivity =
        when {
          token.isNullOrEmpty() -> LoginActivity::class.java
          else -> HomeActivity::class.java
        }
      startActivity(Intent(this, targetActivity))
      finish()
    }
  }
}
