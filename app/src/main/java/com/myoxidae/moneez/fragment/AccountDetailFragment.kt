package com.myoxidae.moneez

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class AccountDetailFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance() = AccountDetailFragment()
    }

    //private lateinit var viewModel: AccountDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProviders.of(this).get(AccountDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
