package com.netchess.pkiykov.ui.screens.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.netchess.pkiykov.ui.BaseActivity
import com.netchess.pkiykov.ui.dialogs.DialogManager
import icepick.Icepick
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseView : Fragment(), ICleanArchitecture.View {

    protected lateinit var rootView: View
    protected lateinit var compositeDisposable: CompositeDisposable
    protected lateinit var dialogManager: DialogManager
    //@State @JvmField protected var shouldShowDialog = false

    abstract val name: String?

    protected abstract fun initPresenter()

    @LayoutRes
    protected abstract fun getContentLayoutID(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        compositeDisposable = CompositeDisposable()
        rootView = inflater.inflate(getContentLayoutID(), container, false)
        dialogManager = DialogManager(setupActivity())
        initPresenter()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCreateViewFragment()
    }

    protected abstract fun onCreateViewFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Icepick.restoreInstanceState(this, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // shouldShowDialog = dialogManager.isDialogVisible()
        // dialogManager.dismissDialog()
        // saveManagedDialogs(outState)
        super.onSaveInstanceState(outState)
        Icepick.saveInstanceState(this, outState)
    }

    protected fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onDestroyView() {
        compositeDisposable.dispose()
        super.onDestroyView()
    }

    override fun showProgressDialog() {
        dialogManager.showProgressDialog()
    }

    override fun dismissProgressDialog() {
        dialogManager.dismissProgressDialog()
    }

    override fun handleError(throwable: Throwable) {
        // Timber.e("onError: " + e.toString()) TODO: Add logging
        //showError(ErrorStyle.UNKNOWN_ERROR)
        showSimpleSnackbarMessage(throwable.message.toString())
    }

    override fun showSimpleSnackbarMessage(message: String) {
        view?.let { Snackbar.make(view!!, message, Snackbar.LENGTH_SHORT).show() }
    }

    override fun setupActivity(): BaseActivity = this.activity as BaseActivity

}