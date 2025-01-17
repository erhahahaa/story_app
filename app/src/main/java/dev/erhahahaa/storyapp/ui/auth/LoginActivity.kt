package dev.erhahahaa.storyapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.erhahahaa.storyapp.databinding.ActivityLoginBinding
import dev.erhahahaa.storyapp.ui.story.HomeActivity
import dev.erhahahaa.storyapp.utils.EspressoIdlingResource
import dev.erhahahaa.storyapp.utils.extensions.getViewModelFactory
import dev.erhahahaa.storyapp.utils.extensions.hideKeyboard
import dev.erhahahaa.storyapp.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

  private lateinit var binding: ActivityLoginBinding
  private val viewModel: LoginViewModel by lazy {
    getViewModelFactory().create(LoginViewModel::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityLoginBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setupObservers()
    setupTextWatchers()
    setupClickListeners()
  }

  private fun setupObservers() {
    viewModel.loginFormState.observe(this) { loginState ->
      binding.btnLogin.isEnabled = loginState?.isDataValid == true
      binding.edLoginEmail.error = loginState?.emailError?.let { getString(it) }
      binding.edLoginPassword.error = loginState?.passwordError?.let { getString(it) }
    }

    viewModel.loginResult.observe(this) { data ->
      data?.let {
        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        if (!it.error) goToHome()
        binding.btnLogin.setLoading(false)
      }
    }
  }

  private fun setupClickListeners() {
    binding.btnLogin.setOnClickListener {
      hideKeyboard()
      binding.btnLogin.setLoading(true)
      EspressoIdlingResource.increment()
      viewModel
        .login(binding.edLoginEmail.text.toString(), binding.edLoginPassword.text.toString())
        .also { EspressoIdlingResource.decrement() }
    }
  }

  private fun setupTextWatchers() {
    binding.edLoginEmail.addTextChangedListener(
      object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
          viewModel.loginDataChanged(
            binding.edLoginEmail.text.toString(),
            binding.edLoginPassword.text.toString(),
          )
        }

        override fun afterTextChanged(s: Editable?) {}
      }
    )

    binding.edLoginPassword.addTextChangedListener(
      object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
          viewModel.loginDataChanged(
            binding.edLoginEmail.text.toString(),
            binding.edLoginPassword.text.toString(),
          )
        }

        override fun afterTextChanged(s: Editable?) {}
      }
    )
  }

  private fun goToHome() {
    val intent = Intent(this, HomeActivity::class.java)
    startActivity(intent)
    finish()
  }
}
