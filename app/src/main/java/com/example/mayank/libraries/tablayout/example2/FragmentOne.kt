package com.example.mayank.libraries.tablayout.example2

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.mayank.libraries.R
import kotlinx.android.synthetic.main.fragment_layout.*

class FragmentOne : Fragment() {

    var rel_main: RelativeLayout? = null
    var tv_name : TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_layout,container,false)
        rel_main = view.findViewById<RelativeLayout>(R.id.rel_main) as RelativeLayout
        rel_main?.setBackgroundColor(Color.CYAN)
        tv_name = view.findViewById<TextView>(R.id.text_view_name) as TextView
        tv_name?.text = "First Tab"

        return view
    }
}