package com.netchess.pkiykov.ui.screens.profile

import android.net.Uri
import com.netchess.pkiykov.ui.components.ErrorStyle
import com.netchess.pkiykov.ui.screens.base.ICleanArchitecture

interface IProfile {

    interface View : ICleanArchitecture.View {

        fun playerNotAvailable(transparency: Float)
        fun getPlayerIdFromArguments(): String?
        fun onAvatarRemoved()
        fun onFailToRemoveAvatar()
        fun onFailToChangeName(problem: String)
        fun onNameChanged(name: String)
        fun showError(errorStyle: ErrorStyle)
        fun loadAvatar(uri: Uri)
        fun activatePlayer()
        fun showPlayerStats(playerStats: Array<String>)
        fun showBirthdateIsChangedMessage()
        fun setPlayerName(name: String?)
        fun showChangeBirthdateDialog()
    }

    interface Presenter {

        fun attachView(view: View)
        fun detachView()
        fun onAvatarRemoveClick()
        fun onPlayerNameChanged(name: String)
        fun onLogoutClick()
        fun onAvatarClick()
        fun isCurrentUser(): Boolean
        fun startCropImageActivity(uri: Uri)
        fun startPickImageActivity()
        fun onBirthdateChanged(date: String)
        fun onPlayerInfoClick(position: Int)
        fun loadPlayerInfo()
        fun uploadAvatar(uri: Uri)
    }
}