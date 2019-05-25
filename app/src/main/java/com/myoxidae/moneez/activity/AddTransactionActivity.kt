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
import androidx.lifecycle.ViewModelProviders
import com.mynameismidori.currencypicker.ExtendedCurrency
import com.myoxidae.moneez.AccountListViewModel
import com.myoxidae.moneez.R.*
import com.myoxidae.moneez.fragment.AccountListFragment
import com.myoxidae.moneez.model.*
import com.myoxidae.moneez.picker.accountpicker.AccountPicker
import com.myoxidae.moneez.picker.categorypicker.CategoryPicker
import com.myoxidae.moneez.picker.iconpicker.IconPicker
import kotlinx.android.synthetic.main.activity_add_account.*
import kotlinx.android.synthetic.main.activity_add_category.*
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder
import java.text.SimpleDateFormat


class AddTransactionActivity : AppCompatActivity() {
    private var accountListViewModel: AccountListViewModel? = null

    private var inputLayoutName: TextInputLayout? = null
    private var inputLayoutAmount: TextInputLayout? = null
    private var inputLayoutReceivedAmount: TextInputLayout? = null
    private var inputLayoutRecipient: TextInputLayout? = null

    private var editTextName: EditText? = null
    private var editTextAmount: EditText? = null
    private var editTextReceivedAmount: EditText? = null
    private var editTextDescription: EditText? = null
    private var editTextCategory: EditText? = null
    private var editTextRecipient: EditText? = null

    private var spinnerRepeat: Spinner? = null

    private var category: Category? = null
    private var recipientAccount: Account? = null
    private var date: Calendar = Calendar.getInstance()
    private var type: TransactionType? = null

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
        setContentView(R.layout.activity_add_transaction)


        type = intent.getSerializableExtra(EXTRA_TYPE) as TransactionType
        val id: Long = intent.getLongExtra(EXTRA_ACCOUNT_ID, -1)

        inputLayoutName = findViewById(R.id.input_layout_name)
        inputLayoutAmount = findViewById(R.id.input_layout_amount)
        inputLayoutReceivedAmount = findViewById(R.id.input_layout_amount_received)
        inputLayoutRecipient = findViewById(R.id.input_layout_recipient)

        editTextName = findViewById(R.id.edit_text_name)
        editTextAmount = findViewById(R.id.edit_text_amount)
        editTextReceivedAmount = findViewById(R.id.edit_text_amount_received)
        editTextDescription = findViewById(R.id.edit_text_description)
        editTextRecipient = findViewById(R.id.edit_text_recipient)

        spinnerRepeat = findViewById(R.id.spinner_repeat)


        // Set items for repeat spinner
        val repeatSpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, RepeatType.values())
        repeatSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRepeat!!.adapter = repeatSpinnerAdapter
        spinnerRepeat!!.prompt = getString(R.string.repeat)


//        Don't need name for withdrawal and transfer
        if (type == TransactionType.Transfer || type == TransactionType.Withdrawal) {
            inputLayoutRecipient?.visibility = View.GONE
            editTextCategory?.visibility = View.GONE
            editTextName?.setText(type.toString())
            editTextName?.visibility = View.GONE
        }

        if (type == TransactionType.Transfer) {
            inputLayoutReceivedAmount?.visibility = View.VISIBLE
            button_recipient?.visibility = View.VISIBLE

            accountListViewModel = ViewModelProviders.of(this).get(AccountListViewModel::class.java)
            val accountList = accountListViewModel?.getAccountsList()!!.toCollection(ArrayList())
            if (accountList.size <= 1) {
                noOtherAccountDialog()
            } else {
                var accid = 0
                for (i in 1..accountList.size) {
                    if (accountList[i - 1].accountId == id) {
                        accid = i - 1
                    }
                }
                accountList.removeAt(accid)
            }

            //        account recipient select
            button_recipient?.setCompoundDrawablesWithIntrinsicBounds(
                MaterialDrawableBuilder.with(this)
                    .setIcon(MaterialDrawableBuilder.IconValue.BANK)
                    .build(),
                null, null, null
            )
            button_recipient?.setOnClickListener {
                val picker = AccountPicker.newInstance(getString(R.string.select_account), accountList)  // dialog title
                picker.setListener { account ->
                    button_recipient?.text = account.name
                    val currency = ExtendedCurrency.getCurrencyByName(account.currency)
                    button_recipient?.setCompoundDrawablesWithIntrinsicBounds(
                        getDrawable(currency.flag),
                        null, null, null
                    )
                    this.recipientAccount = account
                    picker.dismiss()
                    setSaveEnable()
                }
                picker.show(supportFragmentManager, "ACCOUNT_PICKER")
            }

            button_category?.visibility = View.GONE
        }

