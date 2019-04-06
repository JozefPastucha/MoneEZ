package com.pv239_project.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import com.pv239_project.R
import com.pv239_project.fragment.ExpFragment

class ExpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exp)
        if (savedInstanceState == null) {       // Important, otherwise there'd be a new Fragment created with every orientation change
            supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(android.R.id.content, ExpFragment.newInstance(), ExpFragment::class.java.simpleName)
                    ?.commit()
        }
    }
}
