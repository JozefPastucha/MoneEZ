package com.myoxidae.moneez.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.myoxidae.moneez.adapter.CategoryListAdapter
import com.myoxidae.moneez.viewmodel.CategoryListViewModel
import com.myoxidae.moneez.R
import com.myoxidae.moneez.model.Category
import kotlinx.android.synthetic.main.fragment_category_list.*


class CategoryListFragment : androidx.fragment.app.Fragment() {

    private var listener: OnListFragmentInteractionListener? = null
    // TODO: Customize parameters
    // Pocet sloupcu by mel byt urcovany dynamicky podle velikosti zarizeni
    private var columnCount = 1

    private var categoryListViewModel: CategoryListViewModel? = null

    companion object {
        @JvmField
        var ADD_CATEGORY_REQUEST = 10

        // TODO: Customize parameter argument names
        @JvmField
        var ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            CategoryListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: Category?)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category_list, container, false)

        // Set the adapter
        if (view is androidx.recyclerview.widget.RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> androidx.recyclerview.widget.LinearLayoutManager(context)
                    else -> androidx.recyclerview.widget.GridLayoutManager(context, columnCount)
                }
                adapter = CategoryListAdapter(listener, this@CategoryListFragment)
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }


    private fun initRecyclerView() {
        val adapter = CategoryListAdapter(listener, this)
        list.adapter = adapter

        categoryListViewModel = ViewModelProviders.of(this).get(CategoryListViewModel::class.java)
        categoryListViewModel?.getAllCategories()?.observe(this,
            Observer<List<Category>> { categories -> adapter.setCategories(categories) })
    }
}
