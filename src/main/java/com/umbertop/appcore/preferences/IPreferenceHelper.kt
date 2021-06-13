package com.umbertop.appcore.preferences

import java.util.*

sealed interface IPreferenceHelper {
    fun <T> get(key: String, default: T): T
    fun <T> set(key: String, value: T): Boolean
    fun remove(vararg keys: String): Boolean

    fun getUniqueId(): UUID {
        val uuidString = get(PREF_UNIQUE_UUID, "")

        return if (uuidString != "") {
            UUID.fromString(uuidString)
        } else {
            val uuid = UUID.randomUUID()
            set(PREF_UNIQUE_UUID, uuid.toString())
            uuid
        }
    }

    companion object {
        private const val PREF_UNIQUE_UUID = "PREF_UNIQUE_UUID"
    }
}