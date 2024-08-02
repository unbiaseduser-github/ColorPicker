package com.github.dhaval2404.colorpicker.sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.dhaval2404.colorpicker.sample.databinding.ActivityMainBinding
import com.github.dhaval2404.colorpicker.sample.util.IntentUtil
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    companion object {

        private const val GITHUB_REPOSITORY = "https://github.com/Dhaval2404/ColorPicker"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Adapter
        binding.viewPager.adapter = ViewPagerAdapter(this)

        // Disable Swipe
        binding.viewPager.isUserInputEnabled = false

        // Set TabLayout
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val title = when (position) {
                0 -> R.string.action_color_picker
                else -> R.string.action_material_color_picker
            }
            tab.setText(title)
        }.attach()
    }

    fun openGithubRepo(view: View) {
        IntentUtil.openURL(this, GITHUB_REPOSITORY)
    }

    class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ColorPickerFragment()
                else -> MaterialColorPickerFragment()
            }
        }

        override fun getItemCount() = 2
    }
}
