package com.myoxidae.moneez.picker.accountpicker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.mynameismidori.currencypicker.ExtendedCurrency
import com.myoxidae.moneez.model.Account
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class AccountListAdapter(private val mContext: Context, internal var accounts: ArrayList<Account>) :
    BaseAdapter() {
    internal var inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(mContext)
    }


    override fun getCount(): Int {
        return accounts.size
    }

    override fun getItem(arg0: Int): Any? {
        return null
    }

    override fun getItemId(arg0: Int): Long {
        return 0
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View? {
        var view = view
        val account = accounts[position]

        if (view == null)
            view = inflater.inflate(com.myoxidae.moneez.R.layout.row, null)

        val cell = Cell.from(view)
        cell!!.textView?.text = account.name

        val currency = ExtendedCurrency.getCurrencyByName(account.currency)
        cell.imageView?.setImageDrawable(ContextCompat.getDrawable(mContext, currency.flag))
        return view
    }

    internal class Cell {
        var textView: TextView? = null
        var imageView: ImageView? = null

        companion object {

            fun from(view: View?): Cell? {
                if (view == null)
                    return null

                if (view.tag == null) {
                    val cell = Cell()
                    cell.textView = view.findViewById<View>(com.myoxidae.moneez.R.id.row_title) as TextView
                    cell.imageView = view.findViewById<View>(com.myoxidae.moneez.R.id.row_icon) as ImageView
                    view.tag = cell
                    return cell
                } else {
                    return view.tag as Cell
                }
            }
        }
    }
}