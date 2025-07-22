package com.harrrshith.moowe

import androidx.room.Room
import androidx.room.RoomDatabase
import com.harrrshith.moowe.data.local.DB_FILE_NAME
import com.harrrshith.moowe.data.local.MooweDatabase
import java.io.File

fun getDatabaseBuilder(): RoomDatabase.Builder<MooweDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), DB_FILE_NAME)
    return Room.databaseBuilder<MooweDatabase>(
        name = dbFile.absolutePath,
    )
}