package com.myoxidae.moneez.picker

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ListView
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder
import android.widget.AdapterView
import android.widget.EditText
import android.text.Editable
import android.text.TextWatcher
import kotlin.collections.ArrayList


class IconPicker : DialogFragment() {
    private var searchEditText: EditText? = null
    private var iconListView: ListView? = null
    private var adapter: IconListAdapter? = null
    var listener: IconPickerListener? = null
    private var iconList = MaterialDrawableBuilder.IconValue.values().toCollection(ArrayList())
    private var selectedIconList = MaterialDrawableBuilder.IconValue.values().toCollection(ArrayList())

    companion object {
        fun newInstance(dialogTitle: String): IconPicker {
            val picker = IconPicker()
            val bundle = Bundle()
            bundle.putString("dialogTitle", dialogTitle)
            picker.arguments = bundle
            return picker
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.myoxidae.moneez.R.layout.layout_dialog, container, false)

        val args = arguments

        if (args != null && dialog != null) {
            val dialogTitle = args.getString("dialogTitle")
            dialog!!.setTitle(dialogTitle)

            dialog!!.window!!.setLayout(100,100)
        }
        searchEditText = view.findViewById(com.myoxidae.moneez.R.id.icon_picker_search) as EditText
        iconListView = view.findViewById(com.myoxidae.moneez.R.id.icon_picker_list) as ListView

        searchEditText?.setCompoundDrawablesWithIntrinsicBounds(
            MaterialDrawableBuilder.with(this.context)
                .setIcon(MaterialDrawableBuilder.IconValue.MAGNIFY)
                .build(),
            null, null, null
        )

        adapter = IconListAdapter(context!!, selectedIconList)
        iconListView?.setAdapter(adapter)


        iconListView?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            if (listener != null) {
                val item = selectedIconList.get(position)
                listener?.onSelectIcon(item)
            }
        }


        searchEditText?.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable) {
                search(s.toString())
            }
        })


        return view
    }

    override fun dismiss() {
        if (dialog != null) {
            super.dismiss()
        } else {
            fragmentManager!!.popBackStack()
        }
    }

    private fun search(text: String) {
        selectedIconList.clear()
        for (icon in iconList) {
            if (icon.toString().toLowerCase().contains(text.toLowerCase())) {
                selectedIconList.add(icon)
            }
        }
        adapter?.notifyDataSetChanged()
    }

    inline fun setListener(crossinline listener: (MaterialDrawableBuilder.IconValue) -> Unit) {
        this.listener = object : IconPickerListener {
            override fun onSelectIcon(icon: MaterialDrawableBuilder.IconValue) = listener(icon)
        }
    }

}