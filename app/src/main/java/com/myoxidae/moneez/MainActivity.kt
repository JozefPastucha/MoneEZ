package com.myoxidae.moneez

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.Toast
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView
import com.myoxidae.moneez.dummy.DummyContent
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    AccountListFragment.OnListFragmentInteractionListener {

    //    Open activity when clicked on item
    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        val intent = Intent(this, AccountDetailActivity::class.java)
        intent.putExtra("item", item.toString())
        startActivity(intent)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

//        Configure speed dial
        val speedDial: SpeedDialView = findViewById(R.id.speedDial)
        configureSpeedDial(speedDial)

        val drawerLayout: androidx.drawerlayout.widget.DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        // Add list fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.main_content, AccountListFragment.newInstance(1), "AccountList").commit()
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
                    .replace(R.id.main_content, AccountListFragment.newInstance(1), "AccountList").commit()
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
                    Toast.makeText(this, "Regular account form!", Toast.LENGTH_SHORT).show()
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
}
