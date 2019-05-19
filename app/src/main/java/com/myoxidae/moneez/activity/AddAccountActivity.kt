package com.myoxidae.moneez.activity

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_add_account.*
import com.mynameismidori.currencypicker.CurrencyPicker
import com.mynameismidori.currencypicker.ExtendedCurrency
import com.myoxidae.moneez.model.Account
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
        var EXTRA_ACCOUNT = "EXTRA_ACCOUNT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.myoxidae.moneez.R.layout.activity_add_account)

        val type: AccountType = intent.getSerializableExtra(EXTRA_TYPE) as AccountType

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

        // if editing account fill forms
        if (intent.hasExtra(EXTRA_ACCOUNT)) {
            val account = intent.getParcelableExtra(EXTRA_ACCOUNT) as Account
            val currency = ExtendedCurrency.getCurrencyByName(account.currency)
            editTextName?.setText(account.name)
            editTextBalance?.setText(account.initialBalance.toString())
            editTextDescription?.setText(account.description)
            editTextInterest?.setText(account.interest.toString())

            currency_button?.text = currency.name
            currency_button?.setCompoundDrawablesWithIntrinsicBounds(
                getDrawable(currency.flag),
                null, null, null
            )
        }

//        save
        save_button.setOnClickListener {
            val data = Intent()
            val name = editTextName?.text.toString()
            val description = editTextDescription?.text.toString()
            val initialBalance = editTextBalance?.text.toString().toDouble()
            val interest = editTextInterest?.text.toString().toDouble()
            val currency = currency_button?.text.toString()

            val acc = Account(type, name, description, initialBalance, initialBalance, interest, currency)

            data.putExtra(EXTRA_ACCOUNT, acc)
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
