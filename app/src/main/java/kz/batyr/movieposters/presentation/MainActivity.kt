package kz.batyr.movieposters.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.batyr.movieposters.R
import kz.batyr.movieposters.data.PreferencesDataStoreConstants.onboardingCompleted
import kz.batyr.movieposters.databinding.ActivityMainBinding
import kz.batyr.movieposters.presentation.viewPagerAdapter.OnboardingAdapter
import kz.batyr.movieposters.domain.PreferencesDataStoreHelper


class MainActivity : AppCompatActivity() {
    lateinit var viewPager:ViewPager2
    lateinit var binding:ActivityMainBinding
    private lateinit var appBarConfiguration:AppBarConfiguration
    private lateinit var navController:NavController



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferencesDataStoreHelper = PreferencesDataStoreHelper(this)
        viewPager = findViewById(R.id.viewPager2)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)


        val adapter = OnboardingAdapter(this)

/*        binding.bottomNavigation.setOnItemSelectedListener {item ->
            when (item.itemId) {
                R.id.mainFragment -> {
                    navController.navigate(R.id.mainFragment)
                    true
                }
                R.id.menu_search -> {
                    true
                }
                R.id.menu_user -> {
                    true
                }
                else -> false
            }

        }*/
        navController.addOnDestinationChangedListener{ navController, destination, _ ->
            if (destination.id == R.id.mainFragment) {
                binding.bottomNavigation
            }

        }


        lifecycleScope.launch {
            val onboardingState = preferencesDataStoreHelper.getFirstPreference(onboardingCompleted, false)
            if (!onboardingState) {
                binding.bottomNavigation.visibility = View.GONE
                viewPager.adapter = adapter
                viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        if (position == 3) {
                            lifecycleScope.launch {
                                preferencesDataStoreHelper.removePreference(onboardingCompleted)
                                preferencesDataStoreHelper.putPreference(onboardingCompleted, true)
                                delay (500)
                                viewPager.visibility = View.GONE
                                binding.bottomNavigation.visibility = View.VISIBLE
                            }
                        }
                    }
                })
            }
            else binding.viewPager2.visibility = View.GONE
        }




    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    fun closeOnboarding(){
        binding.bottomNavigation.visibility = View.VISIBLE
        viewPager.visibility = View.GONE
    }


}