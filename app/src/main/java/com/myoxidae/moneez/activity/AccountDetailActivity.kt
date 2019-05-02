package com.myoxidae.moneez

import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView

import kotlinx.android.synthetic.main.activity_account_detail.*
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder

class AccountDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_detail)
        setSupportActionBar(toolbar)

//        Configure speed dial
        val speedDial: SpeedDialView = findViewById(R.id.speedDial)
        configureSpeedDial(speedDial)

//        Set text

        val fn = intent.getStringExtra("item")

        val fntext = findViewById<TextView>(R.id.whatever)
        fntext.text = fn
    }

    private fun configureSpeedDial(speedDial: SpeedDialView) {
        speedDial.setMainFabClosedDrawable(
            MaterialDrawableBuilder.with(this)
                .setIcon(MaterialDrawableBuilder.IconValue.PLUS)
                .setColor(Color.WHITE)
                .setToActionbarSize()
                .build()
        )

        speedDial.addActionItem(
            SpeedDialActionItem.Builder(
                R.id.income,
                MaterialDrawableBuilder.with(this)
                    .setIcon(MaterialDrawableBuilder.IconValue.PLUS)
                    .setColor(Color.WHITE)
                    .setToActionbarSize()
                    .build()
            ).setLabel(R.string.income).create()
        )
        speedDial.addActionItem(
            SpeedDialActionItem.Builder(
                R.id.expenditure,
                MaterialDrawableBuilder.with(this)
                    .setIcon(MaterialDrawableBuilder.IconValue.MINUS)
                    .setColor(Color.WHITE)
                    .setToActionbarSize()
                    .build()
            ).setLabel(R.string.expenditure).create()
        )
        speedDial.addActionItem(
            SpeedDialActionItem.Builder(
                R.id.bank_transfer,
                MaterialDrawableBuilder.with(this)
                    .setIcon(MaterialDrawableBuilder.IconValue.BANK)
                    .setColor(Color.WHITE)
                    .setToActionbarSize()
                    .build()
            ).setLabel(R.string.bank_transfer).create()
        )
        speedDial.addActionItem(
            SpeedDialActionItem.Builder(
                R.id.cash_out,
                MaterialDrawableBuilder.with(this)
                    .setIcon(MaterialDrawableBuilder.IconValue.CASH)
                    .setColor(Color.WHITE)
                    .setToActionbarSize()
                    .build()
            ).setLabel(R.string.cash_out).create()
        )

//Do actions when speed dial items are clicked
        speedDial.setOnActionSelectedListener(SpeedDialView.OnActionSelectedListener { speedDialActionItem ->
            when (speedDialActionItem.id) {
                R.id.income -> {
                    Toast.makeText(this, "Income form!", Toast.LENGTH_SHORT).show()
                    false // true to keep the Speed Dial open
                }
                R.id.expenditure -> {
                    Toast.makeText(this, "expenditure form!", Toast.LENGTH_SHORT).show()
                    false
                }
                R.id.bank_transfer -> {
                    Toast.makeText(this, "Bank transfer form!", Toast.LENGTH_SHORT).show()
                    false
                }
                R.id.cash_out -> {
                    Toast.makeText(this, "Cash out form!", Toast.LENGTH_SHORT).show()
                    false
                }
                else -> false
            }
        })
    }
}
