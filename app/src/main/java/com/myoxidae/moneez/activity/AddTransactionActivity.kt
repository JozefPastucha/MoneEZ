package com.myoxidae.moneez.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputLayout
import com.myoxidae.moneez.R
import kotlinx.android.synthetic.main.activity_add_transaction.*
import kotlinx.android.synthetic.main.activity_add_transaction.save_button
import java.util.*
import android.app.TimePickerDialog
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.myoxidae.moneez.R.*
import com.myoxidae.moneez.model.RepeatType
import com.myoxidae.moneez.model.Transaction
import com.myoxidae.moneez.model.TransactionType
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder
import java.text.SimpleDateFormat


class AddTransactionActivity : AppCompatActivity() {
    private var inputLayoutName: TextInputLayout? = null
    private var inputLayoutAmount: TextInputLayout? = null

    private var editTextName: EditText? = null
    private var editTextAmount: EditText? = null
    private var editTextDescription: EditText? = null
    private var editTextCategory: EditText? = null
    private var editTextRecipient: EditText? = null
    private var editTextRepeat: EditText? = null

    private var spinnerRepeat: Spinner? = null

    private var date: Calendar = Calendar.getInstance()

    companion object {
        @JvmField
        var EXTRA_TYPE = "EXTRA_TYPE"
        @JvmField
        var EXTRA_ACCOUNT_ID = "EXTRA_ACCOUNT_ID"
        @JvmField
        var EXTRA_TRANSACTION = "EXTRA_TRANSACTION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_add_transaction)

        val type: TransactionType = intent.getSerializableExtra(EXTRA_TYPE) as TransactionType
        val id: Long = intent.getLongExtra(EXTRA_ACCOUNT_ID, -1)

        inputLayoutName = findViewById(R.id.input_layout_name)
        inputLayoutAmount = findViewById(R.id.input_layout_amount)

        editTextName = findViewById(R.id.edit_text_name)
        editTextAmount = findViewById(R.id.edit_text_amount)
        editTextDescription = findViewById(R.id.edit_text_description)
        //TODO circle_background
//        editTextCategory = findViewById(com.myoxidae.moneez.R.id.edit_text_category)
        //TODO bank transfer, withdrawal
        editTextRecipient = findViewById(R.id.edit_text_recipient)

        spinnerRepeat = findViewById(R.id.spinner_repeat)
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, RepeatType.values())
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinnerRepeat!!.adapter = aa
        spinnerRepeat!!.prompt = "Repeat"


//        Set toolbar - title and back button
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "New $type"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        date_button?.text = SimpleDateFormat("dd.MM.yyyy HH:mm").format(date.time)
        date_button?.setCompoundDrawablesWithIntrinsicBounds(
            MaterialDrawableBuilder.with(this)
                .setIcon(MaterialDrawableBuilder.IconValue.CALENDAR)
                .build(),
            null, null, null
        )

        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (!editTextName?.text.isNullOrEmpty() ||
                    !editTextAmount?.text.isNullOrEmpty() ||
                    !editTextDescription?.text.isNullOrEmpty() ||
                    !editTextRecipient?.text.isNullOrEmpty()
                ) {
                    showDialog()
                } else {
                    finish()
                }
            }
        })

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

        editTextAmount?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (editTextAmount?.text.isNullOrEmpty()) {
                    inputLayoutAmount?.setError("Name can't be empty")
                } else {
                    inputLayoutAmount?.setError(null)
                }
                setSaveEnable()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })

        editTextAmount?.setOnFocusChangeListener { v, hasFocus ->
            run {
                if (!hasFocus) {
                    if (editTextAmount?.text.isNullOrEmpty()) {
                        inputLayoutAmount?.setError("Amount can't be empty")
                    } else {
                        inputLayoutAmount?.setError(null)
                    }
                }
                setSaveEnable()
            }
        }

//        save
        save_button.setOnClickListener {
            val data = Intent()
            val name = editTextName?.text.toString()
            val amount = editTextAmount?.text.toString().toDouble()
            val description = editTextDescription?.text.toString()
//            val circle_background = editTextCategory?.text.toString()
            val recipient = editTextRecipient?.text.toString()
            val repeat = spinnerRepeat?.selectedItem as RepeatType

            if (name.trim().isEmpty()) {
                Toast.makeText(this, "Please insert a description", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newTransaction =
                Transaction(
                    id,
                    type,
                    name,
                    amount,
                    description,
                    date.time,
                    0,
                    repeat,
                    recipient
                )

            data.putExtra(EXTRA_TRANSACTION, newTransaction)

            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }

    fun showDatePickerDialog(view: View) {
        val currentDate = date
        date = Calendar.getInstance()
        DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            date.set(year, monthOfYear, dayOfMonth)
            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                date.set(Calendar.HOUR_OF_DAY, hourOfDay)
                date.set(Calendar.MINUTE, minute)

// TODO date format by locale
                date_button.text = SimpleDateFormat("dd.MM.yyyy HH:mm").format(date.time)
            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true).show()
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show()
    }


    private fun setSaveEnable() {
        save_button.setEnabled(!editTextName?.text.isNullOrEmpty() && !editTextAmount?.text.isNullOrEmpty())
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