package dev.erhahahaa.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import dev.erhahahaa.storyapp.R
import dev.erhahahaa.storyapp.databinding.ActivityHomeBinding
import dev.erhahahaa.storyapp.ui.greeting.GreetingActivity
import dev.erhahahaa.storyapp.utils.extensions.getViewModelFactory
import dev.erhahahaa.storyapp.viewmodel.MainViewModel

class HomeActivity : AppCompatActivity() {

  private lateinit var appBarConfiguration: AppBarConfiguration
  private lateinit var binding: ActivityHomeBinding
  private lateinit var drawerName: TextView
  private lateinit var drawerEmail: TextView

  private val mainViewModel: MainViewModel by lazy {
    val factory = getViewModelFactory()
    factory.create(MainViewModel::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityHomeBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setSupportActionBar(binding.toolbar)

    binding.fab.setOnClickListener { view ->
      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        .setAction("Action", null)
        .setAnchorView(R.id.fab)
        .show()
    }
    val drawerLayout: DrawerLayout = binding.drawerLayout
    val navView: NavigationView = binding.navView
    val navController = findNavController(R.id.nav_host_fragment_content_home)

    drawerName = navView.getHeaderView(0).findViewById(R.id.tv_drawer_name)
    drawerEmail = navView.getHeaderView(0).findViewById(R.id.tv_drawer_email)

    mainViewModel.user.observe(this) { user ->
      drawerName.text = user?.name
      drawerEmail.text = user?.email
    }

    appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home), drawerLayout)
    setupActionBarWithNavController(navController, appBarConfiguration)
    navView.setupWithNavController(navController)

    navView.setNavigationItemSelectedListener { item ->
      when (item.itemId) {
        R.id.nav_logout -> {
          showLogoutDialog()
          true
        }
        else -> false
      }
    }
  }

  private fun showLogoutDialog() {
    AlertDialog.Builder(this)
      .setTitle("Logout")
      .setMessage("Are you sure you want to logout?")
      .setPositiveButton("Yes") { _, _ ->
        mainViewModel.logout()
        navigateToGreeting()
      }
      .setNegativeButton("No", null)
      .show()
  }

  private fun navigateToGreeting() {
    val intent = Intent(this, GreetingActivity::class.java)
    startActivity(intent)
    finish()
  }

  override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment_content_home)
    return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
  }
}
