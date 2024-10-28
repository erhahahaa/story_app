package dev.erhahahaa.storyapp.ui.greeting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.erhahahaa.storyapp.databinding.ActivityGreetingBinding
import dev.erhahahaa.storyapp.ui.login.LoginActivity
import dev.erhahahaa.storyapp.ui.register.RegisterActivity

class GreetingActivity : AppCompatActivity() {

  private lateinit var binding: ActivityGreetingBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityGreetingBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.apply {
      btnSignIn.setOnClickListener {
        val intent = Intent(this@GreetingActivity, LoginActivity::class.java)
        startActivity(intent)
      }

      btnSignUp.setOnClickListener {
        val intent = Intent(this@GreetingActivity, RegisterActivity::class.java)
        startActivity(intent)
      }
    }
  }
}
