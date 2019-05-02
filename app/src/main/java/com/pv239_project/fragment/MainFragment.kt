package com.pv239_project.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.pv239_project.AccountListAdapter
import com.pv239_project.R
import kotlinx.android.synthetic.main.fragment_main.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.pv239_project.AccountListViewModel
import com.pv239_project.activity.AddAccountActivity
import com.pv239_project.activity.AddIncomeActivity
import com.pv239_project.activity.AddSpendingActivity
import com.pv239_project.model.*
import kotlinx.android.synthetic.main.account_list_item.*
import java.util.*


class MainFragment : Fragment() {

    private var accountListViewModel: AccountListViewModel? = null

    companion object {
        @JvmField
        var ADD_ACCOUNT_REQUEST = 1  //@JvmField var or const val ??
        @JvmField
        var ADD_INCOME_REQUEST = 2
        @JvmField
        var ADD_SPENDING_REQUEST = 3

        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        button2.setOnClickListener {
            val intent = Intent(this.context, AddAccountActivity::class.java)
            //start from fragment not activity
            startActivityForResult(intent, 1)
        }
    }


    private fun initRecyclerView() {
        val adapter = AccountListAdapter(this)
        list_recycler.adapter = adapter

        accountListViewModel = ViewModelProviders.of(this).get(AccountListViewModel::class.java!!)
        accountListViewModel?.getAllAccounts()?.observe(this, //dalsie drbnute otazniky
            Observer<List<Account>> { accounts -> adapter.setAccounts(accounts) })
        // Don't forget to tell the RecyclerView how to show the items! (Linear - LinearLayoutManager, Grid - GridLayoutManager etc.)
        list_recycler.layoutManager = LinearLayoutManager(this.context)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_ACCOUNT_REQUEST) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(activity, "Account not saved", Toast.LENGTH_SHORT).show()
            } else {
                val name = data!!.getStringExtra(AddAccountActivity.EXTRA_NAME)
                val acc = Account(AccountType.Cash, name, "info", 0.0, 0.0, 0.0, "lol")
                accountListViewModel?.insert(acc)

                Toast.makeText(activity, "Account saved", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == ADD_INCOME_REQUEST) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(activity, "Income not saved", Toast.LENGTH_SHORT).show()
            } else {
                val description = data!!.getStringExtra(AddIncomeActivity.EXTRA_DESCRIPTION)
                val date = Date(1, 1, 1, 1, 1)
                val id = data.getLongExtra("id", -1)  //error without def value...

                val newTransaction =
                    Transaction(id, 10.0, date, Category.Food, "title", description, TransactionType.Cash)
                accountListViewModel?.insertTransaction(newTransaction)
                //update account balance
                Toast.makeText(activity, "Income saved", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == ADD_SPENDING_REQUEST) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(activity, "Spending not saved", Toast.LENGTH_SHORT).show()
            } else {
                val description = data!!.getStringExtra(AddSpendingActivity.EXTRA_DESCRIPTION)
                val date = Date(2000, 1, 1, 1, 1)
                val id = data.getLongExtra("id", -1) ///error without def value...

                val newTransaction =
                    Transaction(id, 10.0, date, Category.Food, "title", description, TransactionType.Cash)
                accountListViewModel?.insertTransaction(newTransaction)
                //update account balance
                Toast.makeText(activity, "Spending saved", Toast.LENGTH_SHORT).show()
            }
        }


    }
}
