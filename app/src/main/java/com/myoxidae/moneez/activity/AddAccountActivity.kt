package com.myoxidae.moneez.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.add_account.*


class AddAccountActivity : AppCompatActivity() {

    private var editTextTitle: EditText? = null

    companion object {
        @JvmField var EXTRA_NAME = "EXTRA_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.myoxidae.moneez.R.layout.add_account)
        editTextTitle = findViewById(com.myoxidae.moneez.R.id.edit_text_title);

        button3.setOnClickListener {
            val data = Intent()
            val name = editTextTitle?.text.toString()
            if (name.trim().isEmpty()) {
                Toast.makeText(this, "Please insert a name", Toast.LENGTH_SHORT).show();
                return@setOnClickListener;
            }

            data.putExtra(EXTRA_NAME, name )
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }
}