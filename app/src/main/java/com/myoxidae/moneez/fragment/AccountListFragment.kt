package com.myoxidae.moneez.fragment

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.myoxidae.moneez.AccountListAdapter
import com.myoxidae.moneez.R
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.myoxidae.moneez.AccountListViewModel
import com.myoxidae.moneez.activity.AddAccountActivity
import com.myoxidae.moneez.activity.AddIncomeActivity
import com.myoxidae.moneez.activity.AddSpendingActivity
import com.myoxidae.moneez.content.AccountContent
import com.myoxidae.moneez.model.*
import kotlinx.android.synthetic.main.fragment_account_list.*
import java.util.*


class AccountListFragment : androidx.fragment.app.Fragment() {

    private var listener: OnListFragmentInteractionListener? = null
    // TODO: Customize parameters
    private var columnCount = 1

    private var accountListViewModel: AccountListViewModel? = null

    companion object {
        @JvmField
        var ADD_ACCOUNT_REQUEST = 1  //@JvmField var or const val ??
        @JvmField
        var ADD_INCOME_REQUEST = 2
        @JvmField
        var ADD_SPENDING_REQUEST = 3

        // TODO: Customize parameter argument names
        @JvmField
        var ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            AccountListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: Account?)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account_list, container, false)

        // Set the adapter
        if (view is androidx.recyclerview.widget.RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> androidx.recyclerview.widget.LinearLayoutManager(context)
                    else -> androidx.recyclerview.widget.GridLayoutManager(context, columnCount)
                }
                adapter = AccountListAdapter(listener, this@AccountListFragment)
            }
        }
        return view
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        /*button2.setOnClickListener {
            val intent = Intent(this.context, AddAccountActivity::class.java)
            //start from fragment not activity
            startActivityForResult(intent, ADD_ACCOUNT_REQUEST)
        }*/
    }


    private fun initRecyclerView() {
        val adapter = AccountListAdapter(listener, this)
        list.adapter = adapter

        accountListViewModel = ViewModelProviders.of(this).get(AccountListViewModel::class.java)
        accountListViewModel?.getAllAccounts()?.observe(this, //dalsie drbnute otazniky
            Observer<List<Account>> { accounts -> adapter.setAccounts(accounts) })
        // Don't forget to tell the RecyclerView how to show the items! (Linear - LinearLayoutManager, Grid - GridLayoutManager etc.)
        list.layoutManager = LinearLayoutManager(this.context)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        /*if (requestCode == ADD_ACCOUNT_REQUEST) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(activity, "Account not saved", Toast.LENGTH_SHORT).show()
            } else {
                val name = data!!.getStringExtra(AddAccountActivity.EXTRA_NAME)
                val acc = Account(AccountType.Cash, name, "info", 0.0, 0.0, 0.0, "lol")
                accountListViewModel?.insert(acc)

                Toast.makeText(activity, "Account saved", Toast.LENGTH_SHORT).show()
            }
        } else*/ if (requestCode == ADD_INCOME_REQUEST) {
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
                    Transaction(id, -100.0, date, Category.Food, "title", description, TransactionType.Cash)
                accountListViewModel?.insertTransaction(newTransaction)
                //update account balance
                Toast.makeText(activity, "Spending saved", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
