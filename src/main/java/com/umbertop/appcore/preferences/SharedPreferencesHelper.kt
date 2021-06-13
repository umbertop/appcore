package com.umbertop.appcore.preferences

import android.content.Context
import android.content.SharedPreferences

@Suppress("UNCHECKED_CAST")
class SharedPreferencesHelper(context: Context, name: String) : IPreferenceHelper {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        name, Context.MODE_PRIVATE
    )

    override fun <T> get(key: String, default: T): T {
        sharedPreferences.run {
            return when (default) {
                is Set<*> -> getStringSet(key, default as Set<String>) as T
                is String -> getString(key, default) as T
                is Boolean -> getBoolean(key, default) as T
                is Float -> getFloat(key, default) as T
                is Int -> getInt(key, default) as T
                else -> default
            }
        }
    }

    override fun <T> set(key: String, value: T): Boolean {
        return if (key.isNotEmpty()) {
            sharedPreferences.edit()?.run {
                when (value) {
                    is Set<*> -> putStringSet(key, value as Set<String>)
                    is String -> putString(key, value)
                    is Boolean -> putBoolean(key, value)
                    is Float -> putFloat(key, value)
                    is Int -> putInt(key, value)
                    else -> return false
                }

                commit()
            } ?: false
        } else {
            false
        }
    }

    override fun remove(vararg keys: String): Boolean {
        sharedPreferences.edit()?.run {
            keys.forEach { key ->
                remove(key)
            }

            return commit()
        }

        return false
    }
}