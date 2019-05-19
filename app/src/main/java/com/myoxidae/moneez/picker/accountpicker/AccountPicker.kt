package com.myoxidae.moneez.picker.accountpicker

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ListView
import android.widget.AdapterView
import android.widget.EditText
import android.text.Editable
import android.text.TextWatcher
import com.myoxidae.moneez.R
import com.myoxidae.moneez.model.Account
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder



class AccountPicker : DialogFragment() {
    private var searchEditText: EditText? = null
    private var accountListView: ListView? = null
    private var adapter: AccountListAdapter? = null
    var listener: AccountPickerListener? = null
    private lateinit var accountList: ArrayList<Account>
    private lateinit var selectedAccountList: ArrayList<Account>

    companion object {
        fun newInstance(dialogTitle: String, accountList: ArrayList<Account>): AccountPicker {
            val picker = AccountPicker()
            val bundle = Bundle()
            bundle.putString("dialogTitle", dialogTitle)
            bundle.putParcelableArrayList("list", accountList)
            picker.arguments = bundle
            return picker
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_dialog, container, false)

        val args = arguments

        if (args != null && dialog != null) {
            val dialogTitle = args.getString("dialogTitle")
            dialog!!.setTitle(dialogTitle)
            dialog!!.window!!.setLayout(100, 100)
            accountList = args.getParcelableArrayList("list")
        }
        searchEditText = view.findViewById(R.id.icon_picker_search) as EditText
        accountListView = view.findViewById(R.id.icon_picker_list) as ListView


        selectedAccountList = ArrayList(accountList.size)
        selectedAccountList.addAll(accountList)

        searchEditText?.setCompoundDrawablesWithIntrinsicBounds(
            MaterialDrawableBuilder.with(this.context)
                .setIcon(MaterialDrawableBuilder.IconValue.MAGNIFY)
                .build(),
            null, null, null
        )

        adapter = AccountListAdapter(context!!, selectedAccountList)
        accountListView?.setAdapter(adapter)


        accountListView?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            if (listener != null) {
                val item = selectedAccountList.get(position)
                listener?.onSelectAccount(item)
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
        selectedAccountList.clear()
        for (account in accountList) {
            if (account.toString().toLowerCase().contains(text.toLowerCase())) {
                selectedAccountList.add(account)
            }
        }
        adapter?.notifyDataSetChanged()
    }

    inline fun setListener(crossinline listener: (Account) -> Unit) {
        this.listener = object : AccountPickerListener {
            override fun onSelectAccount(account: Account) = listener(account)
        }
    }

}