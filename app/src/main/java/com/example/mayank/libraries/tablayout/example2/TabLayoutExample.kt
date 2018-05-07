package com.example.mayank.libraries.tablayout.example2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.mayank.libraries.R
import kotlinx.android.synthetic.main.activity_tab_layout_example.*

class TabLayoutExample : AppCompatActivity() {

    internal lateinit var viewpageradapter:ViewPagerAdapter //Declare PagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_layout_example)

        viewpageradapter= ViewPagerAdapter(supportFragmentManager)

        this.viewPager.adapter=viewpageradapter  //Binding PagerAdapter with ViewPager
        this.tab_layout.setupWithViewPager(this.viewPager) //Binding ViewPager with TabLayout
    }

}
