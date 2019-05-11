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
import kotlinx.android.synthetic.main.add_account.*
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder
import com.mynameismidori.currencypicker.CurrencyPickerListener
import com.mynameismidori.currencypicker.CurrencyPicker
import android.R
import android.opengl.Visibility
import com.mynameismidori.currencypicker.ExtendedCurrency
import com.myoxidae.moneez.model.AccountType


class AddAccountActivity : AppCompatActivity() {

    private var inputLayoutName: TextInputLayout? = null
    private var inputLayoutInterest: TextInputLayout? = null

    private var editTextName: EditText? = null
    private var editTextBalance: EditText? = null
    private var editTextDescription: EditText? = null
    private var editTextInterest: EditText? = null

    companion object {
        @JvmField
        var EXTRA_TYPE = "EXTRA_TYPE"
        @JvmField
        var EXTRA_NAME = "EXTRA_NAME"
        @JvmField
        var EXTRA_BALANCE = "EXTRA_BALANCE"
        @JvmField
        var EXTRA_CURRENCY = "EXTRA_CURRENCY"
        @JvmField
        var EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION"
        @JvmField
        var EXTRA_INTEREST = "EXTRA_INTEREST"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val type: AccountType = intent.getSerializableExtra("type") as AccountType
        super.onCreate(savedInstanceState)
        setContentView(com.myoxidae.moneez.R.layout.add_account)

        inputLayoutName = findViewById(com.myoxidae.moneez.R.id.input_layout_name)
        inputLayoutInterest = findViewById(com.myoxidae.moneez.R.id.input_layout_interest)

        editTextName = findViewById(com.myoxidae.moneez.R.id.edit_text_name)
        editTextBalance = findViewById(com.myoxidae.moneez.R.id.edit_text_initial_balance)
        editTextDescription = findViewById(com.myoxidae.moneez.R.id.edit_text_description)
        editTextInterest = findViewById(com.myoxidae.moneez.R.id.edit_text_interest)

        if (type == AccountType.Cash) {
            inputLayoutInterest?.visibility = View.GONE
        }

//        Set toolbar - title and back button
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(com.myoxidae.moneez.R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "New " + type.toString() + " account"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (!editTextName?.text.isNullOrEmpty() || !currency_button?.text.toString().startsWith("*")
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

        currency_button?.setOnClickListener {
            val picker = CurrencyPicker.newInstance("Select Currency")  // dialog title
            picker.setListener { name, code, symbol, flagDrawableResID ->
                // Implement your code here
                currency_button?.text = name
                currency_button?.setCompoundDrawablesWithIntrinsicBounds(
                    getDrawable(flagDrawableResID),
                    null, null, null
                )
                picker.dismiss()
                setSaveEnable()
            }
            picker.show(supportFragmentManager, "CURRENCY_PICKER")
        }


//        save
        save_button.setOnClickListener {
            val data = Intent()
            val name = editTextName?.text.toString()
            val initial_balance = editTextBalance?.text.toString()
            val currency = currency_button?.text.toString()
            val description = editTextDescription?.text.toString()
            val interest = editTextInterest?.text.toString()

            data.putExtra(EXTRA_TYPE, type)
            data.putExtra(EXTRA_NAME, name)
            data.putExtra(EXTRA_BALANCE, initial_balance)
            data.putExtra(EXTRA_CURRENCY, currency)
            data.putExtra(EXTRA_DESCRIPTION, description)
            data.putExtra(EXTRA_INTEREST, interest)

            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }

    private fun setSaveEnable() {
        save_button.setEnabled(!editTextName?.text.isNullOrEmpty() && !currency_button?.text.toString().startsWith("*"))
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
