package com.netchess.pkiykov.ui.screens.profile.view

import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.netchess.pkiykov.R
import com.netchess.pkiykov.ui.screens.base.BaseView

class ProfileView : BaseView() {

    override val name: String? = ProfileView::class.qualifiedName

    private lateinit var deleteAvatar: ImageButton
    private lateinit var avatar: ImageView
    private lateinit var logout: Button
    private lateinit var uploadPhoto: Button
    private lateinit var playerName: TextView

    override fun getContentLayoutID(): Int = R.layout.fragment_profile

    companion object {
        @JvmStatic
        fun getInstance(): ProfileView = ProfileView()
    }

    override fun initPresenter() {

    }

    override fun bindViews() {
        deleteAvatar = rootView.findViewById(R.id.delete_avatar)
        avatar = rootView.findViewById(R.id.avatar_image)
        logout = rootView.findViewById(R.id.logout)
        uploadPhoto = rootView.findViewById(R.id.upload_photo)
        playerName = rootView.findViewById(R.id.player_name_text)
    }

}