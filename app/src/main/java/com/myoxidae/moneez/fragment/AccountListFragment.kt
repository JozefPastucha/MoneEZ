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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.myoxidae.moneez.*
import com.myoxidae.moneez.activity.AddTransactionActivity
import com.myoxidae.moneez.model.*
import kotlinx.android.synthetic.main.fragment_account_list.*
import java.text.SimpleDateFormat
import java.util.*


class AccountListFragment : androidx.fragment.app.Fragment() {

    private var listener: OnListFragmentInteractionListener? = null
    // TODO: Customize parameters
    private var columnCount = 1

    private var accountListViewModel: AccountListViewModel? = null

    companion object {
        @JvmField
        var ADD_ACCOUNT_REQUEST = 0  //@JvmField var or const val ?? //move this into resources, used also in accountDet
        @JvmField
        var ADD_TRANSACTION_REQUEST = 1

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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
    }


    private fun initRecyclerView() {
        val adapter = AccountListAdapter(listener, this)
        list.adapter = adapter

        accountListViewModel = ViewModelProviders.of(this).get(AccountListViewModel::class.java)
        accountListViewModel?.getAllAccounts()?.observe(this,
            Observer<List<Account>> { accounts -> adapter.setAccounts(accounts) })
        // Don't forget to tell the RecyclerView how to show the items! (Linear - LinearLayoutManager, Grid - GridLayoutManager etc.)
        list.layoutManager = LinearLayoutManager(this.context)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_TRANSACTION_REQUEST) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(activity, getString(R.string.transaction) + " " + getString(R.string.not_saved), Toast.LENGTH_SHORT).show()
            } else {
                val newTransaction = data!!.getParcelableExtra(AddTransactionActivity.EXTRA_TRANSACTION) as Transaction
                accountListViewModel?.insertTransaction(newTransaction)
                if (newTransaction.repeat != RepeatType.None) {
                    //if the transaction was in the past, add transaction until today and save plan
                    AddPastTransactionsAndNewPlan.addPastTransactionsAndNewPlan(newTransaction, activity!!.application)
                    Toast.makeText(activity, getString(R.string.transaction) + " " + getString(R.string.saved), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
