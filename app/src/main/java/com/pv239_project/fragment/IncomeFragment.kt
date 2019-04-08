package com.pv239_project.fragment

import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pv239_project.R
import com.pv239_project.database.Database
import com.pv239_project.model.Account
import com.pv239_project.model.AccountType
import kotlinx.android.synthetic.main.account_list_item.*

class IncomeFragment : Fragment() {

    companion object {

        fun newInstance(): IncomeFragment {
            return IncomeFragment()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }
}
