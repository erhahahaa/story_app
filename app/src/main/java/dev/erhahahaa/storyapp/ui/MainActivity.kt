package dev.erhahahaa.storyapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.erhahahaa.storyapp.ui.greeting.GreetingActivity
import dev.erhahahaa.storyapp.ui.story.HomeActivity
import dev.erhahahaa.storyapp.utils.extensions.getViewModelFactory
import dev.erhahahaa.storyapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

  private val viewModel: MainViewModel by lazy {
    val factory = getViewModelFactory()
    factory.create(MainViewModel::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.getUser()
    viewModel.user.observe(this) { user ->
      val targetActivity =
        when {
          user == null -> GreetingActivity::class.java
          else -> HomeActivity::class.java
        }
      startActivity(Intent(this, targetActivity))
      finish()
    }
  }
}
