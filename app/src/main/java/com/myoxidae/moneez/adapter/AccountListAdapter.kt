package com.myoxidae.moneez.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.mynameismidori.currencypicker.ExtendedCurrency
import com.myoxidae.moneez.R
import com.myoxidae.moneez.activity.AddTransactionActivity
import com.myoxidae.moneez.fragment.AccountListFragment
import com.myoxidae.moneez.model.Account
import com.myoxidae.moneez.fragment.AccountListFragment.OnListFragmentInteractionListener
import com.myoxidae.moneez.model.TransactionType
import kotlinx.android.synthetic.main.fragment_account.view.*


class AccountListAdapter(
    private val mListener: OnListFragmentInteractionListener?,
    private val fragment: AccountListFragment
) : androidx.recyclerview.widget.RecyclerView.Adapter<AccountListAdapter.ViewHolder>() {

    private var accounts = listOf<Account>()
    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Account
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    /**
     * Creates new ViewHolder instances and inflates them with XML layout.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_account,
                parent,
                false
            )
        )
        return viewHolder
    }


    /**
     * Adapter needs to know how many items are there to show.
     */
    override fun getItemCount(): Int {
        return accounts.size
    }

    fun setAccounts(accounts: List<Account>) {
        this.accounts = accounts
        notifyDataSetChanged()
    }

    /**
     * Gets inflated ViewHolder instance and fills views with data.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = accounts[position]
        holder.typeView.text = item.type.toString() + " account"
        holder.nameView.text = item.name
        val currency = ExtendedCurrency.getCurrencyByName(item.currency).symbol
        holder.balanceView.text = item.currentBalance.toInt().toString() + currency


        holder.exp.setOnClickListener {
            val intent = Intent(holder.itemView.context, com.myoxidae.moneez.activity.AddTransactionActivity::class.java)
            intent.putExtra(AddTransactionActivity.EXTRA_ACCOUNT_ID, accounts[position].accountId)
            intent.putExtra(AddTransactionActivity.EXTRA_TYPE, TransactionType.Spending)
            fragment.startActivityForResult(intent, AccountListFragment.ADD_TRANSACTION_REQUEST)
        }
        holder.income.setOnClickListener {
            val intent = Intent(holder.itemView.context, com.myoxidae.moneez.activity.AddTransactionActivity::class.java)
            intent.putExtra(AddTransactionActivity.EXTRA_ACCOUNT_ID, accounts[position].accountId)
            intent.putExtra(AddTransactionActivity.EXTRA_TYPE, TransactionType.Income)
            fragment.startActivityForResult(intent, AccountListFragment.ADD_TRANSACTION_REQUEST)
        }

        with(holder.itemView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }


    /**
     * Reusable ViewHolder objects.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var exp: Button = itemView.findViewById(R.id.add_spending)
        var income: Button = itemView.findViewById(R.id.add_income)

        val nameView: TextView = itemView.item_name
        val typeView: TextView = itemView.item_type
        val balanceView: TextView = itemView.item_balance

        override fun toString(): String {
            return super.toString() + " '" + nameView.text + "'"
        }
    }
}
