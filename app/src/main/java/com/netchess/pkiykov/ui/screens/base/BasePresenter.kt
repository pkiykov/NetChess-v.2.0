package com.netchess.pkiykov.ui.screens.base

import android.content.Context
import com.netchess.pkiykov.core.dagger.ForApplication
import com.netchess.pkiykov.ui.BaseActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<V : ICleanArchitecture.View, R : BaseRouter>(@ForApplication val context: Context) : ICleanArchitecture.Presenter<V> {

    private val mainThread = AndroidSchedulers.mainThread()
    protected lateinit var router: R
    protected var view: V? = null
    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun disposeAll() = compositeDisposable.clear()

    override fun attachView(view: V) {
        this.view = view
        initRouter(view.setupActivity())
    }

    abstract fun initRouter(activity: BaseActivity)

    override fun detachView() {
        disposeAll()
        view = null
    }

    protected fun isAttached(): Boolean {
        return view != null
    }

    protected fun doSafelyWithView(method: V.() -> Unit) {
        if (isAttached()) {
            mainThread.scheduleDirect {
                if (isAttached()) {
                    view?.method()
                }
            }
        }
    }
}