package com.netchess.pkiykov.ui.dialogs

/*
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.UserProfileChangeRequest
import com.netchess.pkiykov.R
import com.netchess.pkiykov.core.Constants

class ChangeNameDialog(private val context : Context,
                       private val viewEventListener: ViewEventListener) : BaseAlertDialog(context) {

    private lateinit var editName : EditText

    override fun getContentLayoutID(): Int = R.layout.dialog_new_player_name

    override fun bindViews() {
        editName = dialogView.findViewById(R.id.edit_name)
    }

    override fun create() {
        super.create()

        val enterName = context.getString(R.string.enter_name)
        val done = context.getString(R.string.done)
        val cancel = context.getString(R.string.cancel)
        val nameChanged = context.getString(R.string.name_changed)

        val changeNameDialog = buildDialog(dialogView, true, enterName, "", done, cancel,
                DialogInterface.OnClickListener { dialogInterface, i ->
            val playerNameNew = editName.text.toString()
            if (!playerNameNew.isEmpty()) {
                helper.getRef().child(Constants.NAME).setValue(playerNameNew)
                        .addOnSuccessListener(OnSuccessListener<Void> {
                    Toast.makeText(context, nameChanged, Toast.LENGTH_SHORT).show() })
                        .addOnFailureListener(OnFailureListener { e -> Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show() })
                val userChangeRequest = UserProfileChangeRequest.Builder().setDisplayName(playerNameNew).build()
                mAuth.getCurrentUser()!!.updateProfile(userChangeRequest)
                playerName.setText(playerNameNew)
            } else {
                viewEventListener.showSnackBar(context.resources.getString(R.string.name_can_not_be_empty))
            }
        }, DialogInterface.OnClickListener { dialogInterface, _ -> dialogInterface.dismiss() })

        changeNameDialog.show()
    }



    private fun buildDialog(view: View,
                    cancelable: Boolean,
                    title: String,
                    message: String,
                    positiveBtn: String,
                    negativeBtn: String,
                    positive: DialogInterface.OnClickListener,
                    negative: DialogInterface.OnClickListener): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(context).apply {
            setTitle(title)
            setMessage(message)
            setView(view)
            setPositiveButton(positiveBtn, positive)
            setNegativeButton(negativeBtn, negative)
        }

        return dialogBuilder.create().apply {
            setCanceledOnTouchOutside(false)
            setCancelable(cancelable)
            setOnShowListener { dialogInterface ->
                val window = (dialogInterface as AlertDialog).window
                val outValue = TypedValue()
                context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

                val positiveButton = dialogInterface.getButton(DialogInterface.BUTTON_POSITIVE)
                positiveButton.setBackgroundResource(outValue.resourceId)

                val negativeButton = dialogInterface.getButton(DialogInterface.BUTTON_NEGATIVE)
                negativeButton.setBackgroundResource(outValue.resourceId)
            }
        }
    }
}*/
