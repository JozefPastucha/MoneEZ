package com.myoxidae.moneez

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.myoxidae.moneez.fragment.StatisticsListFragment
import com.myoxidae.moneez.model.StatisticsCategory
import kotlinx.android.synthetic.main.fragment_stat_category.view.*


class StatisticsListAdapter() : androidx.recyclerview.widget.RecyclerView.Adapter<StatisticsListAdapter.ViewHolder>() {

    private var categories = listOf<StatisticsCategory>()


    /**
     * Creates new ViewHolder instances and inflates them with XML layout.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.fragment_stat_category,
            parent,
            false
        )
    )
    }


    /**
     * Adapter needs to know how many items are there to show.
     */
    override fun getItemCount(): Int {
        return categories.size
    }

    fun setCategories(categories: List<StatisticsCategory>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    /**
     * Gets inflated ViewHolder instance and fills views with data.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = categories[position]
        holder.mIdName.text = item.name
        holder.mIdSum.text = item.amount.toString()

    }


    /**
     * Reusable ViewHolder objects.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mIdName: TextView = itemView.item_name
        val mIdSum: TextView = itemView.item_sum

        override fun toString(): String {
            return super.toString() + " '" + mIdName.text + "' " + "lol"
        }
    }
}
