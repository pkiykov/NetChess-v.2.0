package com.netchess.pkiykov.ui.dialogs

import android.support.annotation.IdRes

interface ViewEventListener {

    fun showSnackBar(@IdRes message: String)
}