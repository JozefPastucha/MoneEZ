package com.myoxidae.moneez.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView
import com.myoxidae.moneez.AccountDetailActivity
import com.myoxidae.moneez.AccountListViewModel
import com.myoxidae.moneez.fragment.AccountListFragment
import com.myoxidae.moneez.R
import com.myoxidae.moneez.fragment.AccountListFragment.Companion.ADD_ACCOUNT_REQUEST
import com.myoxidae.moneez.model.Account
import com.myoxidae.moneez.model.AccountType
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    AccountListFragment.OnListFragmentInteractionListener {

    private var accountListViewModel: AccountListViewModel? = null

    //    Open activity when clicked on item
    override fun onListFragmentInteraction(item: Account?) {
        val intent = Intent(this, AccountDetailActivity::class.java)
        intent.putExtra("item", item.toString())
        startActivity(intent)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        accountListViewModel = ViewModelProviders.of(this).get(AccountListViewModel::class.java)

        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

//        Configure speed dial
        val speedDial: SpeedDialView = findViewById(R.id.speedDial)
        configureSpeedDial(speedDial)

        val drawerLayout: androidx.drawerlayout.widget.DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        // Add list fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.main_content,
                    AccountListFragment.newInstance(1), "AccountList").commit()
        }

        navView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        val drawerLayout: androidx.drawerlayout.widget.DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_accounts -> {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.main_content,
                        AccountListFragment.newInstance(1), "AccountList").commit()
            }
            R.id.nav_statistics -> {

            }
            R.id.nav_categories -> {

            }
            R.id.nav_settings -> {

            }
        }
        val drawerLayout: androidx.drawerlayout.widget.DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
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
                R.id.regular_account,
                MaterialDrawableBuilder.with(this)
                    .setIcon(MaterialDrawableBuilder.IconValue.BANK)
                    .setColor(Color.WHITE)
                    .setToActionbarSize()
                    .build()
            ).setLabel(R.string.regular_account).create()
        )
        speedDial.addActionItem(
            SpeedDialActionItem.Builder(
                R.id.cash_account,
                MaterialDrawableBuilder.with(this)
                    .setIcon(MaterialDrawableBuilder.IconValue.CASH)
                    .setColor(Color.WHITE)
                    .setToActionbarSize()
                    .build()
            ).setLabel(R.string.cash_account).create()
        )
        speedDial.addActionItem(
            SpeedDialActionItem.Builder(
                R.id.savings_account,
                MaterialDrawableBuilder.with(this)
                    .setIcon(MaterialDrawableBuilder.IconValue.CURRENCY_USD)
                    .setColor(Color.WHITE)
                    .setToActionbarSize()
                    .build()
            ).setLabel(R.string.savings_account).create()
        )

//Do actions when speed dial items are clicked
        speedDial.setOnActionSelectedListener(SpeedDialView.OnActionSelectedListener { speedDialActionItem ->
            when (speedDialActionItem.id) {
                R.id.regular_account -> {
                    val intent = Intent(this, AddAccountActivity::class.java)
                    //start from fragment not activity
                    startActivityForResult(intent, ADD_ACCOUNT_REQUEST)
                    false // true to keep the Speed Dial open
                }
                R.id.cash_account -> {
                    Toast.makeText(this, "Cash account form!", Toast.LENGTH_SHORT).show()
                    false
                }
                R.id.savings_account -> {
                    Toast.makeText(this, "Savings account form!", Toast.LENGTH_SHORT).show()
                    false
                }
                else -> false
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_ACCOUNT_REQUEST) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(this, "Account not saved", Toast.LENGTH_SHORT).show()
            } else {
                val name = data!!.getStringExtra(AddAccountActivity.EXTRA_NAME)
                val acc = Account(AccountType.Cash, name, "info", 0.0, 0.0, 0.0, "lol")
                accountListViewModel?.insert(acc)

                Toast.makeText(this, "Account saved", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
