package com.myoxidae.moneez.activity

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mynameismidori.currencypicker.ExtendedCurrency
import com.myoxidae.moneez.R
import com.myoxidae.moneez.TransactionListViewModel
import com.myoxidae.moneez.fragment.AccountListFragment
import com.myoxidae.moneez.model.*
import kotlinx.android.synthetic.main.activity_transaction_detail.*
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder
import java.text.SimpleDateFormat

class TransactionDetailActivity : AppCompatActivity() {
    private lateinit var transactionListViewModel: TransactionListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_detail)
        transactionListViewModel = ViewModelProviders.of(this).get(TransactionListViewModel::class.java)


        val transactionId= intent.getLongExtra("transactionId", -1)

        transactionListViewModel.getTransaction(transactionId).observe(this,
            Observer<TransactionWithCategoryData> { transaction ->
                if(transaction != null) {
//                    TODO currency
                    transactionListViewModel.transactionWithCategory = transaction
                    if (transactionListViewModel.transactionWithCategory?.type == TransactionType.Income) {
                        transaction_amount.setTextColor(ContextCompat.getColor(this, R.color.colorSuccess))
                        transaction_amount.text = "+" + transactionListViewModel.transactionWithCategory?.amount.toString()
                    } else if (transactionListViewModel.transaction?.type == TransactionType.Spending) {
                        transaction_amount.setTextColor(ContextCompat.getColor(this, R.color.colorDanger))
                        transaction_amount.text = "-" + transactionListViewModel.transactionWithCategory?.amount.toString()
                    }
                    transaction_title.text = transactionListViewModel.transactionWithCategory?.name.toString()
                    transaction_date.text = SimpleDateFormat("dd.MM.yyyy").format(transactionListViewModel.transactionWithCategory?.date)
                    transaction_description.text = transactionListViewModel.transactionWithCategory?.description.toString()
                    if (transactionListViewModel.transactionWithCategory?.recipient.toString().isNotEmpty()) {
                        transaction_recipient.text =
                            getString(R.string.recipient) + transactionListViewModel.transactionWithCategory?.recipient.toString()
                    }


                    val colorFromCategory = transactionListViewModel.transactionWithCategory?.cColor
                    val icon: MaterialDrawableBuilder.IconValue = MaterialDrawableBuilder.IconValue.valueOf(transactionListViewModel.transactionWithCategory!!.cIcon)

                    val iconColor = Color.parseColor("#$colorFromCategory")
                    val categoryBg = category_icon.background as GradientDrawable

                    categoryBg.setColor(Color.parseColor("#33$colorFromCategory"))
                    category_icon.setIcon(icon)
                    category_icon.setColor(iconColor)
                    category_name.text = transactionListViewModel.transactionWithCategory?.cName.toString()
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
                intent.putExtra(AddTransactionActivity.EXTRA_ACCOUNT_ID, transaction?.accountId)
                intent.putExtra(AddTransactionActivity.EXTRA_TYPE, transaction?.type)
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
                Toast.makeText(this, getString(R.string.transaction) + getString(R.string.not_saved), Toast.LENGTH_SHORT).show()
            } else {
                val transaction = data!!.getParcelableExtra(AddTransactionActivity.EXTRA_TRANSACTION) as Transaction
                transaction.transactionId = intent.getLongExtra("transactionId", -1)
                if(transaction.repeat != RepeatType.None) {
                    //it would be better to save new transaction within insertTransactionPlan and use
                    //database transactions for atomicity when setting lastTime
//                    TODO remove old transaction plan
                    transactionListViewModel.insertTransactionPlan(transaction)
                }
                transactionListViewModel.updateTransaction(transaction)
                Toast.makeText(this, getString(R.string.transaction) + getString(R.string.saved), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteDialog() {
        lateinit var dialog: AlertDialog

        val builder = AlertDialog.Builder(this)

        builder.setMessage(getString(R.string.delete_transaction_alert))

        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    transactionListViewModel.deleteTransaction()
                    finish()
                }
            }
        }

        builder.setPositiveButton(getString(R.string.yes), dialogClickListener)
        builder.setNegativeButton(getString(R.string.no), dialogClickListener)

        dialog = builder.create()
        dialog.show()
    }

}
