package com.harrrshith.moowe.di

import com.harrrshith.moowe.data.local.MooweDatabase
import com.harrrshith.moowe.data.local.getMovieDatabase
import com.harrrshith.moowe.getDatabaseBuilder
import org.koin.dsl.module

actual val platformModule = module {
    single<MooweDatabase>(createdAtStart = true) { 
        // Eager initialization with createdAtStart to ensure database is ready
        val db = getMovieDatabase(getDatabaseBuilder())
        // Verify database is accessible
        try {
            db.getMooweDao()
            println("✅ iOS Room Database initialized successfully")
        } catch (e: Exception) {
            println("❌ iOS Room Database initialization failed: ${e.message}")
            e.printStackTrace()
            throw e
        }
        db
    }
}