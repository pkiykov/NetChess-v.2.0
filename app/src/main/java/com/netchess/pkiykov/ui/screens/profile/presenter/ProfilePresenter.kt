package com.netchess.pkiykov.ui.screens.profile.presenter

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import com.netchess.pkiykov.R
import com.netchess.pkiykov.core.Constants
import com.netchess.pkiykov.core.PlayerInfo
import com.netchess.pkiykov.core.dagger.ForApplication
import com.netchess.pkiykov.core.exceptions.PlayerNotFoundException
import com.netchess.pkiykov.core.exceptions.UnsupportedPlayerInfoException
import com.netchess.pkiykov.core.interactors.FirebaseInteractor
import com.netchess.pkiykov.core.models.Player
import com.netchess.pkiykov.ui.BaseActivity
import com.netchess.pkiykov.ui.components.ErrorStyle
import com.netchess.pkiykov.ui.screens.base.BasePresenter
import com.netchess.pkiykov.ui.screens.profile.IProfile
import com.netchess.pkiykov.ui.screens.profile.router.ProfileRouter
import javax.inject.Inject


class ProfilePresenter @Inject constructor(private val firebaseInteractor: FirebaseInteractor,
                                           @ForApplication context: Context) :
        BasePresenter<IProfile.View, ProfileRouter>(context), IProfile.Presenter {

    override fun initRouter(activity: BaseActivity) {
        router = ProfileRouter(activity)
    }

    override fun startPickImageActivity() {
        router.startPickImageActivity()
    }

    override fun startCropImageActivity(uri: Uri) {
        router.startCropImageActivity(uri)
    }

    override fun isCurrentUser(): Boolean = firebaseInteractor.isCurrentUser()

    override fun attachView(view: IProfile.View) {
        super.attachView(view)
        if (firebaseInteractor.player == null) {
            firebaseInteractor.playerId = view.getPlayerIdFromArguments()
            loadPlayerInfo()
        }
    }

    override fun uploadAvatar(uri: Uri) {
        compositeDisposable.add(firebaseInteractor.uploadAvatar(uri)
                .doOnSubscribe {
                    doSafelyWithView {
                        showProgressDialog()
                    }
                }
                .doFinally { doSafelyWithView { dismissProgressDialog() } }
                .subscribe(
                        { doSafelyWithView { loadAvatar(uri) } },
                        {
                            doSafelyWithView {
                                handleError(it)
                                showSimpleSnackbarMessage(context.getString(R.string.fail_to_load_avatar))
                            }
                        }
                ))
    }

    private fun getTransparencyValue(): Float {
        val outValue = TypedValue()
        context.resources.getValue(R.dimen.half_transparency, outValue, true)
        return outValue.float
    }

    override fun loadPlayerInfo() {
        compositeDisposable.add(firebaseInteractor.getPlayer()
                .doOnSubscribe {
                    doSafelyWithView {
                        showProgressDialog()
                        playerNotAvailable(getTransparencyValue())
                    }
                }
                .doFinally { doSafelyWithView { dismissProgressDialog() } }
                .subscribe({
                    loadPhoto()
                    createPlayer(it)
                    if (firebaseInteractor.isCurrentUser()) {
                        doSafelyWithView { activatePlayer() }
                    }
                }, { error ->
                    when (error) {
                        PlayerNotFoundException() -> {
                            doSafelyWithView { showError(ErrorStyle.PLAYER_NOT_EXIST) }
                            if (firebaseInteractor.isCurrentUser()) {
                                firebaseInteractor.removePlayer()
                            }
                            router.openPreviousScreen()
                        }
                        else -> doSafelyWithView { showError(ErrorStyle.UNKNOWN_ERROR) }
                    }
                }))
    }

    private fun createPlayer(player: Player) {
        val totalGames = player.wins + player.draws + player.losses
        val list = arrayOf(
                context.getString(R.string.rating) + player.rating,
                context.getString(R.string.age) + player.age,
                context.getString(R.string.wins) + player.wins,
                context.getString(R.string.losses) + player.losses,
                context.getString(R.string.draws) + player.draws,
                context.getString(R.string.total) + totalGames)
        doSafelyWithView {
            showPlayerStats(list)
            setPlayerName(player.name)
        }
    }

    private fun loadPhoto() {
        compositeDisposable.add(firebaseInteractor.loadAvatar()
                .subscribe(
                        { doSafelyWithView { loadAvatar(it) } },
                        { doSafelyWithView { handleError(it) } }
                ))
    }

    override fun onAvatarClick() {
        router.onAvatarClick()
    }

    override fun onPlayerNameChanged(name: String) {
        if (name.isBlank()) {
            doSafelyWithView { onFailToChangeName(context.resources.getString(R.string.name_can_not_be_empty)) }
        } else {
            compositeDisposable.add(firebaseInteractor.setPlayerName(name)
                    .doOnSubscribe {
                        doSafelyWithView {
                            showProgressDialog()
                        }
                    }
                    .doFinally { doSafelyWithView { dismissProgressDialog() } }
                    .subscribe(
                            { doSafelyWithView { onNameChanged(name) } },
                            {
                                doSafelyWithView {
                                    handleError(it)
                                    onFailToChangeName(context.resources.getString(R.string.connection_problem))
                                }
                            }
                    ))
        }
    }

    override fun onLogoutClick() {
        firebaseInteractor.signOut()
        router.onLogoutClick()
    }

    override fun onAvatarRemoveClick() {
        compositeDisposable.add(firebaseInteractor.removeAvatar()
                .doOnSubscribe {
                    doSafelyWithView {
                        showProgressDialog()
                    }
                }
                .doFinally { doSafelyWithView { dismissProgressDialog() } }
                .subscribe({ doSafelyWithView { onAvatarRemoved() } },
                        {
                            doSafelyWithView {
                                handleError(it)
                                onFailToRemoveAvatar()
                            }
                        }
                ))
    }

    override fun onPlayerInfoClick(position: Int) {
        val playerInfo = PlayerInfo.getByPosition(position)
        when (playerInfo) {
            PlayerInfo.RATING -> {
                val bundle = Bundle()
                bundle.putString(Constants.PLAYER_ID, firebaseInteractor.playerId)
                router.openRankListScreen(bundle)
            }
            PlayerInfo.AGE -> {
                if (firebaseInteractor.isCurrentUser()) {
                    doSafelyWithView { showChangeBirthdateDialog() }
                } else {
                    doSafelyWithView { showSimpleSnackbarMessage(context.getString(R.string.player_birthdate) + firebaseInteractor.playerBirthdate) }
                }
            }
            PlayerInfo.TOTAL_GAMES -> {
                val bundle = Bundle()
                bundle.putString(Constants.PLAYER_ID, firebaseInteractor.playerId)
                router.openArchiveGamesScreen(bundle)
            }
            else -> {
                throw UnsupportedPlayerInfoException()
            }
        }
    }

    override fun onBirthdateChanged(date: String) {
        compositeDisposable.add(firebaseInteractor.setBirthdate(date)
                .doOnSubscribe {
                    doSafelyWithView {
                        showProgressDialog()
                    }
                }
                .doFinally { doSafelyWithView { dismissProgressDialog() } }
                .subscribe({
                    doSafelyWithView { showBirthdateIsChangedMessage() }
                }, {
                    doSafelyWithView {
                        handleError(it)
                    }
                }))
    }
}