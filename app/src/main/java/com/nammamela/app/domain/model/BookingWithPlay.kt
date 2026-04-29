package com.nammamela.app.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class BookingWithPlay(
    @Embedded val booking: Booking,
    @Relation(
        parentColumn = "playId",
        entityColumn = "id"
    )
    val play: Play
)
