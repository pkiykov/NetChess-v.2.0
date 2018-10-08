package com.netchess.pkiykov.ui

import android.support.annotation.AnimRes
import android.support.annotation.AnimatorRes

data class FragmentAnimation(
        @AnimatorRes @AnimRes val enter: Int,
        @AnimatorRes @AnimRes val exit: Int,
        @AnimatorRes @AnimRes val popEnter: Int,
        @AnimatorRes @AnimRes val popExit: Int
)