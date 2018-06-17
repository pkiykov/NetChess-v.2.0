package com.netchess.pkiykov.ui

import android.app.FragmentTransaction
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import com.netchess.pkiykov.ui.screens.base.BaseView

abstract class BaseActivity : AppCompatActivity() {

    abstract fun selectFragment()

    fun setFragment(@IdRes container: Int, fragment: BaseView) {
        supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(container, fragment)
                .addToBackStack(fragment.name())
                .commit()
    }
}