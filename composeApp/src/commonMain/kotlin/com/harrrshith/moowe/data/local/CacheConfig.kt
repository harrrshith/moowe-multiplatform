package com.harrrshith.moowe.data.local

/**
 * Configuration for cache behavior
 */
data class CacheConfig(
    /**
     * Cache expiration time in milliseconds
     * Default: 1 hour
     */
    val expirationTimeMillis: Long = 60 * 60 * 1000, // 1 hour
    
    /**
     * Maximum number of movies to store in cache
     * When exceeded, oldest accessed items will be removed (LRU)
     * Default: 1000 movies
     */
    val maxCacheSize: Int = 1000,
    
    /**
     * Whether to use stale-while-revalidate strategy
     * If true, returns cached data immediately while fetching fresh data
     * Default: true
     */
    val staleWhileRevalidate: Boolean = true,
    
    /**
     * Time after which cache is considered stale (but still usable) in milliseconds
     * Used for stale-while-revalidate strategy
     * Default: 5 minutes
     */
    val staleTimeMillis: Long = 5 * 60 * 1000 // 5 minutes
) {
    companion object {
        val DEFAULT = CacheConfig()
        
        // Aggressive caching for offline-first experience
        val OFFLINE_FIRST = CacheConfig(
            expirationTimeMillis = 24 * 60 * 60 * 1000, // 24 hours
            maxCacheSize = 2000,
            staleWhileRevalidate = true,
            staleTimeMillis = 60 * 60 * 1000 // 1 hour
        )
        
        // Fresh data priority
        val FRESH_PRIORITY = CacheConfig(
            expirationTimeMillis = 5 * 60 * 1000, // 5 minutes
            maxCacheSize = 500,
            staleWhileRevalidate = false,
            staleTimeMillis = 60 * 1000 // 1 minute
        )
    }
}
