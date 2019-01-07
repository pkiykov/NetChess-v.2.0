package com.netchess.pkiykov.ui.screens.profile.router

import android.Manifest
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat.requestPermissions
import com.netchess.pkiykov.ui.BaseActivity
import com.netchess.pkiykov.ui.MainActivity
import com.netchess.pkiykov.ui.Screen
import com.netchess.pkiykov.ui.screens.base.BaseRouter
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView


class ProfileRouter(dispatcher: BaseActivity) : BaseRouter(dispatcher) {

    fun openLoginScreen() {
        dispatcher.selectFragment(Screen.LOGIN)
    }

    fun openRankListScreen() {
        dispatcher.selectFragment(Screen.RANK_LIST)
    }

    fun openRankListScreen(data: Bundle) {
        dispatcher.selectFragment(Screen.RANK_LIST, data)
    }

    fun startCropImageActivity(imageUri: Uri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setFixAspectRatio(true)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setBorderLineColor(Color.RED)
                .setGuidelinesColor(Color.GREEN)
                .start(dispatcher)
    }

    fun startPickImageActivity() {
        CropImage.startPickImageActivity(dispatcher)
    }

    fun onAvatarClick() {
        if (CropImage.isExplicitCameraPermissionRequired(dispatcher)) {
            requestPermissions(dispatcher, arrayOf(Manifest.permission.CAMERA), CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE)
        } else {
            CropImage.startPickImageActivity(dispatcher)
        }
    }

    fun openArchiveGamesScreen(bundle: Bundle) {
        dispatcher.selectFragment(Screen.ARCHIVE_GAMES_LIST, bundle)
    }

    fun onLogoutClick() {
        (dispatcher as MainActivity).signOut()
    }
}