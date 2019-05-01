package com.pv239_project.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.pv239_project.AccountListAdapter
import com.pv239_project.R
import kotlinx.android.synthetic.main.fragment_main.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.pv239_project.AccountListViewModel
import com.pv239_project.activity.AddAccount
import com.pv239_project.model.Account
import com.pv239_project.model.AccountType




class MainFragment : Fragment() {
    val ADD_ACCOUNT_REQUEST = 1

    private var accountListViewModel: AccountListViewModel? = null
    companion object {

        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        button2.setOnClickListener {
            val intent = Intent(this.context, AddAccount::class.java)
            //start from fragment not activity
            startActivityForResult(intent, 1)
        }
    }


    private fun initRecyclerView() {
        val adapter = AccountListAdapter()
        list_recycler.adapter = adapter

        accountListViewModel = ViewModelProviders.of(this).get(AccountListViewModel::class.java!!)
        accountListViewModel?.getAllAccounts()?.observe(this, //dalsie drbnute otazniky
            Observer<List<Account>> { accounts -> adapter.setAccounts(accounts) })
        // Don't forget to tell the RecyclerView how to show the items! (Linear - LinearLayoutManager, Grid - GridLayoutManager etc.)
        list_recycler.layoutManager = LinearLayoutManager(this.context)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_ACCOUNT_REQUEST && resultCode == RESULT_OK) {
            val name = data!!.getStringExtra(AddAccount.EXTRA_NAME)

            val acc = Account(AccountType.Cash, name, "info", 0.0, 0.0, 0.0, "lol")
            accountListViewModel?.insert(acc)

            Toast.makeText(activity, "Note saved", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity, "Note not saved", Toast.LENGTH_SHORT).show()
        }
    }
}
