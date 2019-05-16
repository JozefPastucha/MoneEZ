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
import android.opengl.Visibility
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.myoxidae.moneez.R.*
import com.myoxidae.moneez.fragment.AccountListFragment
import com.myoxidae.moneez.model.AccountType
import com.myoxidae.moneez.model.RepeatType
import com.myoxidae.moneez.model.Transaction
import com.myoxidae.moneez.model.TransactionType
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder
import java.text.SimpleDateFormat


class AddTransactionActivity : AppCompatActivity() {
    private var inputLayoutName: TextInputLayout? = null
    private var inputLayoutAmount: TextInputLayout? = null
    private var inputLayoutReceivedAmount: TextInputLayout? = null

    private var editTextName: EditText? = null
    private var editTextAmount: EditText? = null
    private var editTextReceivedAmount: EditText? = null
    private var editTextDescription: EditText? = null
    private var editTextCategory: EditText? = null
    private var editTextRecipient: EditText? = null

    private var spinnerRepeat: Spinner? = null
    private var spinnerRecipient: Spinner? = null

    private var date: Calendar = Calendar.getInstance()

    companion object {
        @JvmField
        var EXTRA_TYPE = "EXTRA_TYPE"
        @JvmField
        var EXTRA_ACCOUNT_ID = "EXTRA_ACCOUNT_ID"
        @JvmField
        var EXTRA_TRANSACTION = "EXTRA_TRANSACTION"
        @JvmField
        var EXTRA_TRANSFER = "EXTRA_TRANSFER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_add_transaction)

        var type: TransactionType = intent.getSerializableExtra(EXTRA_TYPE) as TransactionType
        val id: Long = intent.getLongExtra(EXTRA_ACCOUNT_ID, -1)

        inputLayoutName = findViewById(R.id.input_layout_name)
        inputLayoutAmount = findViewById(R.id.input_layout_amount)
        inputLayoutReceivedAmount = findViewById(R.id.input_layout_amount_received)

        editTextName = findViewById(R.id.edit_text_name)
        editTextAmount = findViewById(R.id.edit_text_amount)
        editTextReceivedAmount = findViewById(R.id.edit_text_amount_received)
        editTextDescription = findViewById(R.id.edit_text_description)
        //TODO category
//        editTextCategory = findViewById(com.myoxidae.moneez.R.id.edit_text_category)
        editTextRecipient = findViewById(R.id.edit_text_recipient)

        spinnerRepeat = findViewById(R.id.spinner_repeat)
        spinnerRecipient = findViewById(R.id.spinner_recipient)


        // Set items for repeat spinner
        val repeatSpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, RepeatType.values())
        repeatSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRepeat!!.adapter = repeatSpinnerAdapter
        spinnerRepeat!!.prompt = "Repeat"


//        Don't need name for withdrawal and transfer
        if (type == TransactionType.Transfer || type == TransactionType.Withdrawal) {
            editTextRecipient?.visibility = View.GONE
            editTextName?.setText(type.toString())
            editTextName?.visibility = View.GONE
        }

        if (type == TransactionType.Transfer) {
            inputLayoutReceivedAmount?.visibility = View.VISIBLE
            spinnerRecipient?.visibility = View.VISIBLE
//            TODO check if has other accounts
//            TODO get actual accounts
//            TODO show currency in list of accounts
            val accountsWithSameCurrency = arrayOf("account1", "account2")
            val recipientSpinnerAdapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, accountsWithSameCurrency)
            recipientSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerRecipient!!.adapter = recipientSpinnerAdapter
            spinnerRecipient!!.prompt = "Choose account"
        }

        if (type == TransactionType.Withdrawal) {
//          TODO check if has cash account
//          TODO check again when he closes CreateAccount activity
            val hasCashAcc = false
            if (!hasCashAcc) {
                noCashAccountDialog()
            }
        }


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
//            TODO get recipient from spinner or cash account
            val recipient = editTextRecipient?.text.toString()
//            TODO get category id
            val categoryId: Long = 0
//            TODO get other account ID
            val otherAccountId: Long = 0
            val repeat = spinnerRepeat?.selectedItem as RepeatType

            if (type == TransactionType.Transfer) {
//                TODO set category transfer
            } else if (type == TransactionType.Withdrawal) {
//                TODO set category withdraw
            }

            if (type == TransactionType.Withdrawal || type == TransactionType.Transfer) {
                var receivedAmount = editTextReceivedAmount?.text.toString().toDouble()
                if (receivedAmount == 0.0) receivedAmount = amount

                type = TransactionType.Spending

                val newTransfer =
                    Transaction(
                        otherAccountId,
                        TransactionType.Income,
                        name,
                        receivedAmount,
                        description,
                        date.time,
                        categoryId,
                        RepeatType.None,
                        recipient
                    )

                data.putExtra(EXTRA_TRANSFER, newTransfer)
            }

            val newTransaction =
                Transaction(
                    id,
                    type,
                    name,
                    amount,
                    description,
                    date.time,
                    categoryId,
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

    private fun noCashAccountDialog() {
        lateinit var dialog: AlertDialog

        val builder = AlertDialog.Builder(this)

        builder.setMessage("You don't have any cash account. Do you want to create one?")

        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> run {

                    //                    TODO create cash account
                    val accintent = Intent(this, AddAccountActivity::class.java)
                    accintent.putExtra(AddAccountActivity.EXTRA_TYPE, AccountType.Cash)
                    startActivityForResult(accintent, AccountListFragment.ADD_ACCOUNT_REQUEST)
                }

                DialogInterface.BUTTON_NEGATIVE -> finish()
            }
        }

        builder.setPositiveButton("Yes", dialogClickListener)
        builder.setNegativeButton("Cancel", dialogClickListener)

        dialog = builder.create()
        dialog.show()
    }
}