package dev.erhahahaa.storyapp.ui.story

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.Instrumentation
import android.content.Intent
import android.provider.MediaStore
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import dev.erhahahaa.storyapp.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddStoryFragmentTest {

  @get:Rule val activityScenarioRule = ActivityScenarioRule(HomeActivity::class.java)

  @Before
  fun setup() {
    Intents.init()

    val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    device.executeShellCommand(
      "pm grant ${InstrumentationRegistry.getInstrumentation().targetContext.packageName} ${Manifest.permission.CAMERA}"
    )
  }

  @After
  fun teardown() {
    Intents.release()
  }

  @Test
  fun testAddStoryWithRealCamera() {
    onView(withId(R.id.fab)).perform(click())

    onView(withId(R.id.ed_add_description)).check(matches(isDisplayed()))

    onView(withId(R.id.ed_add_description)).perform(typeText("New Story"), closeSoftKeyboard())

    Intents.intending(IntentMatchers.hasAction(MediaStore.ACTION_IMAGE_CAPTURE))
      .respondWith(Instrumentation.ActivityResult(RESULT_OK, Intent()))

    onView(withId(R.id.btn_camera)).perform(click())

    Intents.intended(IntentMatchers.hasAction(MediaStore.ACTION_IMAGE_CAPTURE))

    onView(withId(R.id.sv_fragment_add_story)).perform(swipeUp())
    onView(withId(R.id.button_add)).perform(click())
  }
}
