package dev.erhahahaa.storyapp.ui.story

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import dev.erhahahaa.storyapp.R
import dev.erhahahaa.storyapp.databinding.ActivityHomeBinding
import dev.erhahahaa.storyapp.ui.greeting.GreetingActivity
import dev.erhahahaa.storyapp.utils.EspressoIdlingResource
import dev.erhahahaa.storyapp.utils.extensions.getViewModelFactory
import dev.erhahahaa.storyapp.viewmodel.MainViewModel

class HomeActivity : AppCompatActivity() {

  private lateinit var binding: ActivityHomeBinding
  private lateinit var drawerName: TextView
  private lateinit var drawerEmail: TextView

  private val mainViewModel: MainViewModel by lazy {
    getViewModelFactory().create(MainViewModel::class.java)
  }
  private lateinit var appBarConfiguration: AppBarConfiguration
  private lateinit var navController: NavController

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityHomeBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setupToolbar()
    setupDrawer()
    setupFab()
    observeUser()
  }

  private fun setupToolbar() {
    setSupportActionBar(binding.toolbar)
  }

  private fun setupDrawer() {
    val drawerLayout: DrawerLayout = binding.drawerLayout
    val navView: NavigationView = binding.navView
    navController = findNavController(R.id.nav_host_fragment_content_home)

    appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home), drawerLayout)
    setupActionBarWithNavController(navController, appBarConfiguration)
    navView.setupWithNavController(navController)

    navView.setNavigationItemSelectedListener { item ->
      when (item.itemId) {
        R.id.action_logout -> {
          EspressoIdlingResource.increment()
          mainViewModel.logout()
          navigateToGreeting()
          EspressoIdlingResource.decrement()
          true
        }
        R.id.action_map -> {
          val action = HomeFragmentDirections.actionNavHomeToNavMap()
          navController.navigate(action)
          drawerLayout.close()
          true
        }
        else -> false
      }
    }
  }

  private fun setupFab() {
    binding.fab.setOnClickListener {
      val action = HomeFragmentDirections.actionNavHomeToNavAddStory()
      navController.navigate(action)
    }

    navController.addOnDestinationChangedListener { _, destination, _ ->
      if (destination.id == R.id.nav_add_story || destination.id == R.id.nav_detail_story) {
        binding.fab.hide()
      } else {
        binding.fab.show()
      }
    }
  }

  private fun observeUser() {
    mainViewModel.user.observe(this) { user ->
      val headerView = binding.navView.getHeaderView(0)
      drawerName = headerView.findViewById(R.id.tv_drawer_name)
      drawerEmail = headerView.findViewById(R.id.tv_drawer_email)

      drawerName.text = user?.name ?: "Guest"
      drawerEmail.text = user?.email ?: "No Email"
    }
  }

  private fun navigateToGreeting() {
    startActivity(Intent(this, GreetingActivity::class.java))
    finish()
  }

  override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment_content_home)
    return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
  }
}
