package com.netchess.pkiykov.core.models

import android.os.Parcelable
import com.netchess.pkiykov.core.Constants.INITIAL_PLAYER_RATING
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Player(val id: String = "",
                  var name: String = "",
                  var birthdate: String = "1970-01-01",
                  var rating: Int = INITIAL_PLAYER_RATING,
                  var wins: Int = 0,
                  var losses: Int = 0,
                  var draws: Int = 0) : Parcelable, Serializable {

    val age: Int
        get() {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            var birth: Date? = null
            try {
                birth = dateFormat.parse(this.birthdate)
            } catch (e: ParseException) {
            }

            val current = Date()
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = current.time - birth!!.time
            return calendar.get(Calendar.YEAR) - 1970
        }

    private val k: Int
        get() {
            val rating = rating
            val age = age
            val gamesCount = draws + wins + losses
            var k: Int
            k = if (rating >= 2400) {
                10
            } else {
                20
            }
            if (rating < 2300 && age < 18 || gamesCount <= 30) {
                k = 40
            }
            return k
        }


    private fun getExpValue(rating: Int): Double {
        return 1.0 / (1.0 + Math.pow(10.0, (rating - this.rating).toDouble() / 400.0))
    }

    fun resultsAfterWin(rating: Int) {
        this.rating = (rating.toDouble() + k.toDouble() * (1.0 - getExpValue(rating))).toInt()
        wins += 1
    }

    fun resultsAfterLose(rating: Int) {
        this.rating = (rating.toDouble() + k.toDouble() * (0.0 - getExpValue(rating))).toInt()
        losses += 1
    }

    fun resultsAfterDraw(rating: Int) {
        this.rating = (rating.toDouble() + k.toDouble() * (0.5 - getExpValue(rating))).toInt()
        draws += 1
    }

}