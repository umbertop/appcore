package com.umbertop.appcore.livedata

import com.umbertop.appcore.extensions.exhaustive

sealed class Result<out Success, out Failure> {
    data class Success<out Success>(val value: Success): Result<Success, Nothing>()
    data class Failure<out Failure>(val reason: Failure): Result<Nothing, Failure>()

    override fun toString(): String {
        return when(this){
            is Result.Success -> "Success[value=$value]"
            is Result.Failure -> "Failure[reason=$reason]"
        }.exhaustive
    }
}