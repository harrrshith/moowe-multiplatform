package com.harrrshith.moowe

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.harrrshith.moowe.data.local.DB_FILE_NAME
import com.harrrshith.moowe.data.local.MooweDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<MooweDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath(DB_FILE_NAME)

    return Room.databaseBuilder<MooweDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}