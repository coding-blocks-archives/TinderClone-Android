package com.codingblocks.tinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codingblocks.tinder.adapters.ViewPagerFragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        viewPager2.adapter = ViewPagerFragmentStateAdapter(this)
        TabLayoutMediator(tab_layout, viewPager2,
            TabLayoutMediator.OnConfigureTabCallback { tab, position ->
                // Styling each tab here
                when (position) {
                    0 -> tab.setIcon(R.drawable.ic_user)
                    1 -> tab.setIcon(R.drawable.ic_tinder)
                    2 -> tab.setIcon(R.drawable.ic_chat)


                }
            }).attach()
    }
}
