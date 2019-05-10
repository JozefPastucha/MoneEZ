package com.myoxidae.moneez

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.myoxidae.moneez.fragment.AccountListFragment
import com.myoxidae.moneez.model.Account
import com.myoxidae.moneez.fragment.TransactionListFragment.OnListFragmentInteractionListener
import com.myoxidae.moneez.fragment.TransactionListFragment
import com.myoxidae.moneez.model.Transaction
import kotlinx.android.synthetic.main.fragment_account.view.*
import kotlinx.android.synthetic.main.fragment_transaction.view.*


class TransactionListAdapter(
    private val mListener: OnListFragmentInteractionListener?,
    private val fragment: TransactionListFragment
) : androidx.recyclerview.widget.RecyclerView.Adapter<TransactionListAdapter.ViewHolder>() {

    private var transactions = listOf<Transaction>()
    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Transaction
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }
    /**
     * Creates new ViewHolder instances and inflates them with XML layout.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_transaction,
                parent,
                false
            )
        )
    }


    /**
     * Adapter needs to know how many items are there to show.
     */
    override fun getItemCount(): Int {
        return transactions.size
    }

    fun setTransactions(transactions: List<Transaction>) {
        this.transactions = transactions
        notifyDataSetChanged()
    }

    /**
     * Gets inflated ViewHolder instance and fills views with data.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = transactions[position]
        holder.mIdView.text = item.amount.toString()

        with(holder.itemView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }


    /**
     * Reusable ViewHolder objects.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mIdView: TextView = itemView.item_amount
    }
}