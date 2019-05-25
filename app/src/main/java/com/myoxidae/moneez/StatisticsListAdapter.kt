package com.myoxidae.moneez

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.myoxidae.moneez.fragment.StatisticsListFragment
import com.myoxidae.moneez.model.StatisticsCategory
import kotlinx.android.synthetic.main.fragment_stat_category.view.*
import kotlinx.android.synthetic.main.fragment_transaction.view.*
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder
import net.steamcrafted.materialiconlib.MaterialIconView


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

        val colorFromCategory = item.color
        val icon: MaterialDrawableBuilder.IconValue = MaterialDrawableBuilder.IconValue.valueOf(item.icon)

        val iconColor = Color.parseColor("#$colorFromCategory")
        val categoryBg = holder.mIDCategory.background as GradientDrawable

        categoryBg.setColor(Color.parseColor("#33$colorFromCategory"))
        holder.mIDCategory.setIcon(icon)
        holder.mIDCategory.setColor(iconColor)
    }


    /**
     * Reusable ViewHolder objects.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mIdName: TextView = itemView.item_name
        val mIdSum: TextView = itemView.item_sum
        val mIDCategory: MaterialIconView = itemView.cat_icon


        override fun toString(): String {
            return super.toString() + " '" + mIdName.text + "' " + "lol"
        }
    }
}
