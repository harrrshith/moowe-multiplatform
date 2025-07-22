package com.harrrshith.moowe.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.harrrshith.moowe.data.local.entity.MovieEntity
import kotlinx.coroutines.Dispatchers

@Database(entities = [MovieEntity::class], version = 1)
@ConstructedBy(MooweDatabaseConstructor::class)
abstract class MooweDatabase: RoomDatabase() {
    abstract fun getMooweDao(): MooweDao
}
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object MooweDatabaseConstructor: RoomDatabaseConstructor<MooweDatabase> {
    override fun initialize(): MooweDatabase

}


//fun getMovieDatabase(builder: RoomDatabase.Builder<MooweDatabase>): MooweDatabase {
//    return builder
//        .setDriver(BundledSQLiteDriver())
//        .setQueryCoroutineContext(Dispatchers.IO)
//        .build()
//}

internal const val DB_FILE_NAME = "moowe.db"