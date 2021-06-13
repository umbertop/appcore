package com.umbertop.appcore.livedata

import com.umbertop.appcore.extensions.exhaustive

/**
 * Network specific [Result] for handling network requests.
 */
sealed class NetworkResult<out Result> {
    data class Success<out Result>(val data: Result) : NetworkResult<Result>()
    data class Error(val errorCode: Int) : NetworkResult<Nothing>()

    object Loading : NetworkResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[data=$data]"
            is Error -> "Error[code=$errorCode]"
            Loading -> "Loading"
        }.exhaustive
    }
}