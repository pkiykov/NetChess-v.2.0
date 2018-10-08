package com.netchess.pkiykov.ui

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.netchess.pkiykov.ui.screens.base.BaseView

abstract class BaseActivity : AppCompatActivity() {

    protected var fragment: BaseView? = null

    @get:LayoutRes
    protected abstract val contentViewId: Int

    abstract fun selectFragment(fragmentName: IScreen, data: Bundle? = null, addToBackStack: Boolean = false, animation: FragmentAnimation? = null)

    protected fun setFragment(@IdRes containerId: Int, fragment: BaseView, addToBackStack: Boolean = false, animation: FragmentAnimation? = null) {
        this.fragment = fragment
        val transaction = supportFragmentManager.beginTransaction()

        if (animation != null) {
            transaction.setCustomAnimations(animation.enter, animation.exit, animation.popEnter, animation.popExit)
        }
        if (addToBackStack) {
            val fragmentName = fragment.javaClass.simpleName
            val oldFragment = supportFragmentManager.findFragmentByTag(fragmentName)

            if (oldFragment != null) {
                transaction.replace(containerId, oldFragment)
            } else {
                transaction.replace(containerId, fragment)
                transaction.addToBackStack(fragmentName)
            }
        }

        transaction.commit()
    }

    protected fun getMainFragment(): Fragment? = supportFragmentManager.findFragmentById(com.netchess.pkiykov.R.id.container)

    protected abstract fun onCreateActivity(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentViewId)
        onCreateActivity(savedInstanceState)
    }
}