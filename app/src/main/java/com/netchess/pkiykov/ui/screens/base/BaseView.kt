package com.netchess.pkiykov.ui.screens.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import icepick.Icepick

abstract class BaseView : Fragment() {

    protected lateinit var rootView: View

    abstract val name: String?

    protected abstract fun initPresenter()

    protected abstract fun bindViews()

    @LayoutRes
    protected abstract fun getContentLayoutID(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(getContentLayoutID(), container, false)
        bindViews()
        initPresenter()
        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Icepick.restoreInstanceState(this, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Icepick.saveInstanceState(this, outState)
    }

}