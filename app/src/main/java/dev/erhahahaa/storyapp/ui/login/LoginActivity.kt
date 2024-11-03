package dev.erhahahaa.storyapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.erhahahaa.storyapp.R
import dev.erhahahaa.storyapp.databinding.ActivityLoginBinding
import dev.erhahahaa.storyapp.ui.home.HomeActivity
import dev.erhahahaa.storyapp.ui.register.RegisterActivity
import dev.erhahahaa.storyapp.utils.extensions.getViewModelFactory
import dev.erhahahaa.storyapp.utils.extensions.hideKeyboard
import dev.erhahahaa.storyapp.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

  private lateinit var binding: ActivityLoginBinding
  private val viewModel: LoginViewModel by lazy {
    val factory = getViewModelFactory()
    factory.create(LoginViewModel::class.java)
  }
  private val pleaseWait by lazy { getString(R.string.please_wait) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityLoginBinding.inflate(layoutInflater)
    setContentView(binding.root)

    viewModel.loginFormState.observe(this) { loginState ->
      loginState ?: return@observe

      binding.btnLogin.isEnabled = loginState.isDataValid
      binding.edLoginEmail.error = loginState.emailError?.let { getString(it) }
      binding.edLoginPassword.error = loginState.passwordError?.let { getString(it) }
    }

    viewModel.loginResult.observe(this) { data ->
      data ?: return@observe
      val msg = data.message
      Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
      if (!data.error) {
        goToHome()
      }
      binding.btnLogin.setLoading(false)
    }

    val textWatcher =
      object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
          viewModel.loginDataChanged(binding.edLoginEmail.text, binding.edLoginPassword.text)
        }

        override fun afterTextChanged(s: Editable?) {}
      }

    binding.apply {
      edLoginEmail.addTextChangedListener(textWatcher)
      edLoginPassword.addTextChangedListener(textWatcher)
      btnLogin.setOnClickListener {
        hideKeyboard()
        binding.btnLogin.setLoading(true)
        Toast.makeText(this@LoginActivity, pleaseWait, Toast.LENGTH_SHORT).show()
        viewModel.login(edLoginEmail.text, edLoginPassword.text)
      }

      tvGoToRegister.setOnClickListener { goToRegister() }
    }
  }

  private fun goToHome() {
    finish()
    val intent = Intent(this, HomeActivity::class.java)
    startActivity(intent)
  }

  private fun goToRegister() {
    finish()
    val intent = Intent(this, RegisterActivity::class.java)
    startActivity(intent)
  }
}
