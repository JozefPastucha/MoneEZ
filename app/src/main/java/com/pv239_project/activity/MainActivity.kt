package com.pv239_project.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pv239_project.fragment.MainFragment



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.pv239_project.R.layout.activity_main)

        if (savedInstanceState == null) {       // Important, otherwise there'd be a new Fragment created with every orientation change
            supportFragmentManager
                ?.beginTransaction()
                ?.replace(android.R.id.content, MainFragment.newInstance(), MainFragment::class.java.simpleName)
                ?.commit()
        }
    }

    override fun onBackPressed() {
        //TODO solve navigation
        //dont go back to expActivity or incomeActivity from mainActivity
    }
}
