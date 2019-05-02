package com.pv239_project

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.pv239_project.model.Account
import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.core.app.ActivityCompat.startActivityForResult
import com.pv239_project.fragment.MainFragment


class AccountListAdapter(private val fragment: MainFragment) : RecyclerView.Adapter<AccountListAdapter.ViewHolder>() {
    private var accounts = listOf<Account>()
    /**
     * Creates new ViewHolder instances and inflates them with XML layout.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.account_list_item,
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
    class ViewHolder(itemView: View, fragment: MainFragment) : RecyclerView.ViewHolder(itemView) {
        val fragment: MainFragment = fragment
        var exp: Button = itemView.findViewById(R.id.exp)
        var income: Button = itemView.findViewById(R.id.income)

        var name: TextView = itemView.findViewById(R.id.name)
        var type: TextView = itemView.findViewById(R.id.type)
        var info: TextView = itemView.findViewById(R.id.info)


        fun bind(account: Account) {
            exp.setOnClickListener {
                //Toast.makeText(itemView.context, "Clicked: ${user.name}", Toast.LENGTH_LONG).show()
                val intent = Intent(itemView.context, com.pv239_project.activity.AddSpendingActivity::class.java)
                intent.putExtra("id", account.accountId);
                fragment.startActivityForResult(intent, MainFragment.ADD_SPENDING_REQUEST)
            }
            income.setOnClickListener {
                //Toast.makeText(itemView.context, "Clicked: ${user.name}", Toast.LENGTH_LONG).show()
                val intent = Intent(itemView.context, com.pv239_project.activity.AddIncomeActivity::class.java)
                intent.putExtra("id", account.accountId);
                fragment.startActivityForResult(intent, MainFragment.ADD_INCOME_REQUEST)
            }
            name.text = account.name
            type.text = account.type.toString()
            info.text = account.info
        }
    }
}
