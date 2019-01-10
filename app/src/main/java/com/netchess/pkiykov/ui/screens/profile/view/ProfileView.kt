package com.netchess.pkiykov.ui.screens.profile.view

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.firebase.storage.StorageException
import com.netchess.pkiykov.R
import com.netchess.pkiykov.core.App
import com.netchess.pkiykov.core.Constants
import com.netchess.pkiykov.core.Constants.PLAYER_ID
import com.netchess.pkiykov.ui.components.ErrorStyle
import com.netchess.pkiykov.ui.dialogs.DialogManager
import com.netchess.pkiykov.ui.screens.base.BaseView
import com.netchess.pkiykov.ui.screens.profile.IProfile
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileView : BaseView(), IProfile.View, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var listView: ListView
    lateinit var presenter: IProfile.Presenter

    override fun getContentLayoutID(): Int = R.layout.fragment_profile

    companion object {

        fun getInstance(): ProfileView = ProfileView()

        fun getInstance(data: Bundle?): ProfileView = ProfileView().apply {
            arguments = createArguments(data)
        }

        private fun createArguments(data: Bundle?): Bundle =
                Bundle().apply {
                    val playerId = data?.getString(PLAYER_ID)
                    if (playerId != null) {
                        putString(PLAYER_ID, data.getString(PLAYER_ID))
                    }
                }

    }

    override fun onCreateViewFragment() {
        playerName.setOnClickListener {
            dialogManager.showChangeNameDialog(object : DialogManager.ChangeNameDialogListener {

                override fun changeName(name: String) {
                    presenter.onPlayerNameChanged(name)
                }
            })
        }
        logout.setOnClickListener { presenter.onLogoutClick() }
        avatar.setOnClickListener { presenter.onAvatarClick() }
        avatar.setOnLongClickListener {
            if (avatar.getTag(R.id.avatar_tag) != Constants.EMPTY_AVATAR) {
                dialogManager.showRemoveAvatarDialog(object : DialogManager.RemoveAvatarListener {
                    override fun removeAvatar() {
                        presenter.onAvatarRemoveClick()
                    }
                })
            }
            true
        }

        avatar.setTag(R.id.avatar_tag, Constants.EMPTY_AVATAR)
        swipeLayout.setOnRefreshListener(this)
        listView = rootView.findViewById(android.R.id.list)
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            presenter.onPlayerInfoClick(i)
        }
    }

    override fun onRefresh() {
        presenter.loadPlayerInfo()
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    override fun initPresenter() {
        presenter = App.presenterComponent().getProfilePresenter()
    }

    override fun getPlayerIdFromArguments(): String? = arguments?.getString(PLAYER_ID)

    override fun onAvatarRemoved() {
        avatar.setImageResource(R.drawable.empty_avatar)
        avatar.setTag(R.id.avatar_tag, Constants.EMPTY_AVATAR)
        Snackbar.make(rootView, R.string.avatar_has_been_removed, Snackbar.LENGTH_SHORT).show()
    }

    override fun onFailToRemoveAvatar() {
        Snackbar.make(rootView, R.string.removing_avatar_failed, Snackbar.LENGTH_SHORT).show()
    }

    override fun showBirthdateIsChangedMessage() {
        Snackbar.make(rootView, getString(R.string.birthdate_has_been_changed), Toast.LENGTH_SHORT).show()
    }


    override fun showError(errorStyle: ErrorStyle) {
        dialogManager.showErrorDialog(errorStyle.titleId, errorStyle.contentId)
    }

    override fun playerNotAvailable(transparency: Float) {
        playerName.alpha = transparency
        logout.alpha = transparency
        playerName.isEnabled = false
        logout.isEnabled = false

    }

    override fun activatePlayer() {
        playerName.isEnabled = true
        playerName.alpha = 1f
        logout.alpha = 1f
        logout.isEnabled = true
    }

    override fun onFailToChangeName(problem: String) {
        Snackbar.make(view!!, problem, Toast.LENGTH_SHORT).show()
    }

    override fun onNameChanged(name: String) {
        playerName.text = name
        Snackbar.make(view!!, R.string.name_has_been_changed, Toast.LENGTH_SHORT).show()
    }

    override fun loadAvatar(uri: Uri) {
        Glide.with(context!!).load(uri)
                .apply(RequestOptions()
                        .fitCenter()
                        .format(DecodeFormat.PREFER_ARGB_8888))
                .listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean) = false

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        avatar.setTag(R.id.avatar_tag, Constants.USER_AVATAR)
                        return false
                    }
                })
                .apply(RequestOptions.circleCropTransform())
                .into(avatar)
    }

    override val name: String? = ProfileView::class.qualifiedName

    override fun showPlayerStats(playerStats: Array<String>) {
        listView.adapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, android.R.id.text1, playerStats)
    }

    override fun setPlayerName(name: String?) {
        playerName.text = name
    }

    override fun showChangeBirthdateDialog() {
        dialogManager.showChangeBirthdateDialog(object : DialogManager.ChangeBirthdateListener {
            override fun onBirthdateChanged(date: String) {
                presenter.onBirthdateChanged(date)
            }
        })
    }

    override fun showProgressDialog() {
        super.showProgressDialog()
        swipeLayout?.isRefreshing = true
    }

    override fun dismissProgressDialog() {
        super.dismissProgressDialog()
        swipeLayout?.isRefreshing = false
    }

    override fun handleError(throwable: Throwable) {
        if (throwable !is StorageException) {
            super.handleError(throwable)
        }
    }
}