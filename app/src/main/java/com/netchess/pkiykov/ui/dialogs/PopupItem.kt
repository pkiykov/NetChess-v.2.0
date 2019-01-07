package com.netchess.pkiykov.ui.dialogs

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class PopupItem(val titleId: Int = 0,
                val messageId: Int = 0,
                val title: String? = null,
                val message: String? = null,
                val positiveBtnId: Int = 0,
                val neutralBtnId: Int = 0,
                val negativeBtnId: Int = 0,
                val buttons: Int = 0) : Parcelable