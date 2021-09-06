package com.devcraft.freesignalsfortrading

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.devcraft.freesignalsfortrading.databinding.ActivityMainBinding
import com.devcraft.freesignalsfortrading.notificationManager.WorkService
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(binding.appBarMain.toolbar)

        setContentView(binding.root)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration =
            AppBarConfiguration(
            setOf(
                R.id.nav_quotes, R.id.nav_alerts, R.id.nav_fav, R.id.nav_share
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val workRequest = PeriodicWorkRequest.Builder(WorkService::class.java, 15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("Update",ExistingPeriodicWorkPolicy.REPLACE,workRequest)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context,MainActivity::class.java)
        }
    }

}