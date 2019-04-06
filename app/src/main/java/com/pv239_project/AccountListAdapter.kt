package com.pv239_project

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.pv239_project.model.Account

class AccountListAdapter(private val users: List<Account>) : RecyclerView.Adapter<AccountListAdapter.ViewHolder>() {
    /**
     * Creates new ViewHolder instances and inflates them with XML layout.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.account_list_item,
                parent,
                false
            )
        )
    }

    /**
     * Gets inflated ViewHolder instance and fills views with data.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
    }

    /**
     * Adapter needs to know how many items are there to show.
     */
    override fun getItemCount(): Int {
        return users.size
    }


    /**
     * Reusable ViewHolder objects.
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var exp: Button = itemView.findViewById(R.id.exp)
        var income: Button = itemView.findViewById(R.id.income)

        var name: TextView = itemView.findViewById(R.id.name)
        var type: TextView = itemView.findViewById(R.id.type)
        var info: TextView = itemView.findViewById(R.id.info)


        fun bind(account: Account) {
            exp.setOnClickListener {
                //Toast.makeText(itemView.context, "Clicked: ${user.name}", Toast.LENGTH_LONG).show()
                val intent = Intent(itemView.context, com.pv239_project.activity.ExpActivity::class.java)
                itemView.context.startActivity(intent)
            }
            income.setOnClickListener {
                //Toast.makeText(itemView.context, "Clicked: ${user.name}", Toast.LENGTH_LONG).show()
                val intent = Intent(itemView.context, com.pv239_project.activity.IncomeActivity::class.java)
                itemView.context.startActivity(intent)
            }
            name.text = account.name
            type.text = account.type.toString()
            info.text = account.info
        }
    }
}
