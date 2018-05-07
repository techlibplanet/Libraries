package com.example.mayank.libraries.tablayout

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.mayank.libraries.R
import kotlinx.android.synthetic.main.activity_tab_layout.*

class TabLayoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_layout)


        val pageAdapter = PageAdapter(supportFragmentManager)

        // create fragments from 0 to 9
        for (i in 0 until 10) {
            pageAdapter.add(PageFragment.newInstance(i), "Tab$i")
        }

        view_pager.adapter = pageAdapter
        tabs.setupWithViewPager(view_pager)
    }
}
