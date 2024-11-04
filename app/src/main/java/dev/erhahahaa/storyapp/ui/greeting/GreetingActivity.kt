package dev.erhahahaa.storyapp.ui.greeting

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import dev.erhahahaa.storyapp.databinding.ActivityGreetingBinding
import dev.erhahahaa.storyapp.ui.auth.LoginActivity
import dev.erhahahaa.storyapp.ui.auth.RegisterActivity

class GreetingActivity : AppCompatActivity() {

  private lateinit var binding: ActivityGreetingBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityGreetingBinding.inflate(layoutInflater)
    setContentView(binding.root)

    playAnimations()

    binding.apply {
      btnSignIn.setOnClickListener {
        val intent = Intent(this@GreetingActivity, LoginActivity::class.java)
        val options =
          ActivityOptionsCompat.makeSceneTransitionAnimation(
            this@GreetingActivity,
            Pair(ivHero, "shared_image"),
            Pair(tvTitle, "shared_title"),
          )
        startActivity(intent, options.toBundle())
      }

      btnSignUp.setOnClickListener {
        val intent = Intent(this@GreetingActivity, RegisterActivity::class.java)
        val options =
          ActivityOptionsCompat.makeSceneTransitionAnimation(
            this@GreetingActivity,
            Pair(ivHero, "shared_image"),
            Pair(tvTitle, "shared_title"),
          )
        startActivity(intent, options.toBundle())
      }
    }
  }

  private fun playAnimations() {
    ObjectAnimator.ofFloat(binding.ivHero, View.ALPHA, 0f, 1f).apply {
      duration = 1000
      start()
    }
    ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 0f, 1f).apply {
      duration = 1000
      startDelay = 500
      start()
    }
    ObjectAnimator.ofFloat(binding.tvDescription, View.ALPHA, 0f, 1f).apply {
      duration = 1000
      startDelay = 1000
      start()
    }
  }
}
