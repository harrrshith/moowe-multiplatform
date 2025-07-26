@file:Suppress("UNCHECKED_CAST")

package com.harrrshith.moowe.ui.utility

import com.harrrshith.moowe.domain.utility.Result

fun <T> Result<T>.successOrEmpty(): T {
    return when (this) {
        is Result.Success -> this.data
        else -> emptyList<Any>() as T
    }
}