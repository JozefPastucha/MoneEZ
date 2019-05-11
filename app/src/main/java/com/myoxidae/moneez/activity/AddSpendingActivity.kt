package com.myoxidae.moneez.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity;
import com.myoxidae.moneez.R
import kotlinx.android.synthetic.main.activity_spending.*

class AddSpendingActivity : AppCompatActivity() {

    private var editTextDescription: EditText? = null

    companion object {
        @JvmField var EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spending)
        editTextDescription = findViewById(com.myoxidae.moneez.R.id.spending_description);

        button5.setOnClickListener {
            val data = Intent()
            val name = editTextDescription?.text.toString()
            if (name.trim().isEmpty()) {
                Toast.makeText(this, "Please insert a name", Toast.LENGTH_SHORT).show();
                return@setOnClickListener;
            }

            data.putExtra(EXTRA_DESCRIPTION, name )
            val id = this.intent.getLongExtra("id", -1)//error without def value...
            data.putExtra("id", id)
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }
}
