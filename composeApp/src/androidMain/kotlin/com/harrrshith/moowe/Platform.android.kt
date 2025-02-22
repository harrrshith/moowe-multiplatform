package com.harrrshith.moowe

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun log(msg: String) {
    android.util.Log.d("Moowe", msg)
}