//        if (type == TransactionType.Withdrawal) {
////          check if has cash account
////          check again when he closes CreateAccount activity
//            val hasCashAcc = false
//            if (!hasCashAcc) {
//                noCashAccountDialog()
//            }
//        }

//        category select
        button_category.setCompoundDrawablesWithIntrinsicBounds(
            MaterialDrawableBuilder.with(this)
                .setIcon(MaterialDrawableBuilder.IconValue.SHAPE)
                .build(),
            null, null, null
        )
        button_category?.setOnClickListener {
            val picker = CategoryPicker.newInstance(getString(R.string.select_category))  // dialog title
            picker.setListener { category ->
                button_category?.text = category.name
                button_category?.setCompoundDrawablesWithIntrinsicBounds(
                    MaterialDrawableBuilder.with(this)
                        .setIcon(MaterialDrawableBuilder.IconValue.valueOf(category.icon))
                        .setToActionbarSize()
                        .build(),
                    null, null, null
                )
                this.category = category
                picker.dismiss()
            }
            picker.show(supportFragmentManager, "CATEGORY_PICKER")
        }

//        Set toolbar - title and back button
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "New $type"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

//        TODO date format by locale
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
                    inputLayoutName?.setError(getString(R.string.name_empty_error))
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
                        inputLayoutName?.setError(getString(R.string.name_empty_error))
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
                    inputLayoutAmount?.setError(getString(R.string.name_empty_error))
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
                        inputLayoutAmount?.setError(getString(R.string.amount_empty_error))
                    } else {
                        inputLayoutAmount?.setError(null)
                    }
                }
                setSaveEnable()
            }
        }

        if (intent.hasExtra(AddTransactionActivity.EXTRA_TRANSACTION)) {
            val transaction = intent.getParcelableExtra(AddTransactionActivity.EXTRA_TRANSACTION) as Transaction
            editTextName?.setText(transaction.name)
            editTextAmount?.setText(transaction.amount.toString())
            editTextDescription?.setText(transaction.description)
            date.time = transaction.date
            editTextRecipient?.setText(transaction.recipient)
//            TODO repeat
//TODO category
//            button_category?.text = "insert category name here"
            date_button.text = SimpleDateFormat("dd.MM.yyyy HH:mm").format(transaction.date)

//            currency_button?.setCompoundDrawablesWithIntrinsicBounds(
//                getDrawable(currency.flag),
//                null, null, null
//            )
        }

//        save
        save_button.setOnClickListener {
            val data = Intent()
            val name = editTextName?.text.toString()
            val amount = editTextAmount?.text.toString().toDouble()
            var categoryId: Long = 1
            val description = editTextDescription?.text.toString()
//            val circle_background = editTextCategory?.text.toString()
            val recipient = editTextRecipient?.text.toString()

            val repeat = spinnerRepeat?.selectedItem as RepeatType

            if (category != null) {
                categoryId = category!!.categoryId
            }

            if (type == TransactionType.Withdrawal || type == TransactionType.Transfer) {
                var receivedAmount = editTextReceivedAmount?.text.toString().toDouble()
                if (receivedAmount == 0.0) receivedAmount = amount

                type = TransactionType.Spending
                categoryId = 0

                val newTransfer =
                    Transaction(
                        recipientAccount!!.accountId,
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
                    type!!,
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
        if (type!! == TransactionType.Transfer) {

            save_button.setEnabled(recipientAccount != null && !editTextAmount?.text.isNullOrEmpty())
        } else {
            save_button.setEnabled(!editTextName?.text.isNullOrEmpty() && !editTextAmount?.text.isNullOrEmpty())
        }
    }

    private fun showDialog() {
        lateinit var dialog: AlertDialog

        val builder = AlertDialog.Builder(this)

        builder.setMessage(getString(R.string.unsaved_changes_alert))

        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_NEGATIVE -> finish()
            }
        }

        builder.setPositiveButton(getString(R.string.keep_editing), dialogClickListener)
        builder.setNegativeButton(getString(R.string.discard), dialogClickListener)

        dialog = builder.create()
        dialog.show()
    }

    private fun noOtherAccountDialog() {
        lateinit var dialog: AlertDialog

        val builder = AlertDialog.Builder(this)

        builder.setMessage(getString(R.string.no_account_alert))

        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_NEUTRAL -> finish()
            }
        }

        builder.setNeutralButton(getString(R.string.ok), dialogClickListener)

        dialog = builder.create()
        dialog.show()
    }
}