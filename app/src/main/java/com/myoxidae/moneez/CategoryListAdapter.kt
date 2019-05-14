package com.myoxidae.moneez

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.myoxidae.moneez.fragment.CategoryListFragment
import com.myoxidae.moneez.model.Category
import com.myoxidae.moneez.fragment.CategoryListFragment.OnListFragmentInteractionListener
import kotlinx.android.synthetic.main.fragment_account.view.*


class CategoryListAdapter(
    private val mListener: OnListFragmentInteractionListener?,
    private val fragment: CategoryListFragment
) : androidx.recyclerview.widget.RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {

    private var categories = listOf<Category>()
    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Category
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
        return categories.size
    }

    fun setCategories(categories: List<Category>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    /**
     * Gets inflated ViewHolder instance and fills views with data.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = categories[position]
        holder.nameView.text = item.name

        with(holder.itemView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }


    /**
     * Reusable ViewHolder objects.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView: TextView = itemView.item_name

        override fun toString(): String {
            return super.toString() + " '" + nameView.text + "'"
        }
    }
}
