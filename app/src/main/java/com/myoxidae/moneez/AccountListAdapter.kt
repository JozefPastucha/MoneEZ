package com.myoxidae.moneez

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.myoxidae.moneez.model.Account
import com.myoxidae.moneez.fragment.AccountListFragment


class AccountListAdapter(private val fragment: AccountListFragment) : RecyclerView.Adapter<AccountListAdapter.ViewHolder>() {
    private var accounts = listOf<Account>()
    /**
     * Creates new ViewHolder instances and inflates them with XML layout.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_account,
                parent,
                false
            ), fragment
        )
    }

    /**
     * Gets inflated ViewHolder instance and fills views with data.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(accounts[position])
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
     * Reusable ViewHolder objects.
     */
    class ViewHolder(itemView: View, fragment: AccountListFragment) : RecyclerView.ViewHolder(itemView) {
        val fragment: AccountListFragment = fragment
        var exp: Button = itemView.findViewById(R.id.add_income)
        var income: Button = itemView.findViewById(R.id.add_spending)

        var name: TextView = itemView.findViewById(R.id.item_name)
        var type: TextView = itemView.findViewById(R.id.type)
        var info: TextView = itemView.findViewById(R.id.info)


        fun bind(account: Account) {
            exp.setOnClickListener {
                //Toast.makeText(itemView.context, "Clicked: ${user.name}", Toast.LENGTH_LONG).show()
                val intent = Intent(itemView.context, com.myoxidae.moneez.activity.AddSpendingActivity::class.java)
                intent.putExtra("id", account.accountId);
                fragment.startActivityForResult(intent, AccountListFragment.ADD_SPENDING_REQUEST)
            }
            income.setOnClickListener {
                //Toast.makeText(itemView.context, "Clicked: ${user.name}", Toast.LENGTH_LONG).show()
                val intent = Intent(itemView.context, com.myoxidae.moneez.activity.AddIncomeActivity::class.java)
                intent.putExtra("id", account.accountId);
                fragment.startActivityForResult(intent, AccountListFragment.ADD_INCOME_REQUEST)
            }
            name.text = account.name
            type.text = account.type.toString()
            info.text = account.info
        }
    }
}
