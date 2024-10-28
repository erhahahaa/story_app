package dev.erhahahaa.storyapp.utils.extensions

import android.app.Activity
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
  val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
  if (currentFocus != null) {
    imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
  }
}
