package com.myoxidae.moneez

import android.content.Intent
import android.graphics.Color
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
import com.myoxidae.moneez.model.TransactionType
import kotlinx.android.synthetic.main.fragment_account.view.*
import kotlinx.android.synthetic.main.fragment_transaction.view.*
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder
import net.steamcrafted.materialiconlib.MaterialIconView
import java.text.SimpleDateFormat
import android.R.color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat


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
//        TODO get currency from account
//        TODO to int only if .0
        if (item.type == TransactionType.Income) {
            holder.mIdAmount.setTextColor(Color.parseColor("#25b210"))
            holder.mIdAmount.text = "+" + item.amount.toInt().toString()
        } else if (item.type == TransactionType.Spending) {
            holder.mIdAmount.setTextColor(Color.parseColor("#d12222"))
            holder.mIdAmount.text = "-" + item.amount.toInt().toString()
        }
        holder.mIdDate.text = SimpleDateFormat("dd.MM.yyyy").format(item.date)
        holder.mIdName.text = item.name

//        TODO icon, color from circle_background
        val colorFromCategory = "d12222"
        val icon: MaterialDrawableBuilder.IconValue = MaterialDrawableBuilder.IconValue.CASH

        val iconColor = Color.parseColor("#$colorFromCategory")
        val categoryBg = holder.mIDCategory.background as GradientDrawable

        categoryBg.setColor(Color.parseColor("#33$colorFromCategory"))
        holder.mIDCategory.setIcon(icon)
        holder.mIDCategory.setColor(iconColor)

        with(holder.itemView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }


    /**
     * Reusable ViewHolder objects.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mIdAmount: TextView = itemView.item_amount
        val mIdDate: TextView = itemView.item_date
        val mIdName: TextView = itemView.item_title
        val mIDCategory: MaterialIconView = itemView.category_icon
    }
}
