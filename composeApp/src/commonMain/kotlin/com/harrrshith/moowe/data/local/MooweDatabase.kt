package com.harrrshith.moowe.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.harrrshith.moowe.data.local.converters.Converters
import com.harrrshith.moowe.data.local.entity.MovieEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [MovieEntity::class],
    version = 3
)
@ConstructedBy(MooweDatabaseConstructor::class)
@TypeConverters(Converters::class)
abstract class MooweDatabase : RoomDatabase() {
    abstract fun getMooweDao(): MooweDao
}
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object MooweDatabaseConstructor: RoomDatabaseConstructor<MooweDatabase> {
    override fun initialize(): MooweDatabase
}
fun getMovieDatabase(builder: RoomDatabase.Builder<MooweDatabase>): MooweDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()
}

internal const val DB_FILE_NAME = "moowe.db"