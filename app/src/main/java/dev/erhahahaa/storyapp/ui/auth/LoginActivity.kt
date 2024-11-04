package dev.erhahahaa.storyapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.erhahahaa.storyapp.R
import dev.erhahahaa.storyapp.databinding.ActivityLoginBinding
import dev.erhahahaa.storyapp.ui.story.HomeActivity
import dev.erhahahaa.storyapp.utils.extensions.getViewModelFactory
import dev.erhahahaa.storyapp.utils.extensions.hideKeyboard
import dev.erhahahaa.storyapp.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

  private lateinit var binding: ActivityLoginBinding
  private val viewModel: LoginViewModel by lazy {
    getViewModelFactory().create(LoginViewModel::class.java)
  }
  private val pleaseWait by lazy { getString(R.string.please_wait) }

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

  private fun setupTextWatchers() {
    val textWatcher =
      object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
          viewModel.loginDataChanged(binding.edLoginEmail.text, binding.edLoginPassword.text)
        }

        override fun afterTextChanged(s: Editable?) {}
      }

    binding.edLoginEmail.addTextChangedListener(textWatcher)
    binding.edLoginPassword.addTextChangedListener(textWatcher)
  }

  private fun setupClickListeners() {
    binding.btnLogin.setOnClickListener {
      hideKeyboard()
      binding.btnLogin.setLoading(true)
      Toast.makeText(this, pleaseWait, Toast.LENGTH_SHORT).show()
      viewModel.login(binding.edLoginEmail.text, binding.edLoginPassword.text)
    }

    binding.tvGoToRegister.setOnClickListener { goToRegister() }
  }

  private fun goToHome() {
    startActivity(Intent(this, HomeActivity::class.java))
    finish()
  }

  private fun goToRegister() {
    startActivity(Intent(this, RegisterActivity::class.java))
    finish()
  }
}
