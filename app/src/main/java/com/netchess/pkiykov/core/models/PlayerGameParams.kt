package com.netchess.pkiykov.core.models

import android.os.Parcel
import android.os.Parcelable

class PlayerGameParams() : Parcelable {

    var timeRest: Long = 0
    var timePicker1: Int = 0
    var timePicker2: Int = 0
    var timeControl: Int = 0
    var handicap: Int = -1
    var pauseCount: Int = 0
    var isCancelableMoves: Boolean = false

    constructor(parcel: Parcel) : this() {
        timeRest = parcel.readLong()
        timePicker1 = parcel.readInt()
        timePicker2 = parcel.readInt()
        timeControl = parcel.readInt()
        handicap = parcel.readInt()
        pauseCount = parcel.readInt()
        isCancelableMoves = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(timeRest)
        parcel.writeInt(timePicker1)
        parcel.writeInt(timePicker2)
        parcel.writeInt(timeControl)
        parcel.writeInt(handicap)
        parcel.writeInt(pauseCount)
        parcel.writeByte(if (isCancelableMoves) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlayerGameParams> {
        override fun createFromParcel(parcel: Parcel): PlayerGameParams {
            return PlayerGameParams(parcel)
        }

        override fun newArray(size: Int): Array<PlayerGameParams?> {
            return arrayOfNulls(size)
        }
    }
}