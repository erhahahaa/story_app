package dev.erhahahaa.storyapp.ui.customview

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import dev.erhahahaa.storyapp.R

class Button
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
  AppCompatButton(context, attrs, defStyleAttr) {

  private var textColorEnabled: Int
  private var textColorDisabled: Int
  private var textSizeCustom: Float
  private var paddingVertical: Int
  private var paddingHorizontal: Int
  private var bgEnabled: Drawable? = ContextCompat.getDrawable(context, R.drawable.bg_button)
  private var bgDisabled: Drawable? =
    ContextCompat.getDrawable(context, R.drawable.bg_button_disable)

  init {
    context.obtainStyledAttributes(attrs, R.styleable.Button, defStyleAttr, 0).apply {
      text = getString(R.styleable.Button_text) ?: text
      textColorEnabled = getColor(R.styleable.Button_textColorEnabled, Color.WHITE)
      textColorDisabled = getColor(R.styleable.Button_textColorDisabled, Color.GRAY)
      textSizeCustom = getDimension(R.styleable.Button_textSizeCustom, 16f)
      paddingVertical = getDimensionPixelSize(R.styleable.Button_paddingVertical, 32)
      paddingHorizontal = getDimensionPixelSize(R.styleable.Button_paddingHorizontal, 32)
      isEnabled = getBoolean(R.styleable.Button_buttonEnabled, isEnabled)
      recycle()
    }
    setupButton()
  }

  private fun setupButton() {
    setTextColor(if (isEnabled) textColorEnabled else textColorDisabled)
    textSize = textSizeCustom
    background = if (isEnabled) bgEnabled else bgDisabled
    gravity = Gravity.CENTER
    setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)
  }

  fun setLoading(isLoading: Boolean) {
    isEnabled = !isLoading
    invalidate()
  }

  override fun setEnabled(enabled: Boolean) {
    super.setEnabled(enabled)
    setupButton()
  }
}
