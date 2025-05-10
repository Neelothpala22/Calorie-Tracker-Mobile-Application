package com.example.kotlin_compose_database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

@Entity(
    tableName = "CalorieData",
)
data class CalorieData(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    var foodName: String,
    var dateCreated: String,
    var foodCategory: String,
    var calories: Double,
    var photoUri: String? = null

)

fun getFormattedDate(): String {
    return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}


@Dao
interface CaloriesDao{
    @Insert
    fun insertData(calorieObj:CalorieData)

    @Query("SELECT * FROM CalorieData")
    fun getAll():List<CalorieData>


    @Query("SELECT*FROM CalorieData WHERE dateCreated=:date")
    fun getDailyCals(date:String): List<CalorieData>


}

@Database(entities = [CalorieData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract  fun caloriesDao(): CaloriesDao

}

object DatabaseProvider {
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "calorie-base"
            ).allowMainThreadQueries()
                .build()
            INSTANCE = instance
            instance
        }
    }
}
