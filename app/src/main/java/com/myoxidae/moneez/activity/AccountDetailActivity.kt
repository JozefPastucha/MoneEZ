package com.myoxidae.moneez

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView
import com.myoxidae.moneez.activity.AddIncomeActivity
import com.myoxidae.moneez.activity.AddSpendingActivity
import com.myoxidae.moneez.activity.TransactionDetailActivity
import com.myoxidae.moneez.fragment.AccountListFragment
import com.myoxidae.moneez.fragment.AccountListFragment.Companion.ADD_INCOME_REQUEST
import com.myoxidae.moneez.fragment.AccountListFragment.Companion.ADD_SPENDING_REQUEST
import com.myoxidae.moneez.fragment.TransactionListFragment
import com.myoxidae.moneez.model.Account
import com.myoxidae.moneez.model.Category
import com.myoxidae.moneez.model.Transaction
import com.myoxidae.moneez.model.TransactionType

import kotlinx.android.synthetic.main.activity_account_detail.*
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder
import java.util.*

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

        setContentView(R.layout.activity_account_detail)
        setSupportActionBar(toolbar)

//        Configure speed dial
        val speedDial: SpeedDialView = findViewById(R.id.speedDial)
        configureSpeedDial(speedDial)

//        Set text

        val fn = transactionListViewModel.getAccount(intent.getLongExtra("accountId", -1))//error without def value...)

        val fntext = findViewById<TextView>(R.id.whatever)
        fntext.text = fn.value?.currentBalance.toString()



        //get account id from onListFragmentInteraction(item: Account?) and put it to TransactionListFragment's intent
        // Add list fragment
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
            ).setLabel(R.string.expenditure).create()
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
        speedDial.addActionItem(
            SpeedDialActionItem.Builder(
                R.id.cash_out,
                MaterialDrawableBuilder.with(this)
                    .setIcon(MaterialDrawableBuilder.IconValue.CASH)
                    .setColor(Color.WHITE)
                    .setToActionbarSize()
                    .build()
            ).setLabel(R.string.cash_out).create()
        )

//Do actions when speed dial items are clicked
        speedDial.setOnActionSelectedListener(SpeedDialView.OnActionSelectedListener { speedDialActionItem ->
            when (speedDialActionItem.id) {
                R.id.income -> {
                    val intent = Intent(this, AddIncomeActivity::class.java)
                    //start from fragment not activity
                    startActivityForResult(intent, ADD_INCOME_REQUEST)
                    false // true to keep the Speed Dial open
                }
                R.id.expenditure -> {
                    val intent = Intent(this, AddSpendingActivity::class.java)
                    //start from fragment not activity
                    startActivityForResult(intent, ADD_SPENDING_REQUEST)
                    false
                }
                R.id.bank_transfer -> {
                    Toast.makeText(this, "Bank transfer form!", Toast.LENGTH_SHORT).show()
                    false
                }
                R.id.cash_out -> {
                    Toast.makeText(this, "Cash out form!", Toast.LENGTH_SHORT).show()
                    false
                }
                else -> false
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


         if (requestCode == ADD_INCOME_REQUEST) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(this, "Income not saved", Toast.LENGTH_SHORT).show()
            } else {
                val description = data!!.getStringExtra(AddIncomeActivity.EXTRA_DESCRIPTION)
                val cal = Calendar.getInstance()
                cal.set(Calendar.YEAR, 1988)
                cal.set(Calendar.MONTH, Calendar.JANUARY)
                cal.set(Calendar.DAY_OF_MONTH, 3)

                /*cal.clear(Calendar.HOUR_OF_DAY);
                cal.clear(Calendar.AM_PM);
                cal.clear(Calendar.MINUTE);
                cal.clear(Calendar.SECOND);
                cal.clear(Calendar.MILLISECOND);
*/

                val date = cal.time

                val newTransaction =
                    Transaction(intent.getLongExtra("accountId", -1), 10.0, date, Category.Food, "title", description, TransactionType.Cash)
                transactionListViewModel.insertTransaction(newTransaction)
                //update account balance
                Toast.makeText(this, "Income saved", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == ADD_SPENDING_REQUEST) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(this, "Spending not saved", Toast.LENGTH_SHORT).show()
            } else {
                val description = data!!.getStringExtra(AddSpendingActivity.EXTRA_DESCRIPTION)
                val cal = Calendar.getInstance()
                cal.set(Calendar.YEAR, 1988)
                cal.set(Calendar.MONTH, Calendar.JANUARY)
                cal.set(Calendar.DAY_OF_MONTH, 3)

                /*cal.clear(Calendar.HOUR_OF_DAY);
                cal.clear(Calendar.AM_PM);
                cal.clear(Calendar.MINUTE);
                cal.clear(Calendar.SECOND);
                cal.clear(Calendar.MILLISECOND);
*/

                val date = cal.time
                val newTransaction =
                    Transaction(intent.getLongExtra("accountId", -1), -100.0, date, Category.Food, "title", description, TransactionType.Cash)
                transactionListViewModel?.insertTransaction(newTransaction)
                //update account balance
                Toast.makeText(this, "Spending saved", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
