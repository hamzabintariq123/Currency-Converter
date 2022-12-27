package com.andela.currencyconverter.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.andela.currencyconverter.data.db.dao.ConverterDao
import com.andela.currencyconverter.data.db.model.ConverterData
import com.andela.currencyconverter.utils.AppConstants.Constants.DB_NAME

@Database(entities = [ConverterData::class], version = 1, exportSchema = false)
abstract class CurrencyConverterDb : RoomDatabase() {
    /**
     * Connects the database to the DAO.
     */
    abstract val converterDao: ConverterDao

    companion object {

        @Volatile
        private var INSTANCE: CurrencyConverterDb? = null

        fun getInstance(context: Context): CurrencyConverterDb {
            // Multiple threads can ask for the database at the same time, ensure we only initialize
            // it once by using synchronized. Only one thread may enter a synchronized block at a
            // time.
            synchronized(this) {

                // Copy the current value of INSTANCE to a local variable so Kotlin can smart cast.
                // Smart cast is only available to local variables.
                var instance = INSTANCE

                // If instance is `null` make a new database instance.
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CurrencyConverterDb::class.java,
                        DB_NAME
                    ).fallbackToDestructiveMigration()
                        .build()
                    // Assign INSTANCE to the newly created database.
                    INSTANCE = instance
                }

                // Return instance; smart cast to be non-null.
                return instance
            }
        }
    }
}