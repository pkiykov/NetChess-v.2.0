package com.netchess.pkiykov.ui.components

import android.support.annotation.StringRes
import com.netchess.pkiykov.R

enum class ErrorStyle(@StringRes val titleId: Int, @StringRes val contentId: Int) {

    NETWORK_ERROR(R.string.error_title, R.string.network_error),
    UNKNOWN_ERROR(R.string.error_title, R.string.error_unknown),
    PLAYER_NOT_EXIST(R.string.error_title, R.string.player_not_exist),
    INCORRECT_CREDENTIAL_ERROR(R.string.error_title, R.string.error_credentials_incorrect);
}