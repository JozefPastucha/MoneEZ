package com.myoxidae.moneez.picker.categorypicker

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
import androidx.lifecycle.ViewModelProviders
import com.myoxidae.moneez.CategoryListViewModel
import com.myoxidae.moneez.R
import com.myoxidae.moneez.model.Category
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder
import kotlin.collections.ArrayList


class CategoryPicker : DialogFragment() {
    private var searchEditText: EditText? = null
    private var categoryListView: ListView? = null
    private var adapter: CategoryListAdapter? = null
    var listener: CategoryPickerListener? = null
    private lateinit var categoryList: ArrayList<Category>
    private lateinit var selectedCategoryList: ArrayList<Category>
    private var categoryListViewModel: CategoryListViewModel? = null

    companion object {
        fun newInstance(dialogTitle: String): CategoryPicker {
            val picker = CategoryPicker()
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
        searchEditText = view.findViewById(R.id.icon_picker_search) as EditText
        categoryListView = view.findViewById(R.id.icon_picker_list) as ListView

        categoryListViewModel = ViewModelProviders.of(this).get(CategoryListViewModel::class.java)
        categoryList = categoryListViewModel?.getCategoriesList()!!.toCollection(ArrayList())

        selectedCategoryList = ArrayList(categoryList.size)
        selectedCategoryList.addAll(categoryList)

        searchEditText?.setCompoundDrawablesWithIntrinsicBounds(
            MaterialDrawableBuilder.with(this.context)
                .setIcon(MaterialDrawableBuilder.IconValue.MAGNIFY)
                .build(),
            null, null, null
        )

        adapter = CategoryListAdapter(context!!, selectedCategoryList)
        categoryListView?.setAdapter(adapter)


        categoryListView?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            if (listener != null) {
                val item = selectedCategoryList.get(position)
                listener?.onSelectCategory(item)
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
        selectedCategoryList.clear()
        for (category in categoryList) {
            if (category.toString().toLowerCase().contains(text.toLowerCase())) {
                selectedCategoryList.add(category)
            }
        }
        adapter?.notifyDataSetChanged()
    }

    inline fun setListener(crossinline listener: (Category) -> Unit) {
        this.listener = object : CategoryPickerListener {
            override fun onSelectCategory(category: Category) = listener(category)
        }
    }

}