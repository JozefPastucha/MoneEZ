package com.myoxidae.moneez.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import com.myoxidae.moneez.*
import com.myoxidae.moneez.fragment.AccountListFragment
import com.myoxidae.moneez.fragment.AccountListFragment.Companion.ADD_ACCOUNT_REQUEST
import com.myoxidae.moneez.fragment.CategoryListFragment
import com.myoxidae.moneez.fragment.CategoryListFragment.Companion.ADD_CATEGORY_REQUEST
import com.myoxidae.moneez.fragment.StatisticsListFragment
import com.myoxidae.moneez.model.Account
import com.myoxidae.moneez.model.AccountType
import com.myoxidae.moneez.model.Category
import com.myoxidae.moneez.viewmodel.AccountListViewModel
import com.myoxidae.moneez.viewmodel.CategoryListViewModel
import kotlinx.android.synthetic.main.app_bar_main.*
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    AccountListFragment.OnListFragmentInteractionListener, CategoryListFragment.OnListFragmentInteractionListener {

    var isPlanWorkerRunning = false

    override fun onStart() {
        super.onStart()
        if(!isPlanWorkerRunning) {
            val planDispatcher = TransactionPlanWorker(application)
            planDispatcher.start()
            isPlanWorkerRunning = true
        }
    }
    var toolbar: Toolbar? = null
    private var accountListViewModel: AccountListViewModel? = null //leteinit?
    private var categoryListViewModel: CategoryListViewModel? = null //leteinit?

    //    Open activity when clicked on item
    override fun onListFragmentInteraction(item: Account?) {
        val intent = Intent(this, AccountDetailActivity::class.java)
        intent.putExtra("accountId", item?.accountId)
        startActivity(intent)
    }

    override fun onListFragmentInteraction(item: Category?) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        accountListViewModel = ViewModelProviders.of(this).get(AccountListViewModel::class.java)
        categoryListViewModel = ViewModelProviders.of(this).get(CategoryListViewModel::class.java)

        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.menu_accounts)

        //        Temporary disable account types

//        Configure speed dial
//        val speedDial: SpeedDialView = findViewById(R.id.speedDial)
//        configureSpeedDial(speedDial)

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
                    AccountListFragment.newInstance(1), "AccountList"
                ).commit()
        }

        fab.setImageDrawable(
            MaterialDrawableBuilder.with(this)
                .setIcon(MaterialDrawableBuilder.IconValue.PLUS)
                .setColor(Color.WHITE)
                .setToActionbarSize()
                .build()
        )

//        Temporary disable account types
        fab.setOnClickListener {
            if (supportActionBar?.title == getString(R.string.menu_categories)) {
                val intent = Intent(this, AddCategoryActivity::class.java)
                startActivityForResult(intent, ADD_CATEGORY_REQUEST)
            } else {
                val intent = Intent(this, AddAccountActivity::class.java)
                intent.putExtra(AddAccountActivity.EXTRA_TYPE, AccountType.Regular)
                startActivityForResult(intent, ADD_ACCOUNT_REQUEST)
            }
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
//        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_accounts -> {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.main_content,
                        AccountListFragment.newInstance(1), "AccountList"
                    ).commit()
                supportActionBar?.title = getString(R.string.menu_accounts)
//                speedDial.visibility = View.VISIBLE
                fab.visibility = View.VISIBLE
            }
            R.id.nav_statistics -> {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.main_content,
                        StatisticsListFragment.newInstance(1), "Statistics"
                    ).commit()
                supportActionBar?.title = getString(R.string.menu_statistics)
                //speedDial.visibility = View.GONE
                fab.visibility = View.GONE
            }
            R.id.nav_categories -> {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.main_content,
                        CategoryListFragment.newInstance(3), "CategoryList"
                    ).commit()
                supportActionBar?.title = getString(R.string.menu_categories)
//                speedDial.visibility = View.GONE
                fab.visibility = View.VISIBLE
            }
        }
        val drawerLayout: androidx.drawerlayout.widget.DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_ACCOUNT_REQUEST) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(this, getString(R.string.account)+ " " + getString(R.string.not_saved), Toast.LENGTH_SHORT).show()
            } else {
                val acc = data!!.getParcelableExtra(AddAccountActivity.EXTRA_ACCOUNT) as Account
                accountListViewModel?.insert(acc)

                Toast.makeText(this, getString(R.string.account)+ " " + getString(R.string.saved), Toast.LENGTH_SHORT).show()
            }
        }

        if (requestCode == ADD_CATEGORY_REQUEST) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(this, getString(R.string.category)+ " " + getString(R.string.not_saved), Toast.LENGTH_SHORT).show()
            } else {
                val cat = data!!.getParcelableExtra(AddCategoryActivity.EXTRA_CATEGORY) as Category
                categoryListViewModel?.insert(cat)

                Toast.makeText(this, getString(R.string.category)+ " " + getString(R.string.saved), Toast.LENGTH_SHORT).show()
            }
        }
    }
}

