package com.pv239_project.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.pv239_project.AccountDb
import com.pv239_project.AccountListAdapter
import com.pv239_project.R
import kotlinx.android.synthetic.main.fragment_main.*

class IncomeFragment : Fragment() {

    companion object {

        fun newInstance(): IncomeFragment {
            return IncomeFragment()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
