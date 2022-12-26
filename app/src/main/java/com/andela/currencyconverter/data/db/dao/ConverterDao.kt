package com.andela.currencyconverter.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andela.currencyconverter.data.db.model.ConverterData

@Dao
interface ConverterDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: ConverterData) : Long

    @Query("select * From converter where date>=:startDate and date<=:endDate")
    fun  fetch(startDate : String, endDate : String) : LiveData<MutableList<ConverterData>>

}