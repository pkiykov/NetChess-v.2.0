package com.netchess.pkiykov.ui.dialogs

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import com.afollestad.materialdialogs.MaterialDialog
import com.netchess.pkiykov.R
import java.util.concurrent.TimeUnit

class DialogManager(private val activity: Activity) {

    private var dialog: MaterialDialog? = null
    private var progressDialog: MaterialDialog =
            MaterialDialog.Builder(activity)
                    .progress(true, 0)
                    .title(R.string.progress_dialog_title)
                    .cancelable(false)
                    .build()


    @SuppressLint("InflateParams")
    fun showChangeNameDialog(listener: ChangeNameDialogListener) {
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_new_player_name, null, false)
        val editName = view.findViewById<EditText>(R.id.edit_name)
        dialog = MaterialDialog.Builder(activity)
                .title(R.string.dialog_input_name)
                .customView(view, true)
                .positiveText(R.string.done)
                .negativeText(R.string.cancel)
                .onPositive { _, _ ->
                    listener.changeName(editName.text.toString())
                    dialog!!.dismiss()
                }
                .onNegative { _, _ -> dialog!!.dismiss() }
                .cancelable(true)
                .build().apply { show() }
    }

    fun showChangeBirthdateDialog(changeBirthdateListener: ChangeBirthdateListener) {
        dialog = MaterialDialog.Builder(activity)
                .customView(R.layout.dialog_birthdate, false)
                .build().also {
                    val view = it.customView!!
                    val picker = view.findViewById(R.id.date_picker) as DatePicker
                    picker.minDate = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(40000)
                    picker.maxDate = System.currentTimeMillis()
                    picker.updateDate(2000, 0, 1)

                    val positiveButton = view.findViewById(R.id.button_ok) as Button
                    positiveButton.setOnClickListener { _ ->
                        val month = picker.month + 1
                        val date = picker.year.toString() + "-" +
                                String.format("%02d", month) + "-" +
                                String.format("%02d", picker.dayOfMonth)
                        changeBirthdateListener.onBirthdateChanged(date)
                        it.dismiss()
                    }

                    val cancelBtn = view.findViewById(R.id.button_cancel) as Button
                    cancelBtn.setOnClickListener { _ -> it.dismiss() }
                    it.show()
                }
    }

    fun isDialogVisible(): Boolean = dialog?.isShowing == true || progressDialog.isShowing

    fun dismissDialog() {
        dialog?.dismiss()
        progressDialog.dismiss()
    }

    fun showProgressDialog() {
        progressDialog.show()
    }

    fun dismissProgressDialog() {
        progressDialog.dismiss()
    }

    fun showRemoveAvatarDialog(removeAvatarListener: DialogManager.RemoveAvatarListener) {
        dialog = MaterialDialog.Builder(activity)
                .title(R.string.do_you_want_to_remove_avatar)
                .positiveText(R.string.button_ok_text)
                .negativeText(R.string.cancel)
                .onPositive { _, _ ->
                    removeAvatarListener.removeAvatar()
                    dialog!!.dismiss()
                }
                .onNegative { _, _ -> dialog!!.dismiss() }
                .cancelable(true)
                .build().apply { show() }
    }

    fun showErrorDialog(titleId: Int, contentId: Int) {
        dialog = MaterialDialog.Builder(activity)
                .title(titleId)
                .content(contentId)
                .cancelable(true)
                .show()
    }

    interface ChangeNameDialogListener {
        fun changeName(name: String)
    }

    interface ChangeBirthdateListener {
        fun onBirthdateChanged(date: String)
    }

    interface RemoveAvatarListener {
        fun removeAvatar()
    }
}