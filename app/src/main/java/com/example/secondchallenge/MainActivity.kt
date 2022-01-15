package com.example.secondchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.secondchallenge.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val manager = Manager()
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.elevation = 0F;

        // HIDE ACTION BAR
        // supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // CONFIGURE ACTION BAR NAVIGATION - PART I
            // Retrieve NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
            // Set up the action bar for use with the NavController
        setupActionBarWithNavController(navController)
    }

    // CONFIGURE ACTION BAR NAVIGATION - PART II
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        Log.i("EDWARD LOGS", "onDestroy executed")
        super.onDestroy()
    }
}