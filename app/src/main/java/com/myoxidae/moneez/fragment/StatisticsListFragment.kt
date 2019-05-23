package com.myoxidae.moneez.fragment

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.TypedArrayUtils.getDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mynameismidori.currencypicker.ExtendedCurrency
import com.myoxidae.moneez.*
import com.myoxidae.moneez.model.*
import com.myoxidae.moneez.picker.accountpicker.AccountPicker
import kotlinx.android.synthetic.main.activity_add_transaction.*
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account_list.*
import kotlinx.android.synthetic.main.fragment_stat_category_list.*
import kotlinx.android.synthetic.main.fragment_stat_category_list.view.*
import kotlinx.android.synthetic.main.fragment_transaction_list.*
import java.text.SimpleDateFormat
import java.util.*


class StatisticsListFragment : androidx.fragment.app.Fragment() {
    private var accountListViewModel: AccountListViewModel? = null

    // TODO: Customize parameters
    private var columnCount = 1

    private lateinit var viewModel: StatisticsViewModel

    companion object {
        // TODO: Customize parameter argument names
        @JvmField
        var ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int, accountId: Long) =
            StatisticsListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //why this count of argument declaration and definition?
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stat_category_list, container, false)

        accountListViewModel = ViewModelProviders.of(this).get(AccountListViewModel::class.java)
        val accountList = accountListViewModel?.getAccountsList()!!.toCollection(ArrayList())

        view.button_account.setOnClickListener {
            val picker = AccountPicker.newInstance(getString(R.string.select_account), accountList)  // dialog title
            picker.setListener { account ->
                viewModel.accountId = account.accountId
                initRecyclerView()
                button_account?.text = account.name
                val currency = ExtendedCurrency.getCurrencyByName(account.currency)
                button_account?.setCompoundDrawablesWithIntrinsicBounds(
                    context?.getDrawable(currency.flag),
                    null, null, null
                )
                picker.dismiss()
            }
            picker.show(activity!!.supportFragmentManager, "ACCOUNT_PICKER")
        }

        // Set the adapter
        if (view is androidx.recyclerview.widget.RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> androidx.recyclerview.widget.LinearLayoutManager(context)
                    else -> androidx.recyclerview.widget.GridLayoutManager(context, columnCount)
                }
                adapter = StatisticsListAdapter()
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }


    private fun initRecyclerView() {
        val adapter = StatisticsListAdapter()
        statlist.adapter = adapter

        viewModel = ViewModelProviders.of(this).get(StatisticsViewModel::class.java)

        if (viewModel.monthYear == null) {
            viewModel.getSumsForCategories()?.observe(this,
                Observer<List<StatisticsCategory>> { categories -> adapter.setCategories(categories) })
        } else {
            val s = SimpleDateFormat("MM/YYYY")
            viewModel.getSumsForMonthAndCategories()?.observe(this,
                Observer<List<StatisticsCategory>> { values ->
                    val filteredByMonthYear = (values.filter {
                        s.format(it.date) == s.format(viewModel.monthYear)
                    })
                    filteredByMonthYear.forEach {
                        if (it.type == TransactionType.Spending) {
                            it.amount *= -1
                        }
                    }
                    val aggByCategory = filteredByMonthYear.groupBy { it.categoryId }
                    val categories: MutableList<StatisticsCategory> = mutableListOf()

                    aggByCategory.forEach { (t, u) ->
                        categories.add(
                            StatisticsCategory(
                                u[0].categoryId,
                                null,
                                u[0].name,
                                null,
                                u.sumByDouble { it.amount })
                        )
                    }
                    adapter.setCategories(categories)
                })
        }
        // Don't forget to tell the RecyclerView how to show the items! (Linear - LinearLayoutManager, Grid - GridLayoutManager etc.)
        statlist.layoutManager = LinearLayoutManager(this.context)
    }
}
