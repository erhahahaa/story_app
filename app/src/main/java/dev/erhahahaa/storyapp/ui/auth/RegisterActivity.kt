package dev.erhahahaa.storyapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.erhahahaa.storyapp.R
import dev.erhahahaa.storyapp.databinding.ActivityRegisterBinding
import dev.erhahahaa.storyapp.utils.extensions.getViewModelFactory
import dev.erhahahaa.storyapp.utils.extensions.hideKeyboard
import dev.erhahahaa.storyapp.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

  private lateinit var binding: ActivityRegisterBinding
  private val viewModel: RegisterViewModel by lazy {
    getViewModelFactory().create(RegisterViewModel::class.java)
  }
  private val pleaseWait by lazy { getString(R.string.please_wait) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityRegisterBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setupObservers()
    setupTextWatchers()
    setupClickListeners()
  }

  private fun setupObservers() {
    viewModel.registerFormState.observe(this) { registerState ->
      registerState?.let {
        binding.btnRegister.isEnabled = it.isDataValid
        binding.edRegisterName.error = it.nameError?.let { error -> getString(error) }
        binding.edRegisterEmail.error = it.emailError?.let { error -> getString(error) }
        binding.edRegisterPassword.error = it.passwordError?.let { error -> getString(error) }
      }
    }

    viewModel.registerResult.observe(this) { data ->
      data?.let {
        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        if (!it.error) goToLogin()
        binding.btnRegister.setLoading(false)
      }
    }
  }

  private fun setupTextWatchers() {
    val textWatcher =
      object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
          viewModel.registerDataChanged(
            binding.edRegisterName.text,
            binding.edRegisterEmail.text,
            binding.edRegisterPassword.text,
          )
        }

        override fun afterTextChanged(s: Editable?) {}
      }

    binding.edRegisterName.addTextChangedListener(textWatcher)
    binding.edRegisterEmail.addTextChangedListener(textWatcher)
    binding.edRegisterPassword.addTextChangedListener(textWatcher)
  }

  private fun setupClickListeners() {
    binding.btnRegister.setOnClickListener {
      hideKeyboard()
      binding.btnRegister.setLoading(true)
      Toast.makeText(this, pleaseWait, Toast.LENGTH_SHORT).show()
      viewModel.register(
        binding.edRegisterName.text,
        binding.edRegisterEmail.text,
        binding.edRegisterPassword.text,
      )
    }

    binding.tvGoToLogin.setOnClickListener { goToLogin() }
  }

  private fun goToLogin() {
    startActivity(Intent(this, LoginActivity::class.java))
    finish()
  }
}
