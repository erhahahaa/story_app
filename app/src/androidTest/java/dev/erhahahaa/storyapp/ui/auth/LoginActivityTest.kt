package dev.erhahahaa.storyapp.ui.auth

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import dev.erhahahaa.storyapp.R
import dev.erhahahaa.storyapp.utils.EspressoIdlingResource
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {

  @get:Rule val activityRule = ActivityScenarioRule(LoginActivity::class.java)

  @Before
  fun disableAnimations() {
    System.setProperty("animationsDisabled", "true")
  }

  @Test
  fun testLoginSuccess() {
    onView(withId(R.id.ed_login_email)).check(matches(isDisplayed()))
    onView(withId(R.id.ed_login_email)).perform(scrollTo(), click())

    onView(withId(R.id.ed_login_email)).perform(typeText("fdsa@fdsa.com"), closeSoftKeyboard())

    onView(withId(R.id.ed_login_password)).check(matches(isDisplayed()))
    onView(withId(R.id.ed_login_password)).perform(scrollTo(), click())
    onView(withId(R.id.ed_login_password)).perform(typeText("fdsafdsa"), closeSoftKeyboard())

    onView(withId(R.id.btn_login)).perform(click())

    EspressoIdlingResource.increment()
    onView(withId(R.id.btn_login)).check(matches(isDisplayed()))
    EspressoIdlingResource.decrement()
  }
}
