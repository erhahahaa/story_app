package dev.erhahahaa.storyapp.ui.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.erhahahaa.storyapp.R
import dev.erhahahaa.storyapp.databinding.ActivityRegisterBinding
import dev.erhahahaa.storyapp.ui.login.LoginActivity
import dev.erhahahaa.storyapp.utils.extensions.getViewModelFactory
import dev.erhahahaa.storyapp.utils.extensions.hideKeyboard
import dev.erhahahaa.storyapp.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

  private lateinit var binding: ActivityRegisterBinding
  private val viewModel: RegisterViewModel by lazy {
    val factory = getViewModelFactory()
    factory.create(RegisterViewModel::class.java)
  }
  private val pleaseWait by lazy { getString(R.string.please_wait) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityRegisterBinding.inflate(layoutInflater)
    setContentView(binding.root)

    viewModel.registerFormState.observe(this) { registerState ->
      registerState ?: return@observe

      binding.btnRegister.isEnabled = registerState.isDataValid
      binding.edRegisterName.error = registerState.nameError?.let { getString(it) }
      binding.edRegisterEmail.error = registerState.emailError?.let { getString(it) }
      binding.edRegisterPassword.error = registerState.passwordError?.let { getString(it) }
    }

    viewModel.registerResult.observe(this) { data ->
      data ?: return@observe
      val msg = data.message
      Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
      if (!data.error) {
        goToLogin()
      }
      binding.btnRegister.setLoading(false)
    }

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

    binding.apply {
      edRegisterName.addTextChangedListener(textWatcher)
      edRegisterEmail.addTextChangedListener(textWatcher)
      edRegisterPassword.addTextChangedListener(textWatcher)

      btnRegister.setOnClickListener {
        hideKeyboard()
        binding.btnRegister.setLoading(true)
        Toast.makeText(this@RegisterActivity, pleaseWait, Toast.LENGTH_SHORT).show()
        viewModel.register(edRegisterName.text, edRegisterEmail.text, edRegisterPassword.text)
      }

      tvGoToLogin.setOnClickListener { goToLogin() }
    }
  }

  private fun goToLogin() {
    finish()
    val intent = Intent(this, LoginActivity::class.java)
    startActivity(intent)
  }
}
