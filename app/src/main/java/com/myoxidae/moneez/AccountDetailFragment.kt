package com.myoxidae.moneez

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class AccountDetailFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance() = AccountDetailFragment()
    }

    private lateinit var viewModel: AccountDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.account_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AccountDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
