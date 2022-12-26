package com.andela.currencyconverter.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "converter")
data class ConverterData (
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0L,
    val from : String,
    val to : String,
    val amount : String,
    val result : String,
    val date : String
)