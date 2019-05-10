package com.myoxidae.moneez.activity

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputLayout
import com.myoxidae.moneez.R
import kotlinx.android.synthetic.main.add_account.*
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder


class AddAccountActivity : AppCompatActivity() {

    private var inputLayoutName: TextInputLayout? = null
    private var inputLayoutCurrency: TextInputLayout? = null

    private var editTextName: EditText? = null
    private var editTextBalance: EditText? = null
    private var editTextCurrency: EditText? = null
    private var editTextDescription: EditText? = null

    companion object {
        @JvmField
        var EXTRA_NAME = "EXTRA_NAME"
        @JvmField
        var EXTRA_BALANCE = "EXTRA_BALANCE"
        @JvmField
        var EXTRA_CURRENCY = "EXTRA_CURRENCY"
        @JvmField
        var EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_account)

        inputLayoutName = findViewById(R.id.input_layout_name)
        inputLayoutCurrency = findViewById(R.id.input_layout_currency)

        editTextName = findViewById(R.id.edit_text_name)
        editTextBalance = findViewById(R.id.edit_text_initial_balance)
        editTextCurrency = findViewById(R.id.edit_text_currency)
        editTextDescription = findViewById(R.id.edit_text_description)

//        Set toolbar - title and back button
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Add account"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (!editTextName?.text.isNullOrEmpty() || !editTextCurrency?.text.isNullOrEmpty()
                    || !editTextDescription?.text.isNullOrEmpty() || editTextBalance?.text.toString() != "0"
                ) {
                    showDialog()
                } else {
                    finish()
                }
            }
        })


//        Enable and disable save button if required fields are filled
        setSaveEnable()

        editTextName?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (editTextName?.text.isNullOrEmpty()) {
                    inputLayoutName?.setError("Name can't be empty")
                } else {
                    inputLayoutName?.setError(null)
                }
                setSaveEnable()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })
        editTextCurrency?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (editTextCurrency?.text.isNullOrEmpty()) {
                    inputLayoutCurrency?.setError("Please choose currency")
                } else {
                    inputLayoutCurrency?.setError(null)
                }
                setSaveEnable()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })

        editTextName?.setOnFocusChangeListener { v, hasFocus ->
            run {
                if (!hasFocus) {
                    if (editTextName?.text.isNullOrEmpty()) {
                        inputLayoutName?.setError("Name can't be empty")
                    } else {
                        inputLayoutName?.setError(null)
                    }
                }
                setSaveEnable()
            }
        }
        editTextCurrency?.setOnFocusChangeListener { v, hasFocus ->
            run {
                if (!hasFocus) {
                    if (editTextCurrency?.text.isNullOrEmpty()) {
                        inputLayoutCurrency?.setError("Please choose currency")
                    } else {
                        inputLayoutCurrency?.setError(null)
                    }
                }
                setSaveEnable()
            }
        }


//        save
        save_button.setOnClickListener {
            val data = Intent()
            val name = editTextName?.text.toString()
            val initial_balance = editTextBalance?.text.toString()
            val currency = editTextCurrency?.text.toString()
            val description = editTextDescription?.text.toString()

            data.putExtra(EXTRA_NAME, name)
            data.putExtra(EXTRA_BALANCE, initial_balance)
            data.putExtra(EXTRA_CURRENCY, currency)
            data.putExtra(EXTRA_DESCRIPTION, description)

            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }

    private fun setSaveEnable() {
        save_button.setEnabled(!editTextName?.text.isNullOrEmpty() && !editTextCurrency?.text.isNullOrEmpty())
    }

    private fun showDialog() {
        lateinit var dialog: AlertDialog

        val builder = AlertDialog.Builder(this)

        builder.setMessage("You have unsaved changes. Do you want to keep editing?")

        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_NEGATIVE -> finish()
            }
        }

        builder.setPositiveButton("Keep editing", dialogClickListener)
        builder.setNegativeButton("Discard", dialogClickListener)

        dialog = builder.create()
        dialog.show()
    }
}
