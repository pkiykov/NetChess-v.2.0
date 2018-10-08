package com.netchess.pkiykov.core.interactors

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import com.netchess.pkiykov.core.Constants
import com.netchess.pkiykov.core.FirebaseErrorWrapper
import com.netchess.pkiykov.core.exceptions.PlayerNotFoundException
import com.netchess.pkiykov.core.models.Player
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class FirebaseInteractor @Inject constructor(private val firebaseAuth: FirebaseAuth,
                                             private val databaseReference: DatabaseReference,
                                             private val storageReference: StorageReference) {


    var player: Player? = null

    var playerId: String? = null
        set(value) {
            if (value == null) {
                field = getCurrentUserId()
            }
        }

    private var playerDatabaseReference: DatabaseReference? = null
        get() {
            return if (playerId != null) {
                databaseReference
                        .child(Constants.PLAYERS)
                        .child(playerId!!)
            } else {
                null
            }
        }

    private var playerStorageReference: StorageReference? = null
        get() {
            return if (playerId != null) {
                storageReference
                        .child(Constants.AVATARS)
                        .child(playerId!!)
            } else {
                null
            }
        }

    var playerBirthdate: String? = null
        get() {
            if (player != null) {
                return player!!.birthdate
            }
            return null
        }

    private fun getCurrentUser() = firebaseAuth.currentUser

    private fun getCurrentUserId() = getCurrentUser()?.uid

    fun isCurrentUser() = getCurrentUserId() == playerId

    fun getPlayer(): Single<Player> =
            Single.create<Player> { emitter ->
                if (playerDatabaseReference == null) {
                    emitter.onError(PlayerNotFoundException())
                } else {
                    playerDatabaseReference!!.addValueEventListener(object : ValueEventListener {

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            player = dataSnapshot.getValue(object : GenericTypeIndicator<Player>() {})
                            playerDatabaseReference!!.removeEventListener(this)
                            emitter.onSuccess(player!!)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            emitter.onError(FirebaseErrorWrapper(error))
                        }
                    })
                }
            }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())


    fun setPlayerName(name: String): Completable =
            Completable.fromAction {
                databaseReference.child(Constants.NAME).setValue(name)
                        .continueWithTask {
                            val userChangeRequest = UserProfileChangeRequest.Builder().setDisplayName(name).build()
                            firebaseAuth.currentUser!!.updateProfile(userChangeRequest)
                        }
                        .addOnSuccessListener {
                            player!!.name = name
                            Completable.complete()
                        }
                        .addOnFailureListener { Completable.error(it) }
            }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun setBirthdate(date: String): Completable =
            Completable.fromAction {
                playerDatabaseReference?.child(Constants.BIRTHDATE)?.setValue(date)
                        ?.addOnSuccessListener {
                            player!!.birthdate = date
                            Completable.complete()
                        }
                        ?.addOnFailureListener { Completable.error(it) }
            }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    fun removePlayer() {
        getCurrentUser()?.delete()
        playerDatabaseReference?.removeValue()
        playerStorageReference?.delete()
        firebaseAuth.signOut()
    }

    fun removeAvatar(): Completable =
            Completable.fromAction {
                playerStorageReference?.delete()
                        ?.addOnSuccessListener { Completable.complete() }
                        ?.addOnFailureListener { Completable.error(it) }
            }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    fun loadAvatar(): Single<Uri> {
        return Single.create<Uri> { emitter ->
            playerStorageReference?.downloadUrl
                    ?.addOnSuccessListener { uri -> emitter.onSuccess(uri) }
                    ?.addOnFailureListener { emitter.onError(it) }
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun uploadAvatar(imageUri: Uri): Completable =
            Completable.fromAction {
                playerStorageReference?.putFile(imageUri)
                        ?.addOnSuccessListener { Completable.complete() }
                        ?.addOnFailureListener { Completable.error(it) }
            }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

}

