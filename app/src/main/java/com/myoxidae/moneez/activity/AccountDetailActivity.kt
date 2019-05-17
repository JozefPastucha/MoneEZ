package com.myoxidae.moneez

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView
import com.mynameismidori.currencypicker.ExtendedCurrency
import com.myoxidae.moneez.activity.AddAccountActivity
import com.myoxidae.moneez.activity.AddTransactionActivity
import com.myoxidae.moneez.activity.MainActivity
import com.myoxidae.moneez.activity.TransactionDetailActivity
import com.myoxidae.moneez.fragment.AccountListFragment
import com.myoxidae.moneez.fragment.AccountListFragment.Companion.ADD_TRANSACTION_REQUEST
import com.myoxidae.moneez.fragment.TransactionListFragment
import com.myoxidae.moneez.model.*
import kotlinx.android.synthetic.main.activity_account_detail.*

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder


class AccountDetailActivity : AppCompatActivity(), TransactionListFragment.OnListFragmentInteractionListener {

    private lateinit var transactionListViewModel: TransactionListViewModel

    override fun onListFragmentInteraction(item: Transaction?) {
        val intent = Intent(this, TransactionDetailActivity::class.java)
        intent.putExtra("item", item.toString())
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transactionListViewModel = ViewModelProviders.of(this).get(TransactionListViewModel::class.java)

        val accountId = intent.getLongExtra("accountId", -1)

        transactionListViewModel.getAccount(accountId).observe(this,
        Observer<Account>{ account -> transactionListViewModel.account = account
            val currency = ExtendedCurrency.getCurrencyByName(account.currency)
            account_value.text = transactionListViewModel.account?.currentBalance.toString() + currency.symbol
            account_name.text = transactionListViewModel.account?.name.toString()
            account_type.text = transactionListViewModel.account?.type.toString() + " account"
        })


        setContentView(R.layout.activity_account_detail)
        val a: TextView = findViewById(R.id.account_name)

        a.text = transactionListViewModel.account?.name
        account_value.text = transactionListViewModel.account?.currentBalance.toString()

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                finish()
            }
        })

//        Configure speed dial
        val speedDial: SpeedDialView = findViewById(R.id.speedDial)
        configureSpeedDial(speedDial)

//        Set text


//        val fntext = findViewById<TextView>(R.id.whatever)
//        fntext.text = fn.value?.currentBalance.toString()


        //get account id from onListFragmentInteraction(item: Account?) and put it to TransactionListFragment's intent
//         Add list fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.account_detail_content, //do i need id in layout?
                    TransactionListFragment.newInstance(
                        1,
                        intent.getLongExtra("accountId", -1)//error without def value...
                    ),
                    "TransactionList"
                ).commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun configureSpeedDial(speedDial: SpeedDialView) {
        speedDial.setMainFabClosedDrawable(
            MaterialDrawableBuilder.with(this)
                .setIcon(MaterialDrawableBuilder.IconValue.PLUS)
                .setColor(Color.WHITE)
                .setToActionbarSize()
                .build()
        )

        speedDial.addActionItem(
            SpeedDialActionItem.Builder(
                R.id.income,
                MaterialDrawableBuilder.with(this)
                    .setIcon(MaterialDrawableBuilder.IconValue.PLUS)
                    .setColor(Color.WHITE)
                    .setToActionbarSize()
                    .build()
            ).setLabel(R.string.income).create()
        )
        speedDial.addActionItem(
            SpeedDialActionItem.Builder(
                R.id.expenditure,
                MaterialDrawableBuilder.with(this)
                    .setIcon(MaterialDrawableBuilder.IconValue.MINUS)
                    .setColor(Color.WHITE)
                    .setToActionbarSize()
                    .build()
            ).setLabel(R.string.spending).create()
        )
        speedDial.addActionItem(
            SpeedDialActionItem.Builder(
                R.id.bank_transfer,
                MaterialDrawableBuilder.with(this)
                    .setIcon(MaterialDrawableBuilder.IconValue.BANK)
                    .setColor(Color.WHITE)
                    .setToActionbarSize()
                    .build()
            ).setLabel(R.string.bank_transfer).create()
        )
//        speedDial.addActionItem(
//            SpeedDialActionItem.Builder(
//                R.id.cash_out,
//                MaterialDrawableBuilder.with(this)
//                    .setIcon(MaterialDrawableBuilder.IconValue.CASH)
//                    .setColor(Color.WHITE)
//                    .setToActionbarSize()
//                    .build()
//            ).setLabel(R.string.cash_out).create()
//        )

//Do actions when speed dial items are clicked
        speedDial.setOnActionSelectedListener(SpeedDialView.OnActionSelectedListener { speedDialActionItem ->
            when (speedDialActionItem.id) {
                R.id.income -> {
                    val intent = Intent(this, AddTransactionActivity::class.java)
                    intent.putExtra(AddTransactionActivity.EXTRA_ACCOUNT_ID, transactionListViewModel.account?.accountId)
                    intent.putExtra(AddTransactionActivity.EXTRA_TYPE, TransactionType.Income)
                    startActivityForResult(intent, ADD_TRANSACTION_REQUEST)
                    false // true to keep the Speed Dial open
                }
                R.id.expenditure -> {
                    val intent = Intent(this, AddTransactionActivity::class.java)
                    intent.putExtra(AddTransactionActivity.EXTRA_ACCOUNT_ID, transactionListViewModel.account?.accountId)
                    intent.putExtra(AddTransactionActivity.EXTRA_TYPE, TransactionType.Spending)
                    startActivityForResult(intent, ADD_TRANSACTION_REQUEST)
                    false
                }
                R.id.bank_transfer -> {
                    val intent = Intent(this, AddTransactionActivity::class.java)
                    intent.putExtra(AddTransactionActivity.EXTRA_ACCOUNT_ID, transactionListViewModel.account?.accountId)
                    intent.putExtra(AddTransactionActivity.EXTRA_TYPE, TransactionType.Transfer)
                    startActivityForResult(intent, ADD_TRANSACTION_REQUEST)
                    false
                }
//                R.id.cash_out -> {
//                    val intent = Intent(this, AddTransactionActivity::class.java)
//                    intent.putExtra(AddTransactionActivity.EXTRA_ACCOUNT_ID, transactionListViewModel.account?.accountId)
//                    intent.putExtra(AddTransactionActivity.EXTRA_TYPE, TransactionType.Withdrawal)
//                    startActivityForResult(intent, ADD_TRANSACTION_REQUEST)
//
//                    false
//                }
                else -> false
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_TRANSACTION_REQUEST) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(this, "Transaction not saved", Toast.LENGTH_SHORT).show()
            } else {
                val newTransaction = data!!.getParcelableExtra(AddTransactionActivity.EXTRA_TRANSACTION) as Transaction
                transactionListViewModel.insertTransaction(newTransaction)
                if (data!!.hasExtra(AddTransactionActivity.EXTRA_TRANSFER)) {
                    val newTransfer = data!!.getParcelableExtra(AddTransactionActivity.EXTRA_TRANSFER) as Transaction
                    transactionListViewModel.insertTransaction(newTransfer)
                }
                    //TODO update account balance
                Toast.makeText(this, "Transaction saved", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
