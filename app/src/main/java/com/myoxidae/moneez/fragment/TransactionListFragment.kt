package com.myoxidae.moneez.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.myoxidae.moneez.*
import com.myoxidae.moneez.model.*
import kotlinx.android.synthetic.main.fragment_transaction_list.*
import java.text.SimpleDateFormat
import java.util.*


class TransactionListFragment : androidx.fragment.app.Fragment() {

    private var listener: OnListFragmentInteractionListener? = null
    // TODO: Customize parameters
    private var columnCount = 1

    private lateinit var transactionListViewModel: TransactionListViewModel
    private var accountId: Long = 0

    companion object {
        // TODO: Customize parameter argument names
        @JvmField
        var ARG_COLUMN_COUNT = "column-count"
        var ARG_ACCOUNT_ID = "account-id"
        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int, accountId: Long) =
            TransactionListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                    putLong(ARG_ACCOUNT_ID, accountId)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            accountId = it.getLong(ARG_ACCOUNT_ID)
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
        fun onListFragmentInteraction(item: TransactionWithCategoryData?)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transaction_list, container, false)

        // Set the adapter
        if (view is androidx.recyclerview.widget.RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> androidx.recyclerview.widget.LinearLayoutManager(context)
                    else -> androidx.recyclerview.widget.GridLayoutManager(context, columnCount)
                }
                adapter = TransactionListAdapter(listener, this@TransactionListFragment)
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
        val adapter = TransactionListAdapter(listener, this)
        list2.adapter = adapter

        transactionListViewModel = ViewModelProviders.of(this).get(TransactionListViewModel::class.java)
        transactionListViewModel?.getTransactions(accountId)?.observe(this,
            Observer<List<TransactionWithCategoryData>> { transactions -> adapter.setTransactions(transactions.sortedByDescending { it.date })
            })
        // Don't forget to tell the RecyclerView how to show the items! (Linear - LinearLayoutManager, Grid - GridLayoutManager etc.)
        list2.layoutManager = LinearLayoutManager(this.context)
    }
}
