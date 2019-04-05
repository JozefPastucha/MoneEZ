package com.pv239_project.fragment

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.pv239_project.R
import kotlinx.android.synthetic.main.fragment_exp.*
import kotlinx.android.synthetic.main.fragment_main.*

class ExpFragment : Fragment() {

    companion object {

        fun newInstance(): ExpFragment {
            return ExpFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_exp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ArrayAdapter(this.context, android.R.layout.simple_spinner_item, listOf("USD", "EUR", "CZK"))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        positionSpinner.adapter = adapter

        val adapter2 = ArrayAdapter(this.context, android.R.layout.simple_spinner_item, listOf("Home", "Food", "Booze"))
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.adapter = adapter2
        button.setOnClickListener{
            //save
            val intent = Intent(this.context, com.pv239_project.activity.MainActivity::class.java)
            startActivity(intent)
        }
    }
}
