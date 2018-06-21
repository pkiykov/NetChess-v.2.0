package com.netchess.pkiykov.ui

import android.app.FragmentTransaction
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import com.netchess.pkiykov.ui.screens.base.BaseView

abstract class BaseActivity : AppCompatActivity() {

    @get:LayoutRes
    protected abstract val contentViewId: Int

    abstract fun selectFragment()

    fun setFragment(@IdRes container: Int, fragment: BaseView) {
        supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(container, fragment)
                .addToBackStack(fragment.name)
                .commit()
    }

    protected abstract fun onCreateActivity(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentViewId)
        onCreateActivity(savedInstanceState)
    }
}