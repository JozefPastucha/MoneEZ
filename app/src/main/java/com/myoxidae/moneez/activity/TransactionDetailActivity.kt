package com.myoxidae.moneez.activity

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mynameismidori.currencypicker.ExtendedCurrency
import com.myoxidae.moneez.R
import com.myoxidae.moneez.TransactionListViewModel
import com.myoxidae.moneez.fragment.AccountListFragment
import com.myoxidae.moneez.model.Account
import com.myoxidae.moneez.model.RepeatType
import com.myoxidae.moneez.model.Transaction
import kotlinx.android.synthetic.main.activity_transaction_detail.*

class TransactionDetailActivity : AppCompatActivity() {
    private lateinit var transactionListViewModel: TransactionListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_detail)
        transactionListViewModel = ViewModelProviders.of(this).get(TransactionListViewModel::class.java)


        val transactionId= intent.getLongExtra("transactionId", -1)

        transactionListViewModel.getTransaction(transactionId).observe(this,
            Observer<Transaction> { transaction ->
                if(transaction != null) {
//                    TODO currency
//                    TODO category
                    transactionListViewModel.transaction = transaction
                    transaction_amount.text = transactionListViewModel.transaction?.amount.toString()
                    transaction_title.text = transactionListViewModel.transaction?.name.toString()
                    transaction_date.text = transactionListViewModel.transaction?.date.toString()
                }
            })


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
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_delete -> {
                deleteDialog()
                true
            }
            R.id.action_edit -> {
                val intent = Intent(this, AddTransactionActivity::class.java)
                val transaction= transactionListViewModel.transaction
                intent.putExtra(AddTransactionActivity.EXTRA_TRANSACTION, transaction)
                intent.putExtra(AddAccountActivity.EXTRA_ACCOUNT, transaction?.accountId)
                intent.putExtra(AddAccountActivity.EXTRA_TYPE, transaction?.type)
                startActivityForResult(intent, AccountListFragment.ADD_TRANSACTION_REQUEST)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AccountListFragment.ADD_TRANSACTION_REQUEST) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(this, "Transaction not saved", Toast.LENGTH_SHORT).show()
            } else {
                val transaction = data!!.getParcelableExtra(AddTransactionActivity.EXTRA_TRANSACTION) as Transaction
                if(transaction.repeat != RepeatType.None) {
                    //it would be better to save new transaction within insertTransactionPlan and use
                    //database transactions for atomicity when setting lastTime
//                    TODO remove old transaction plan
                    transactionListViewModel.insertTransactionPlan(transaction)
                }
                transactionListViewModel.updateTransaction(transaction)
                Toast.makeText(this, "Transaction saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteDialog() {
        lateinit var dialog: AlertDialog

        val builder = AlertDialog.Builder(this)

        builder.setMessage("Are you sure you want to delete this transaction?")

        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    transactionListViewModel.deleteTransaction()
                    finish()
                }
            }
        }

        builder.setPositiveButton("Yes", dialogClickListener)
        builder.setNegativeButton("No", dialogClickListener)

        dialog = builder.create()
        dialog.show()
    }

}
