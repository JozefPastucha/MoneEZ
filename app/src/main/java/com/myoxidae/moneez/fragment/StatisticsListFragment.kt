package com.myoxidae.moneez.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mynameismidori.currencypicker.ExtendedCurrency
import com.myoxidae.moneez.*
import com.myoxidae.moneez.model.*
import com.myoxidae.moneez.picker.accountpicker.AccountPicker
import kotlinx.android.synthetic.main.fragment_stat_category_list.*
import kotlinx.android.synthetic.main.fragment_stat_category_list.view.*
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
        fun newInstance(columnCount: Int) =
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

        viewModel = ViewModelProviders.of(this).get(StatisticsViewModel::class.java)

        if(accountList.size > 0) {
            setAccount(accountList.first(), view)
        }

        view.button_account.setOnClickListener {
            val picker = AccountPicker.newInstance(getString(R.string.select_account), accountList)  // dialog title
            picker.setListener { account ->
                setAccount(account, view)
                initRecyclerView()
                picker.dismiss()
            }
            picker.show(activity!!.supportFragmentManager, "ACCOUNT_PICKER")
        }

        view.button_next_month.setOnClickListener {
            if(viewModel.monthYear != null) {
                val cal = Calendar.getInstance()
                val s = SimpleDateFormat("MM/YYYY")

                if(s.format(viewModel.monthYear) == s.format(cal.time)) {
                    viewModel.monthYear = null
                    monthAndYear.text = "All months"
                }
                else {
                    cal.time = viewModel.monthYear
                    cal.add(Calendar.MONTH, 1)
                    viewModel.monthYear = cal.time
                    monthAndYear.text = s.format(viewModel.monthYear)
                }
                initRecyclerView()
            }
        }

        view.button_prev_month.setOnClickListener {
            val cal = Calendar.getInstance()
            val s = SimpleDateFormat("MM/YYYY")
            if(viewModel.monthYear != null) {
                cal.time = viewModel.monthYear
                cal.add(Calendar.MONTH, -1)
                viewModel.monthYear = cal.time
            }
            else {
                viewModel.monthYear = cal.time
            }
            initRecyclerView()
            monthAndYear.text = s.format(viewModel.monthYear)
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

        if (viewModel.monthYear == null) {
            viewModel.transactionsWithCategoryName()?.observe(this,
                Observer<List<TransactionWithCategoryData>> { values ->
                    adapter.setCategories(sumByCategoryAndSort(values))
                })
        }
        else {
            val s = SimpleDateFormat("MM/YYYY")

                viewModel.transactionsWithCategoryName()?.observe(this,
                    Observer<List<TransactionWithCategoryData>> { values ->
                        val filteredByMonthYear = (values.filter {
                            s.format(it.date) == s.format(viewModel.monthYear)
                        })
                        adapter.setCategories(sumByCategoryAndSort(filteredByMonthYear))
                    })
            }
        // Don't forget to tell the RecyclerView how to show the items! (Linear - LinearLayoutManager, Grid - GridLayoutManager etc.)
        statlist.layoutManager = LinearLayoutManager(this.context)
    }

    private fun sumByCategoryAndSort(values: List<TransactionWithCategoryData>): MutableList<StatisticsCategory> {
        values.forEach {
            if (it.type == TransactionType.Spending) {
                it.amount *= -1
            }
        }
        val aggByCategory = values.groupBy { it.categoryId }
        val categories: MutableList<StatisticsCategory> = mutableListOf()

        aggByCategory.forEach { (t, u) ->
            categories.add(
                StatisticsCategory(
                    u[0].categoryId,
                    u[0].cName,
                    u[0].cIcon,
                    u[0].cColor,
                    u.sumByDouble { it.amount })
            )
        }
        categories.sortedBy { it.name }
        return categories
    }

    private fun setAccount(account: Account, view: View) {
        viewModel.accountId = account.accountId
        view.button_account?.text = account.name
        val currency = ExtendedCurrency.getCurrencyByName(account.currency)
        view.button_account?.setCompoundDrawablesWithIntrinsicBounds(
            context?.getDrawable(currency.flag),
            null, null, null
        )
    }
}
