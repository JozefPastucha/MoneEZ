package com.myoxidae.moneez

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_account_detail.*

class AccountDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_detail)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Open form ", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

//        Set text

        val fn = intent.getStringExtra("item")

        val fntext = findViewById<TextView>(R.id.whatever)
        fntext.text = fn
    }

}
