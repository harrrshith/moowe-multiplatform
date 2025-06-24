package com.harrrshith.moowe

import androidx.compose.ui.window.ComposeUIViewController
import com.harrrshith.moowe.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}