package com.netchess.pkiykov.ui

import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.netchess.pkiykov.R
import com.netchess.pkiykov.core.MainApplication
import com.netchess.pkiykov.ui.MenuItems.CURRENT_GAME
import com.netchess.pkiykov.ui.MenuItems.EXIT
import com.netchess.pkiykov.ui.MenuItems.NEW_GAME
import com.netchess.pkiykov.ui.MenuItems.PROFILE
import com.netchess.pkiykov.ui.MenuItems.RANK_LIST
import com.netchess.pkiykov.ui.screens.base.BaseView
import com.netchess.pkiykov.ui.screens.onBoarding.OnBoardingView
import com.netchess.pkiykov.ui.screens.profile.view.ProfileView
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var fragment: BaseView
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MainApplication.graph.inject(this)
        initViews()
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
            setHomeAsUpIndicator(R.drawable.ic_menu_arrow_back)
        }
        drawerLayout = findViewById(R.id.drawerLayout)
        drawerLayout.addDrawerListener(
                object : DrawerLayout.DrawerListener {
                    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                        // Respond when the drawer's position changes
                    }

                    override fun onDrawerOpened(drawerView: View) {
                        // Respond when the drawer is opened
                    }

                    override fun onDrawerClosed(drawerView: View) {
                        // Respond when the drawer is closed
                    }

                    override fun onDrawerStateChanged(newState: Int) {
                        // Respond when the drawer motion state changes
                    }
                }
        )
        drawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                invalidateOptionsMenu()
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                invalidateOptionsMenu()
            }
        }

        drawerLayout.addDrawerListener(drawerToggle)

        val navigationView: NavigationView = findViewById(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // set item as selected to persist highlight
            menuItem.isChecked = true
            // close drawer when item is tapped
            drawerLayout.closeDrawers()

            val id = menuItem.itemId
            when (id) {
                CURRENT_GAME,
                NEW_GAME,
                PROFILE,
                RANK_LIST,
                EXIT -> {
                    /* finish()
                     System.exit(1)*/
                }
            }
            true
        }
    }

    override fun selectFragment() {
        fragment = if (firebaseAuth.currentUser != null) {
            OnBoardingView.getInstance()
        } else {
            ProfileView.getInstance()
        }
        setFragment(R.id.container, fragment)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

}
