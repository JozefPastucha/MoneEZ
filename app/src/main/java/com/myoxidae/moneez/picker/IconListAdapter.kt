package com.myoxidae.moneez.picker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder

class IconListAdapter(private val mContext: Context, internal var icons: ArrayList<MaterialDrawableBuilder.IconValue>) :
    BaseAdapter() {
    internal var inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(mContext)
    }


    override fun getCount(): Int {
        return icons.size
    }

    override fun getItem(arg0: Int): Any? {
        return null
    }

    override fun getItemId(arg0: Int): Long {
        return 0
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View? {
        var view = view
        val icon = icons[position]

        if (view == null)
            view = inflater.inflate(com.myoxidae.moneez.R.layout.row, null)

        val cell = Cell.from(view)
        cell!!.textView?.text = icon.toString().replace("_", " ")

        cell.imageView?.setImageDrawable(MaterialDrawableBuilder.with(this.mContext)
            .setIcon(icon)
            .setToActionbarSize()
            .build())
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