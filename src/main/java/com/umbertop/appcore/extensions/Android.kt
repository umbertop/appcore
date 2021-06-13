package com.umbertop.appcore.extensions

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import kotlin.reflect.KClass

/**
 * Toggles a view visibility
 */
var View.visible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

fun <T : View> View.getViewsByType(type: KClass<T>): List<T> {
    val result = mutableListOf<T>()

    if (this is ViewGroup) {
        for (view in this.children) {
            result.addAll(view.getViewsByType(type))
        }
    }

    if (type.isInstance(this)) {
        result.add(this as T)
    }

    return result
}

inline fun <reified T : View> View.getViewsByType(): List<T> = getViewsByType(T::class)

fun View.getViewsByTag(tag: Any): List<View> {
    val result = mutableListOf<View>()

    if (this is ViewGroup) {
        for (view in this.children) {
            result.addAll(view.getViewsByTag(tag))
        }
    }

    if(this.tag == tag){
        result.add(this)
    }

    return result
}