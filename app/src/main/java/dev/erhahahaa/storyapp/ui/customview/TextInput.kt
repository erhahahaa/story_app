package dev.erhahahaa.storyapp.ui.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import dev.erhahahaa.storyapp.R

class TextInput
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
  LinearLayout(context, attrs, defStyleAttr), View.OnTouchListener {
  private val labelTextView: TextView
  private val inputEditText: EditText
  private val errorTextView: TextView
  private var isObscured = false
  private val visibilityDrawable = ContextCompat.getDrawable(context, R.drawable.ic_visibility)
  private val visibilityOffDrawable =
    ContextCompat.getDrawable(context, R.drawable.ic_visibility_off)

  var text: String
    get() = inputEditText.text.toString()
    set(value) {
      inputEditText.setText(value)
    }

  var error: String?
    get() = errorTextView.text.toString()
    set(value) {
      errorTextView.text = value
      errorTextView.visibility = if (value.isNullOrEmpty()) GONE else VISIBLE
    }

  init {
    orientation = VERTICAL
    LayoutInflater.from(context).inflate(R.layout.text_input, this, true)

    labelTextView = findViewById(R.id.labelTextView)
    inputEditText = findViewById(R.id.inputEditText)
    errorTextView = findViewById(R.id.errorTextView)

    context.obtainStyledAttributes(attrs, R.styleable.TextInput, defStyleAttr, 0).apply {
      labelTextView.text = getString(R.styleable.TextInput_labelText) ?: "Label"

      inputEditText.apply {
        hint = getString(R.styleable.TextInput_hintText)
        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        inputType = getInt(R.styleable.TextInput_inputType, InputType.TYPE_CLASS_TEXT)
        maxLines = getInt(R.styleable.TextInput_maxLines, 1)
        setSelectAllOnFocus(getBoolean(R.styleable.TextInput_selectAllOnFocus, false))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          setAutofillHints(getString(R.styleable.TextInput_autofillHints))
        }
      }
      recycle()
    }
    isObscured = inputEditText.inputType == 129
    if (isObscured) setupToggleVisibility()

    val isPasswordType =
      inputEditText.inputType ==
        InputType.TYPE_TEXT_VARIATION_PASSWORD or
          InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD or
          InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD or
          129

    if (isPasswordType) setupPasswordLengthValidation()
  }

  @SuppressLint("ClickableViewAccessibility")
  private fun setupToggleVisibility() {
    setButtonDrawables(endOfTheText = if (isObscured) visibilityDrawable else visibilityOffDrawable)

    inputEditText.setOnFocusChangeListener { _, hasFocus ->
      if (hasFocus) {
        setButtonDrawables(
          endOfTheText = if (isObscured) visibilityDrawable else visibilityOffDrawable
        )
      } else {
        setButtonDrawables()
      }
    }

    inputEditText.setOnTouchListener(this)
  }

  private fun setupPasswordLengthValidation() {
    inputEditText.addTextChangedListener(
      object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
          error =
            if ((s?.length ?: 0) < 8) {
              context.getString(R.string.password_must_be_at_least_8_characters)
            } else {
              null
            }
        }

        override fun afterTextChanged(s: Editable?) {}
      }
    )
  }

  private fun toggleVisibility() {
    isObscured = !isObscured
    inputEditText.inputType =
      if (isObscured) {
        InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
      } else {
        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
      }

    inputEditText.setSelection(inputEditText.text.length)

    setButtonDrawables(endOfTheText = if (isObscured) visibilityDrawable else visibilityOffDrawable)

    inputEditText.invalidate()
    inputEditText.requestLayout()
  }

  private fun setButtonDrawables(
    startOfTheText: Drawable? = null,
    topOfTheText: Drawable? = null,
    endOfTheText: Drawable? = null,
    bottomOfTheText: Drawable? = null,
  ) {
    inputEditText.setCompoundDrawablesWithIntrinsicBounds(
      startOfTheText,
      topOfTheText,
      endOfTheText,
      bottomOfTheText,
    )
  }

  fun addTextChangedListener(textWatcher: TextWatcher) {
    inputEditText.addTextChangedListener(textWatcher)
  }

  override fun onTouch(v: View?, event: MotionEvent?): Boolean {
    if (inputEditText.compoundDrawables[2] != null) {
      val rightDrawable = inputEditText.compoundDrawables[2]
      if (event?.action == MotionEvent.ACTION_UP) {
        if (event.rawX >= inputEditText.right - rightDrawable.bounds.width()) {
          toggleVisibility()
          return true
        }
      }
    }
    return false
  }
}
