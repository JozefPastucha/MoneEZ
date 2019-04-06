package com.pv239_project.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pv239_project.R
import com.pv239_project.fragment.IncomeFragment

class IncomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income)
        if (savedInstanceState == null) {       // Important, otherwise there'd be a new Fragment created with every orientation change
            supportFragmentManager
                ?.beginTransaction()
                ?.replace(android.R.id.content, IncomeFragment.newInstance(), IncomeFragment::class.java.simpleName)
                ?.commit()
        }
    }
}
